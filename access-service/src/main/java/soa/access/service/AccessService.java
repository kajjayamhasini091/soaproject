package soa.access.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import soa.access.dto.AccessRequest;
import soa.access.dto.AccessResponse;
import soa.access.model.Access;
import soa.access.repository.AccessRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccessService {

    private static final Logger log = LoggerFactory.getLogger(AccessService.class);

    private final AccessRepository accessRepository;
    private final RestTemplate restTemplate;
    private final RestTemplate directRestTemplate;
    private final DiscoveryClient discoveryClient;

    public AccessService(AccessRepository accessRepository,
                         RestTemplate restTemplate,
                         @Qualifier("directRestTemplate") RestTemplate directRestTemplate,
                         DiscoveryClient discoveryClient) {
        this.accessRepository = accessRepository;
        this.restTemplate = restTemplate;
        this.directRestTemplate = directRestTemplate;
        this.discoveryClient = discoveryClient;
    }

    @PostConstruct
    public void seedInitialAccess() {
        if (accessRepository.count() == 0) {
            // Seed active access for Divishka (user 1) to Content 1 & 2
            accessRepository.save(new Access(
                    null, 1L, 1L, "ACTIVE", "PREMIUM",
                    LocalDateTime.now(), LocalDateTime.now().plusDays(90),
                    "Full academic access granted to Team Lead"
            ));

            accessRepository.save(new Access(
                    null, 1L, 2L, "ACTIVE", "STANDARD",
                    LocalDateTime.now(), LocalDateTime.now().plusDays(60),
                    "Research paper access granted"
            ));

            // Seed active access for Hemalatha (user 2)
            accessRepository.save(new Access(
                    null, 2L, 1L, "ACTIVE", "STANDARD",
                    LocalDateTime.now(), LocalDateTime.now().plusDays(60),
                    "Textbook study access"
            ));

            // Seed active access for Nikhitha (user 3)
            accessRepository.save(new Access(
                    null, 3L, 4L, "ACTIVE", "FREE",
                    LocalDateTime.now(), LocalDateTime.now().plusDays(30),
                    "AI Book study access"
            ));
        }
    }

    /**
     * Inter-Service Call 1: Access -> Content
     * Fetch content resource details from Content Service
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> fetchContentDetails(Long contentId) {
        try {
            // First try Eureka discovery name
            String url = "http://content-service/api/content/" + contentId;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e1) {
            log.warn("Eureka service lookup for content-service failed: {}. Trying direct port 8082 fallback.", e1.getMessage());
            try {
                // Direct fallback for local execution before Eureka registration completes
                String fallbackUrl = "http://localhost:8082/api/content/" + contentId;
                return directRestTemplate.getForObject(fallbackUrl, Map.class);
            } catch (Exception e2) {
                log.error("Direct fallback to content-service failed: {}", e2.getMessage());
                return null;
            }
        }
    }

    /**
     * Inter-Service Call 2: Access -> Usage
     * Informs Usage Service that an access grant has been authorized/initialized.
     */
    public void notifyUsageService(Long userId, Long contentId, String planType) {
        try {
            String url = "http://usage-service/api/usage/init-permission";
            Map<String, Object> payload = Map.of(
                    "userId", userId,
                    "contentId", contentId,
                    "planType", planType,
                    "timestamp", System.currentTimeMillis()
            );
            restTemplate.postForObject(url, payload, Map.class);
            log.info("Successfully notified Usage Service of permission initialization for user {} on content {}", userId, contentId);
        } catch (Exception e1) {
            log.warn("Eureka notify to usage-service failed: {}. Trying direct port 8084.", e1.getMessage());
            try {
                String fallbackUrl = "http://localhost:8084/api/usage/init-permission";
                Map<String, Object> payload = Map.of(
                        "userId", userId,
                        "contentId", contentId,
                        "planType", planType,
                        "timestamp", System.currentTimeMillis()
                );
                directRestTemplate.postForObject(fallbackUrl, payload, Map.class);
            } catch (Exception e2) {
                log.warn("Direct fallback to usage-service also failed (usage-service may be starting up): {}", e2.getMessage());
            }
        }
    }

    /**
     * Main Workflow: Grant Content Access
     * Implements Inter-service communication: (Access -> Content -> Usage)
     */
    public AccessResponse grantAccess(AccessRequest request) {
        if (request.getUserId() == null || request.getContentId() == null) {
            throw new IllegalArgumentException("userId and contentId are required");
        }

        // 1. Inter-service call: Access -> Content
        Map<String, Object> contentDetails = fetchContentDetails(request.getContentId());
        String contentTitle = contentDetails != null && contentDetails.get("title") != null
                ? (String) contentDetails.get("title") : "Digital Knowledge Resource #" + request.getContentId();
        String contentType = contentDetails != null && contentDetails.get("type") != null
                ? (String) contentDetails.get("type") : "RESOURCE";

        // 2. Determine plan and expiration
        String plan = request.getPlanType() != null ? request.getPlanType().toUpperCase() : "STANDARD";
        int days = request.getDurationDays() != null && request.getDurationDays() > 0 ? request.getDurationDays() : 30;

        // Check if access already exists
        Optional<Access> existingOpt = accessRepository.findByUserIdAndContentId(request.getUserId(), request.getContentId());
        Access access;
        if (existingOpt.isPresent()) {
            access = existingOpt.get();
            access.setAccessStatus("ACTIVE");
            access.setPlanType(plan);
            access.setExpiresAt(LocalDateTime.now().plusDays(days));
            access.setNotes(request.getNotes() != null ? request.getNotes() : access.getNotes());
        } else {
            access = new Access(
                    null,
                    request.getUserId(),
                    request.getContentId(),
                    "ACTIVE",
                    plan,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(days),
                    request.getNotes() != null ? request.getNotes() : "Access granted for " + plan + " tier"
            );
        }

        Access saved = accessRepository.save(access);

        // 3. Inter-service call: Access -> Usage (inform usage service)
        notifyUsageService(request.getUserId(), request.getContentId(), plan);

        return new AccessResponse(
                saved.getAccessId(),
                saved.getUserId(),
                saved.getContentId(),
                contentTitle,
                contentType,
                saved.getAccessStatus(),
                saved.getPlanType(),
                saved.getGrantedAt(),
                saved.getExpiresAt(),
                saved.isCurrentlyActive(),
                "Access successfully granted. Inter-service sync completed (Access -> Content -> Usage)."
        );
    }

    public Map<String, Object> checkAccess(Long userId, Long contentId) {
        Optional<Access> accessOpt = accessRepository.findByUserIdAndContentId(userId, contentId);
        if (accessOpt.isPresent()) {
            Access a = accessOpt.get();
            boolean active = a.isCurrentlyActive();
            return Map.of(
                    "hasAccess", active,
                    "accessId", a.getAccessId(),
                    "status", a.getAccessStatus(),
                    "planType", a.getPlanType() != null ? a.getPlanType() : "FREE",
                    "expiresAt", a.getExpiresAt() != null ? a.getExpiresAt().toString() : "NEVER",
                    "message", active ? "Active access permission confirmed" : "Access expired or inactive"
            );
        } else {
            return Map.of(
                    "hasAccess", false,
                    "status", "NONE",
                    "message", "No access permission found for this user and content"
            );
        }
    }

    public boolean revokeAccess(Long accessId) {
        return accessRepository.findById(accessId).map(access -> {
            access.setAccessStatus("REVOKED");
            accessRepository.save(access);
            return true;
        }).orElse(false);
    }

    public List<AccessResponse> getAccessByUserId(Long userId) {
        return accessRepository.findByUserId(userId).stream().map(a -> {
            Map<String, Object> details = fetchContentDetails(a.getContentId());
            String title = details != null && details.get("title") != null ? (String) details.get("title") : "Content #" + a.getContentId();
            String type = details != null && details.get("type") != null ? (String) details.get("type") : "RESOURCE";
            return new AccessResponse(
                    a.getAccessId(),
                    a.getUserId(),
                    a.getContentId(),
                    title,
                    type,
                    a.getAccessStatus(),
                    a.getPlanType(),
                    a.getGrantedAt(),
                    a.getExpiresAt(),
                    a.isCurrentlyActive(),
                    "Retrieved from Access Service"
            );
        }).collect(Collectors.toList());
    }

    public List<Access> getAllGrants() {
        return accessRepository.findAll();
    }
}

package soa.usage.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import soa.usage.dto.*;
import soa.usage.model.Usage;
import soa.usage.repository.UsageRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsageService {
    private static final Logger log = LoggerFactory.getLogger(UsageService.class);
    private final UsageRepository usageRepository;
    private final RestTemplate restTemplate;
    private final RestTemplate directRestTemplate;

    public UsageService(UsageRepository usageRepository, RestTemplate restTemplate,
                        @Qualifier("directRestTemplate") RestTemplate directRestTemplate) {
        this.usageRepository = usageRepository;
        this.restTemplate = restTemplate;
        this.directRestTemplate = directRestTemplate;
    }

    @PostConstruct
    public void seedUsage() {
        if (usageRepository.count() == 0) {
            usageRepository.save(new Usage(null, 1L, 1L, 45.0, 8, LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), "COMPLETED", "Browser", "Initial reading session"));
            usageRepository.save(new Usage(null, 2L, 1L, 20.0, 4, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusMinutes(20), "IN_PROGRESS", "Browser", "Study session"));
            usageRepository.save(new Usage(null, 1L, 2L, 30.0, 5, LocalDateTime.now().minusHours(5), LocalDateTime.now().minusHours(4), "COMPLETED", "Mobile", "Research paper review"));
        }
    }

    @SuppressWarnings("unchecked")
    private String fetchContentTitle(Long contentId) {
        try {
            Map<String, Object> details = restTemplate.getForObject("http://content-service/api/content/" + contentId, Map.class);
            return details != null && details.get("title") != null ? (String) details.get("title") : "Resource #" + contentId;
        } catch (Exception e1) {
            try {
                Map<String, Object> details = directRestTemplate.getForObject("http://localhost:8082/api/content/" + contentId, Map.class);
                return details != null && details.get("title") != null ? (String) details.get("title") : "Resource #" + contentId;
            } catch (Exception e2) {
                return "Resource #" + contentId;
            }
        }
    }

    public Usage startSession(ReadingSessionRequest req) {
        Usage usage = new Usage();
        usage.setUserId(req.getUserId());
        usage.setContentId(req.getContentId());
        usage.setDeviceInfo(req.getDeviceInfo() != null ? req.getDeviceInfo() : "Browser");
        usage.setLastPageRead(req.getStartPage() != null ? req.getStartPage() : 1);
        usage.setReadingStatus("IN_PROGRESS");
        usage.setSessionStart(LocalDateTime.now());
        return usageRepository.save(usage);
    }

    public Usage logReading(ReadingLogRequest req) {
        Usage usage;
        if (req.getUsageId() != null) {
            usage = usageRepository.findById(req.getUsageId()).orElse(null);
        } else {
            usage = usageRepository.findTopByUserIdAndContentIdAndReadingStatusOrderBySessionStartDesc(
                    req.getUserId(), req.getContentId(), "IN_PROGRESS").orElse(null);
        }
        if (usage == null) {
            usage = new Usage();
            usage.setUserId(req.getUserId());
            usage.setContentId(req.getContentId());
        }
        if (req.getDurationMinutes() != null) usage.setDurationMinutes(req.getDurationMinutes());
        if (req.getLastPageRead() != null) usage.setLastPageRead(req.getLastPageRead());
        if (req.getReadingStatus() != null) usage.setReadingStatus(req.getReadingStatus());
        if (req.getNotes() != null) usage.setNotes(req.getNotes());
        if ("COMPLETED".equals(req.getReadingStatus())) usage.setSessionEnd(LocalDateTime.now());
        return usageRepository.save(usage);
    }

    public Map<String, Object> initPermission(Map<String, Object> payload) {
        log.info("Permission initialized from Access Service: {}", payload);
        return Map.of("status", "acknowledged", "message", "Usage service received permission init", "timestamp", System.currentTimeMillis());
    }

    public List<UsageDTO> getHistoryByUser(Long userId) {
        return usageRepository.findByUserIdOrderBySessionStartDesc(userId).stream()
                .map(u -> new UsageDTO(u, fetchContentTitle(u.getContentId())))
                .collect(Collectors.toList());
    }

    public List<UsageDTO> getUsageByContent(Long contentId) {
        return usageRepository.findByContentIdOrderBySessionStartDesc(contentId).stream()
                .map(u -> new UsageDTO(u, fetchContentTitle(u.getContentId())))
                .collect(Collectors.toList());
    }

    public UsageAnalyticsSummary getAnalyticsSummary() {
        List<Usage> all = usageRepository.findAll();
        long totalSessions = all.size();
        double totalMinutes = all.stream().mapToDouble(u -> u.getDurationMinutes() != null ? u.getDurationMinutes() : 0).sum();
        long distinctUsers = all.stream().map(Usage::getUserId).distinct().count();
        long distinctResources = all.stream().map(Usage::getContentId).distinct().count();
        double avgMinutes = totalSessions > 0 ? totalMinutes / totalSessions : 0;
        Map<String, Long> byResource = all.stream()
                .collect(Collectors.groupingBy(u -> "Content #" + u.getContentId(), Collectors.counting()));
        return new UsageAnalyticsSummary(totalSessions, totalMinutes / 60.0, distinctUsers, distinctResources, avgMinutes, byResource);
    }
}

package soa.content.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import soa.content.model.Content;
import soa.content.repository.ContentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContentService {

    private final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @PostConstruct
    public void seedInitialContent() {
        if (contentRepository.count() == 0) {
            contentRepository.save(new Content(
                    null,
                    "Service-Oriented Architecture & Microservices Engineering",
                    "EBOOK",
                    "Dr. K. L. Rao",
                    "Computer Science",
                    "Comprehensive textbook covering SOA principles, service contracts, REST architecture, Eureka discovery, and API Gateways.",
                    "https://assets.klu.ac.in/digital-library/soa-microservices.pdf",
                    "Chapter 1: Foundations of Service-Oriented Architecture (SOA)\n\nService-Oriented Architecture is an architectural approach in which applications make use of services available in the network. In this paradigm, services provide functionality with a clean interface contract.\n\nMicroservices take SOA to its natural evolution: independent, decoupled services deployed autonomously with lightweight protocols (HTTP/REST, gRPC) and decentralized data management.",
                    "FREE",
                    15
            ));

            contentRepository.save(new Content(
                    null,
                    "Distributed Consensus & Fault Tolerance in Cloud Networks",
                    "RESEARCH_PAPER",
                    "Divishka Bypilla & Team",
                    "Cloud Computing",
                    "High-impact research paper examining Paxos, Raft, and client-side load balancing resilience under heavy network partitions.",
                    "https://assets.klu.ac.in/papers/distributed-consensus-2026.pdf",
                    "Abstract: Scalable microservices depend heavily on robust service registries and adaptive load balancers. This paper analyzes Eureka heartbeat timers, threshold self-preservation modes, and round-robin traffic distribution across resilient edge gateways.",
                    "STANDARD",
                    8
            ));

            contentRepository.save(new Content(
                    null,
                    "Modern Cryptographic Protocols: JWT, OAuth2, and Zero-Trust",
                    "JOURNAL",
                    "IEEE Transactions on Software Security",
                    "Security",
                    "In-depth analysis of JSON Web Tokens, HMAC-SHA256 signatures, stateless authorization, and gateway token verification filters.",
                    "https://assets.klu.ac.in/journals/jwt-oauth2-sec.pdf",
                    "Introduction: Stateless authentication removes server-side session bottlenecks. A JSON Web Token encapsulates cryptographic identity, role authorizations, and expiration timestamps into an encoded compact string.",
                    "PREMIUM",
                    22
            ));

            contentRepository.save(new Content(
                    null,
                    "Artificial Intelligence Systems & LLM Agent Orchestration",
                    "EBOOK",
                    "Kilari Hemalatha",
                    "Artificial Intelligence",
                    "Exploration of multi-agent cognitive architectures, context management, and autonomous tool calling in modern AI systems.",
                    "https://assets.klu.ac.in/books/ai-agents.pdf",
                    "Overview: Autonomous software agents utilize reasoning loops, perception, and external tools to complete multi-step software engineering goals reliably.",
                    "FREE",
                    12
            ));
        }
    }

    public List<Content> getAllContent(String type, String category, String query) {
        if (query != null && !query.trim().isEmpty()) {
            return contentRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query.trim(), query.trim());
        }
        if (type != null && !type.trim().isEmpty()) {
            return contentRepository.findByTypeIgnoreCase(type.trim());
        }
        if (category != null && !category.trim().isEmpty()) {
            return contentRepository.findByCategoryIgnoreCase(category.trim());
        }
        return contentRepository.findAll();
    }

    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }

    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    public Content updateContent(Long id, Content updated) {
        return contentRepository.findById(id).map(existing -> {
            if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
            if (updated.getType() != null) existing.setType(updated.getType());
            if (updated.getAuthor() != null) existing.setAuthor(updated.getAuthor());
            if (updated.getCategory() != null) existing.setCategory(updated.getCategory());
            if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
            if (updated.getFileUrl() != null) existing.setFileUrl(updated.getFileUrl());
            if (updated.getContentBody() != null) existing.setContentBody(updated.getContentBody());
            if (updated.getAccessTier() != null) existing.setAccessTier(updated.getAccessTier());
            if (updated.getTotalPages() != null) existing.setTotalPages(updated.getTotalPages());
            return contentRepository.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Content resource with ID " + id + " not found"));
    }

    public boolean deleteContent(Long id) {
        if (contentRepository.existsById(id)) {
            contentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

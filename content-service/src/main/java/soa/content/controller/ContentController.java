package soa.content.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.content.model.Content;
import soa.content.service.ContentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@CrossOrigin(origins = "*")
public class ContentController {

    private final ContentService contentService;

    @Value("${server.port:8082}")
    private String serverPort;

    @Value("${eureka.instance.instance-id:content-service-default}")
    private String instanceId;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<List<Content>> getAllContent(
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "q", required = false) String q) {

        return ResponseEntity.ok(
                contentService.getAllContent(type, category, q)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContentById(@PathVariable("id") Long id) {

        return contentService.getContentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/read")
    public ResponseEntity<?> readContent(@PathVariable("id") Long id) {

        return contentService.getContentById(id)
                .map(content -> ResponseEntity.ok(Map.of(
                        "contentId", content.getContentId(),
                        "title", content.getTitle(),
                        "author", content.getAuthor(),
                        "type", content.getType(),
                        "totalPages", content.getTotalPages(),
                        "contentBody",
                        content.getContentBody() != null
                                ? content.getContentBody()
                                : "Content body is empty.",
                        "servedByPort", serverPort
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createContent(@RequestBody Content content) {

        try {
            Content saved = contentService.createContent(content);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContent(
            @PathVariable("id") Long id,
            @RequestBody Content content) {

        try {
            Content updated = contentService.updateContent(id, content);

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContent(
            @PathVariable("id") Long id) {

        boolean deleted = contentService.deleteContent(id);

        if (deleted) {

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Content resource deleted successfully",
                            "id", id
                    )
            );

        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Content not found"));
        }
    }

    @GetMapping("/server-info")
    public ResponseEntity<?> getServerInfo() {

        return ResponseEntity.ok(
                Map.of(
                        "service", "content-service",
                        "port", serverPort,
                        "instanceId", instanceId,
                        "status", "UP",
                        "timestamp", System.currentTimeMillis()
                )
        );
    }
}
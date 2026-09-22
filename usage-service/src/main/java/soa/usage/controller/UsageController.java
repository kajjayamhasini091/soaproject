package soa.usage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.usage.dto.*;
import soa.usage.model.Usage;
import soa.usage.service.UsageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usage")
@CrossOrigin(origins = "*")
public class UsageController {
    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody ReadingSessionRequest req) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(usageService.startSession(req)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PostMapping("/log")
    public ResponseEntity<?> logReading(@RequestBody ReadingLogRequest req) {
        try { return ResponseEntity.ok(usageService.logReading(req)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PostMapping("/init-permission")
    public ResponseEntity<?> initPermission(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(usageService.initPermission(payload));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<UsageDTO>> getHistory(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(usageService.getHistoryByUser(userId));
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<UsageDTO>> getByContent(@PathVariable("contentId") Long contentId) {
        return ResponseEntity.ok(usageService.getUsageByContent(contentId));
    }

    @GetMapping("/analytics/summary")
    public ResponseEntity<UsageAnalyticsSummary> getAnalytics() {
        return ResponseEntity.ok(usageService.getAnalyticsSummary());
    }

    @GetMapping("/server-info")
    public ResponseEntity<?> serverInfo() {
        return ResponseEntity.ok(Map.of("service", "usage-service", "status", "UP", "timestamp", System.currentTimeMillis()));
    }
}

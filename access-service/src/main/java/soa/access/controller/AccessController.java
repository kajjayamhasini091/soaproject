package soa.access.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.access.dto.AccessRequest;
import soa.access.dto.AccessResponse;
import soa.access.model.Access;
import soa.access.service.AccessService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/access")
@CrossOrigin(origins = "*")
public class AccessController {

    private final AccessService accessService;

    @Value("${server.port:8083}")
    private String serverPort;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @PostMapping("/grant")
    public ResponseEntity<?> grantAccess(@RequestBody AccessRequest request) {
        try {
            AccessResponse response = accessService.grantAccess(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestAccess(@RequestBody AccessRequest request) {
        return grantAccess(request);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAccess(@RequestParam("userId") Long userId, @RequestParam("contentId") Long contentId) {
        Map<String, Object> result = accessService.checkAccess(userId, contentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccessResponse>> getAccessByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(accessService.getAccessByUserId(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Access>> getAllGrants() {
        return ResponseEntity.ok(accessService.getAllGrants());
    }

    @PutMapping("/revoke/{accessId}")
    public ResponseEntity<?> revokeAccess(@PathVariable("accessId") Long accessId) {
        boolean revoked = accessService.revokeAccess(accessId);
        if (revoked) {
            return ResponseEntity.ok(Map.of("message", "Access revoked successfully", "accessId", accessId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Access grant not found"));
        }
    }

    @GetMapping("/server-info")
    public ResponseEntity<?> getServerInfo() {
        return ResponseEntity.ok(Map.of(
                "service", "access-service",
                "port", serverPort,
                "status", "UP",
                "timestamp", System.currentTimeMillis()
        ));
    }
}

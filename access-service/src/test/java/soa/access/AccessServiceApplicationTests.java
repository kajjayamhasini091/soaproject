package soa.access;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import soa.access.dto.AccessRequest;
import soa.access.dto.AccessResponse;
import soa.access.model.Access;
import soa.access.service.AccessService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccessServiceApplicationTests {

    @Autowired
    private AccessService accessService;

    @Test
    void contextLoads() {
        assertNotNull(accessService);
    }

    @Test
    void testInitialSeedAccess() {
        List<Access> grants = accessService.getAllGrants();
        assertFalse(grants.isEmpty(), "Initial access grants should be seeded");
    }

    @Test
    void testCheckAccess() {
        // User 1 has seeded access to content 1
        Map<String, Object> check = accessService.checkAccess(1L, 1L);
        assertTrue((Boolean) check.get("hasAccess"));

        // Non-existent access
        Map<String, Object> badCheck = accessService.checkAccess(999L, 999L);
        assertFalse((Boolean) badCheck.get("hasAccess"));
    }

    @Test
    void testGrantAndRevokeWorkflow() {
        AccessRequest req = new AccessRequest(50L, 100L, "PREMIUM", 15, "Automated test grant");
        AccessResponse res = accessService.grantAccess(req);
        assertNotNull(res.getAccessId());
        assertEquals("ACTIVE", res.getAccessStatus());
        assertTrue(res.isActive());

        // Revoke
        boolean revoked = accessService.revokeAccess(res.getAccessId());
        assertTrue(revoked);

        Map<String, Object> checkAfter = accessService.checkAccess(50L, 100L);
        assertFalse((Boolean) checkAfter.get("hasAccess"));
    }
}

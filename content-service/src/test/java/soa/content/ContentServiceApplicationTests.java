package soa.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import soa.content.model.Content;
import soa.content.service.ContentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContentServiceApplicationTests {

    @Autowired
    private ContentService contentService;

    @Test
    void contextLoads() {
        assertNotNull(contentService);
    }

    @Test
    void testInitialSeedContent() {
        List<Content> all = contentService.getAllContent(null, null, null);
        assertFalse(all.isEmpty(), "Initial seed content should not be empty");
        assertTrue(all.size() >= 4, "Should have seeded at least 4 items");
    }

    @Test
    void testCreateReadUpdateDeleteWorkflow() {
        Content newDoc = new Content(
                null,
                "Microservices Design Patterns",
                "EBOOK",
                "Chris Richardson",
                "Architecture",
                "Patterns for building resilient distributed architectures",
                "https://example.com/pattern.pdf",
                "Pattern 1: API Gateway, Pattern 2: Database per service...",
                "FREE",
                20
        );

        Content created = contentService.createContent(newDoc);
        assertNotNull(created.getContentId());
        assertEquals("Microservices Design Patterns", created.getTitle());

        // Read
        Optional<Content> found = contentService.getContentById(created.getContentId());
        assertTrue(found.isPresent());
        assertEquals("Architecture", found.get().getCategory());

        // Update
        created.setTitle("Microservices Design Patterns - 2nd Edition");
        Content updated = contentService.updateContent(created.getContentId(), created);
        assertEquals("Microservices Design Patterns - 2nd Edition", updated.getTitle());

        // Delete
        boolean deleted = contentService.deleteContent(created.getContentId());
        assertTrue(deleted);
        assertFalse(contentService.getContentById(created.getContentId()).isPresent());
    }

    @Test
    void testSearchAndFilter() {
        List<Content> results = contentService.getAllContent("EBOOK", null, null);
        assertNotNull(results);
        for (Content c : results) {
            assertEquals("EBOOK", c.getType());
        }
    }
}

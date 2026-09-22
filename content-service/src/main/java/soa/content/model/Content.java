package soa.content.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type; // EBOOK, JOURNAL, RESEARCH_PAPER, ARTICLE

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String category; // Computer Science, AI, Cloud Computing, etc.

    @Column(length = 2000)
    private String description;

    @Column(name = "file_url")
    private String fileUrl;

    @Lob
    @Column(name = "content_body", columnDefinition = "TEXT")
    private String contentBody;

    @Column(name = "access_tier")
    private String accessTier; // FREE, STANDARD, PREMIUM

    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Content() {
        this.createdAt = LocalDateTime.now();
    }

    public Content(Long contentId, String title, String type, String author, String category,
                   String description, String fileUrl, String contentBody, String accessTier, Integer totalPages) {
        this.contentId = contentId;
        this.title = title;
        this.type = type;
        this.author = author;
        this.category = category;
        this.description = description;
        this.fileUrl = fileUrl;
        this.contentBody = contentBody;
        this.accessTier = accessTier;
        this.totalPages = totalPages != null ? totalPages : 10;
        this.createdAt = LocalDateTime.now();
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getAccessTier() {
        return accessTier;
    }

    public void setAccessTier(String accessTier) {
        this.accessTier = accessTier;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

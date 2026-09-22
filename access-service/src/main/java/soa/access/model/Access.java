package soa.access.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_grants")
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id")
    private Long accessId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "access_status", nullable = false)
    private String accessStatus; // ACTIVE, EXPIRED, REVOKED, PENDING

    @Column(name = "plan_type")
    private String planType; // FREE, STANDARD, PREMIUM

    @Column(name = "granted_at")
    private LocalDateTime grantedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "notes")
    private String notes;

    public Access() {
        this.grantedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusDays(30);
    }

    public Access(Long accessId, Long userId, Long contentId, String accessStatus, String planType,
                  LocalDateTime grantedAt, LocalDateTime expiresAt, String notes) {
        this.accessId = accessId;
        this.userId = userId;
        this.contentId = contentId;
        this.accessStatus = accessStatus;
        this.planType = planType;
        this.grantedAt = grantedAt != null ? grantedAt : LocalDateTime.now();
        this.expiresAt = expiresAt != null ? expiresAt : LocalDateTime.now().plusDays(30);
        this.notes = notes;
    }

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(LocalDateTime grantedAt) {
        this.grantedAt = grantedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCurrentlyActive() {
        return "ACTIVE".equalsIgnoreCase(this.accessStatus) &&
                (this.expiresAt == null || this.expiresAt.isAfter(LocalDateTime.now()));
    }
}

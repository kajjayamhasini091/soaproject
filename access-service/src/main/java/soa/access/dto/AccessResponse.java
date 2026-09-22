package soa.access.dto;

import java.time.LocalDateTime;

public class AccessResponse {
    private Long accessId;
    private Long userId;
    private Long contentId;
    private String contentTitle;
    private String contentType;
    private String accessStatus;
    private String planType;
    private LocalDateTime grantedAt;
    private LocalDateTime expiresAt;
    private boolean active;
    private String message;

    public AccessResponse() {}

    public AccessResponse(Long accessId, Long userId, Long contentId, String contentTitle, String contentType,
                          String accessStatus, String planType, LocalDateTime grantedAt, LocalDateTime expiresAt,
                          boolean active, String message) {
        this.accessId = accessId;
        this.userId = userId;
        this.contentId = contentId;
        this.contentTitle = contentTitle;
        this.contentType = contentType;
        this.accessStatus = accessStatus;
        this.planType = planType;
        this.grantedAt = grantedAt;
        this.expiresAt = expiresAt;
        this.active = active;
        this.message = message;
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

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

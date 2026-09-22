package soa.access.dto;

public class AccessRequest {
    private Long userId;
    private Long contentId;
    private String planType; // FREE, STANDARD, PREMIUM
    private Integer durationDays; // default 30
    private String notes;

    public AccessRequest() {
        this.durationDays = 30;
    }

    public AccessRequest(Long userId, Long contentId, String planType, Integer durationDays, String notes) {
        this.userId = userId;
        this.contentId = contentId;
        this.planType = planType;
        this.durationDays = durationDays != null ? durationDays : 30;
        this.notes = notes;
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

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

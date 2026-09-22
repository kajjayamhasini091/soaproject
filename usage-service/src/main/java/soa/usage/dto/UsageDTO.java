package soa.usage.dto;

import soa.usage.model.Usage;

import java.time.LocalDateTime;

public class UsageDTO {
    private Long usageId;
    private Long userId;
    private Long contentId;
    private String contentTitle;
    private Double durationMinutes;
    private Integer lastPageRead;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
    private String readingStatus;
    private String deviceInfo;
    private String notes;

    public UsageDTO() {}

    public UsageDTO(Usage u, String contentTitle) {
        this.usageId = u.getUsageId();
        this.userId = u.getUserId();
        this.contentId = u.getContentId();
        this.contentTitle = contentTitle != null ? contentTitle : "Resource #" + u.getContentId();
        this.durationMinutes = u.getDurationMinutes();
        this.lastPageRead = u.getLastPageRead();
        this.sessionStart = u.getSessionStart();
        this.sessionEnd = u.getSessionEnd();
        this.readingStatus = u.getReadingStatus();
        this.deviceInfo = u.getDeviceInfo();
        this.notes = u.getNotes();
    }

    public Long getUsageId() {
        return usageId;
    }

    public void setUsageId(Long usageId) {
        this.usageId = usageId;
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

    public Double getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Double durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getLastPageRead() {
        return lastPageRead;
    }

    public void setLastPageRead(Integer lastPageRead) {
        this.lastPageRead = lastPageRead;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(LocalDateTime sessionStart) {
        this.sessionStart = sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(LocalDateTime sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

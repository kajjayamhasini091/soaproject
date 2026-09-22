package soa.usage.dto;

public class ReadingLogRequest {
    private Long usageId;
    private Long userId;
    private Long contentId;
    private Double durationMinutes;
    private Integer lastPageRead;
    private String readingStatus; // IN_PROGRESS, COMPLETED
    private String notes;

    public ReadingLogRequest() {}

    public ReadingLogRequest(Long usageId, Long userId, Long contentId, Double durationMinutes,
                             Integer lastPageRead, String readingStatus, String notes) {
        this.usageId = usageId;
        this.userId = userId;
        this.contentId = contentId;
        this.durationMinutes = durationMinutes;
        this.lastPageRead = lastPageRead;
        this.readingStatus = readingStatus;
        this.notes = notes;
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

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

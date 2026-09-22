package soa.usage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage_records")
public class Usage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long usageId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    @Column(name = "duration_minutes")
    private Double durationMinutes;

    @Column(name = "last_page_read")
    private Integer lastPageRead;

    @Column(name = "session_start")
    private LocalDateTime sessionStart;

    @Column(name = "session_end")
    private LocalDateTime sessionEnd;

    @Column(name = "reading_status")
    private String readingStatus; // IN_PROGRESS, COMPLETED

    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "notes")
    private String notes;

    public Usage() {
        this.sessionStart = LocalDateTime.now();
        this.readingStatus = "IN_PROGRESS";
        this.durationMinutes = 0.0;
        this.lastPageRead = 1;
    }

    public Usage(Long usageId, Long userId, Long contentId, Double durationMinutes,
                 Integer lastPageRead, LocalDateTime sessionStart, LocalDateTime sessionEnd,
                 String readingStatus, String deviceInfo, String notes) {
        this.usageId = usageId;
        this.userId = userId;
        this.contentId = contentId;
        this.durationMinutes = durationMinutes != null ? durationMinutes : 0.0;
        this.lastPageRead = lastPageRead != null ? lastPageRead : 1;
        this.sessionStart = sessionStart != null ? sessionStart : LocalDateTime.now();
        this.sessionEnd = sessionEnd;
        this.readingStatus = readingStatus != null ? readingStatus : "IN_PROGRESS";
        this.deviceInfo = deviceInfo;
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

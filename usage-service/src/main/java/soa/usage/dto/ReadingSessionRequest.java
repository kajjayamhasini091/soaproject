package soa.usage.dto;

public class ReadingSessionRequest {
    private Long userId;
    private Long contentId;
    private String deviceInfo;
    private Integer startPage;

    public ReadingSessionRequest() {}

    public ReadingSessionRequest(Long userId, Long contentId, String deviceInfo, Integer startPage) {
        this.userId = userId;
        this.contentId = contentId;
        this.deviceInfo = deviceInfo;
        this.startPage = startPage != null ? startPage : 1;
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

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }
}

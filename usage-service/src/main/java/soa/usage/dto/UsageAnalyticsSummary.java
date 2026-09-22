package soa.usage.dto;

import java.util.Map;

public class UsageAnalyticsSummary {
    private long totalSessions;
    private double totalReadingHours;
    private long distinctActiveUsers;
    private long distinctReadResources;
    private double averageSessionMinutes;
    private Map<String, Long> sessionsByResource;

    public UsageAnalyticsSummary() {}

    public UsageAnalyticsSummary(long totalSessions, double totalReadingHours, long distinctActiveUsers,
                                 long distinctReadResources, double averageSessionMinutes,
                                 Map<String, Long> sessionsByResource) {
        this.totalSessions = totalSessions;
        this.totalReadingHours = totalReadingHours;
        this.distinctActiveUsers = distinctActiveUsers;
        this.distinctReadResources = distinctReadResources;
        this.averageSessionMinutes = averageSessionMinutes;
        this.sessionsByResource = sessionsByResource;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(long totalSessions) {
        this.totalSessions = totalSessions;
    }

    public double getTotalReadingHours() {
        return totalReadingHours;
    }

    public void setTotalReadingHours(double totalReadingHours) {
        this.totalReadingHours = totalReadingHours;
    }

    public long getDistinctActiveUsers() {
        return distinctActiveUsers;
    }

    public void setDistinctActiveUsers(long distinctActiveUsers) {
        this.distinctActiveUsers = distinctActiveUsers;
    }

    public long getDistinctReadResources() {
        return distinctReadResources;
    }

    public void setDistinctReadResources(long distinctReadResources) {
        this.distinctReadResources = distinctReadResources;
    }

    public double getAverageSessionMinutes() {
        return averageSessionMinutes;
    }

    public void setAverageSessionMinutes(double averageSessionMinutes) {
        this.averageSessionMinutes = averageSessionMinutes;
    }

    public Map<String, Long> getSessionsByResource() {
        return sessionsByResource;
    }

    public void setSessionsByResource(Map<String, Long> sessionsByResource) {
        this.sessionsByResource = sessionsByResource;
    }
}

package com.ulpf.dto.stats;

import java.util.Map;

public class DashboardStatsResponse {
    private long totalEvents;
    private long totalRawLogs;
    private long failedEvents;
    private double parsingSuccessRate;
    private Map<String, Long> eventsBySeverity;
    private Map<String, Long> eventsByVendor;
    private Map<String, Long> topSourceIps;

    public DashboardStatsResponse() {}

    public DashboardStatsResponse(long totalEvents, long totalRawLogs, long failedEvents, double parsingSuccessRate,
                                  Map<String, Long> eventsBySeverity, Map<String, Long> eventsByVendor, Map<String, Long> topSourceIps) {
        this.totalEvents = totalEvents;
        this.totalRawLogs = totalRawLogs;
        this.failedEvents = failedEvents;
        this.parsingSuccessRate = parsingSuccessRate;
        this.eventsBySeverity = eventsBySeverity;
        this.eventsByVendor = eventsByVendor;
        this.topSourceIps = topSourceIps;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getTotalRawLogs() {
        return totalRawLogs;
    }

    public void setTotalRawLogs(long totalRawLogs) {
        this.totalRawLogs = totalRawLogs;
    }

    public long getFailedEvents() {
        return failedEvents;
    }

    public void setFailedEvents(long failedEvents) {
        this.failedEvents = failedEvents;
    }

    public double getParsingSuccessRate() {
        return parsingSuccessRate;
    }

    public void setParsingSuccessRate(double parsingSuccessRate) {
        this.parsingSuccessRate = parsingSuccessRate;
    }

    public Map<String, Long> getEventsBySeverity() {
        return eventsBySeverity;
    }

    public void setEventsBySeverity(Map<String, Long> eventsBySeverity) {
        this.eventsBySeverity = eventsBySeverity;
    }

    public Map<String, Long> getEventsByVendor() {
        return eventsByVendor;
    }

    public void setEventsByVendor(Map<String, Long> eventsByVendor) {
        this.eventsByVendor = eventsByVendor;
    }

    public Map<String, Long> getTopSourceIps() {
        return topSourceIps;
    }

    public void setTopSourceIps(Map<String, Long> topSourceIps) {
        this.topSourceIps = topSourceIps;
    }
}

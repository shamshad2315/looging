package com.ulpf.dto.ingestion;

public class IngestionResponse {
    private String jobId;
    private long receivedLogsCount;
    private long normalizedCount;
    private long errorCount;
    private String status;

    public IngestionResponse() {
    }

    public IngestionResponse(String jobId, long receivedLogsCount, long normalizedCount, long errorCount, String status) {
        this.jobId = jobId;
        this.receivedLogsCount = receivedLogsCount;
        this.normalizedCount = normalizedCount;
        this.errorCount = errorCount;
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public long getReceivedLogsCount() {
        return receivedLogsCount;
    }

    public void setReceivedLogsCount(long receivedLogsCount) {
        this.receivedLogsCount = receivedLogsCount;
    }

    public long getNormalizedCount() {
        return normalizedCount;
    }

    public void setNormalizedCount(long normalizedCount) {
        this.normalizedCount = normalizedCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long errorCount) {
        this.errorCount = errorCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

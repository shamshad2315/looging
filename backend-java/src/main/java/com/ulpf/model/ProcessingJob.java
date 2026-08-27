package com.ulpf.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "processing_jobs")
public class ProcessingJob {

    @Id
    private String id;

    @Indexed(unique = true)
    private String jobId;

    private String jobType; // FILE_UPLOAD, BULK_HTTP, SYSLOG_BATCH

    private String sourceName;

    @Indexed
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED

    private long totalLogsCount;

    private long processedCount;

    private long errorCount;

    @CreatedDate
    private Instant startTime;

    private Instant endTime;

    public ProcessingJob() {
    }

    public ProcessingJob(String jobId, String jobType, String sourceName, long totalLogsCount) {
        this.jobId = jobId;
        this.jobType = jobType;
        this.sourceName = sourceName;
        this.totalLogsCount = totalLogsCount;
        this.status = "PENDING";
        this.startTime = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTotalLogsCount() {
        return totalLogsCount;
    }

    public void setTotalLogsCount(long totalLogsCount) {
        this.totalLogsCount = totalLogsCount;
    }

    public long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(long processedCount) {
        this.processedCount = processedCount;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long errorCount) {
        this.errorCount = errorCount;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
}

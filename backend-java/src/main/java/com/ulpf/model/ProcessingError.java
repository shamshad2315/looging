package com.ulpf.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "processing_errors")
public class ProcessingError {

    @Id
    private String id;

    @Indexed
    private String rawLogId;

    @Indexed
    private String jobId;

    private String rawMessage;

    private String detectedFormat;

    private String errorMessage;

    private String stackTrace;

    @CreatedDate
    private Instant createdAt;

    public ProcessingError() {
    }

    public ProcessingError(String rawLogId, String jobId, String rawMessage, String detectedFormat, String errorMessage, String stackTrace) {
        this.rawLogId = rawLogId;
        this.jobId = jobId;
        this.rawMessage = rawMessage;
        this.detectedFormat = detectedFormat;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRawLogId() {
        return rawLogId;
    }

    public void setRawLogId(String rawLogId) {
        this.rawLogId = rawLogId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getDetectedFormat() {
        return detectedFormat;
    }

    public void setDetectedFormat(String detectedFormat) {
        this.detectedFormat = detectedFormat;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

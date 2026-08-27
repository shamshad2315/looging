package com.ulpf.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "raw_logs")
public class RawLog {

    @Id
    private String id;

    @Indexed
    private String hash;

    private String rawMessage;

    @Indexed
    private String source;

    private String protocol; // HTTP, FILE, SYSLOG

    @Indexed
    private String detectedFormat; // CISCO, FORTINET, PALO_ALTO, GENERIC, UNKNOWN

    private Map<String, Object> metadata;

    @CreatedDate
    private Instant createdAt;

    public RawLog() {
    }

    public RawLog(String hash, String rawMessage, String source, String protocol, String detectedFormat, Map<String, Object> metadata) {
        this.hash = hash;
        this.rawMessage = rawMessage;
        this.source = source;
        this.protocol = protocol;
        this.detectedFormat = detectedFormat;
        this.metadata = metadata;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDetectedFormat() {
        return detectedFormat;
    }

    public void setDetectedFormat(String detectedFormat) {
        this.detectedFormat = detectedFormat;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

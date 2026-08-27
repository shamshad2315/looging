package com.ulpf.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "normalized_events")
public class NormalizedEvent {

    @Id
    private String id;

    @Indexed(unique = true)
    private String eventId;

    @Indexed
    private String rawLogId;

    @Indexed
    private Instant timestamp;

    @Indexed
    private String vendor; // CISCO, FORTINET, PALO_ALTO, GENERIC

    @Indexed
    private String logType; // FIREWALL, IDS, SYSTEM, AUTH

    @Indexed
    private String severity; // CRITICAL, HIGH, MEDIUM, LOW, INFO

    @Indexed
    private String sourceIp;

    @Indexed
    private String destinationIp;

    private Integer sourcePort;

    private Integer destinationPort;

    @Indexed
    private String action; // ALLOW, DENY, DROP, BLOCK, ALERT

    private String protocol; // TCP, UDP, ICMP

    @Indexed
    private String user;

    private String rawMessage;

    private Map<String, Object> metadata;

    @CreatedDate
    private Instant createdAt;

    public NormalizedEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getRawLogId() {
        return rawLogId;
    }

    public void setRawLogId(String rawLogId) {
        this.rawLogId = rawLogId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(Integer destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
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

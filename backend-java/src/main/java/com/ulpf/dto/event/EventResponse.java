package com.ulpf.dto.event;

import java.time.Instant;
import java.util.Map;

public class EventResponse {
    private String eventId;
    private Instant timestamp;
    private String vendor;
    private String logType;
    private String severity;
    private String sourceIp;
    private String destinationIp;
    private Integer sourcePort;
    private Integer destinationPort;
    private String action;
    private String protocol;
    private String user;
    private String rawMessage;
    private Map<String, Object> metadata;

    public EventResponse() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public static EventResponse fromEntity(com.ulpf.model.NormalizedEvent entity) {
        if (entity == null) return null;
        EventResponse dto = new EventResponse();
        dto.setEventId(entity.getId());
        dto.setTimestamp(entity.getTimestamp());
        dto.setVendor(entity.getVendor());
        dto.setLogType(entity.getLogType());
        dto.setSeverity(entity.getSeverity());
        dto.setSourceIp(entity.getSourceIp());
        dto.setDestinationIp(entity.getDestinationIp());
        dto.setSourcePort(entity.getSourcePort());
        dto.setDestinationPort(entity.getDestinationPort());
        dto.setAction(entity.getAction());
        dto.setProtocol(entity.getProtocol());
        dto.setUser(entity.getUser());
        dto.setRawMessage(entity.getRawMessage());
        dto.setMetadata(entity.getMetadata());
        return dto;
    }
}

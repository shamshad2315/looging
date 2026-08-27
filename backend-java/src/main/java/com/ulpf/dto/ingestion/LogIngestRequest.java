package com.ulpf.dto.ingestion;

import java.util.List;
import java.util.Map;

public class LogIngestRequest {
    private String source;
    private String protocol; // HTTP, SYSLOG
    private List<String> logs;
    private Map<String, Object> metadata;

    public LogIngestRequest() {
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

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}

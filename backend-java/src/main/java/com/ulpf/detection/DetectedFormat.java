package com.ulpf.detection;

public enum DetectedFormat {
    CISCO("CISCO", "Cisco ASA / Firewall Format"),
    FORTINET("FORTINET", "Fortinet FortiGate KV Format"),
    PALO_ALTO("PALO_ALTO", "Palo Alto Firewall Format"),
    CEF("CEF", "Common Event Format"),
    LEEF("LEEF", "Log Event Extended Format"),
    JSON("JSON", "Structured JSON Log"),
    GENERIC("GENERIC", "Generic Syslog Format"),
    UNKNOWN("UNKNOWN", "Unrecognized Format");

    private final String code;
    private final String description;

    DetectedFormat(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

package com.ulpf.dto.ingestion;

import jakarta.validation.constraints.NotBlank;

public class SingleLogIngestRequest {

    @NotBlank(message = "rawMessage cannot be blank")
    private String rawMessage;

    private String vendorHint;

    private String sourceIp;

    public SingleLogIngestRequest() {
    }

    public SingleLogIngestRequest(String rawMessage, String vendorHint, String sourceIp) {
        this.rawMessage = rawMessage;
        this.vendorHint = vendorHint;
        this.sourceIp = sourceIp;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getVendorHint() {
        return vendorHint;
    }

    public void setVendorHint(String vendorHint) {
        this.vendorHint = vendorHint;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}

package com.ulpf.detection;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FormatDetector {

    private static final Pattern CISCO_PATTERN = Pattern.compile("(?:%ASA-|%FWSM-|%PIX-|%CGW-|cisco)", Pattern.CASE_INSENSITIVE);
    private static final Pattern FORTINET_PATTERN = Pattern.compile("(?:type=\"?log\"?|devname=|devid=|logid=)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PALO_ALTO_PATTERN = Pattern.compile("(?:TRAFFIC|THREAT|SYSTEM|PAN-OS|PaloAlto)", Pattern.CASE_INSENSITIVE);
    private static final Pattern CEF_PATTERN = Pattern.compile("^CEF:\\d+\\|");
    private static final Pattern LEEF_PATTERN = Pattern.compile("^LEEF:\\d+\\.\\d+\\|");

    public DetectedFormat detect(String rawLog) {
        if (rawLog == null || rawLog.isBlank()) {
            return DetectedFormat.UNKNOWN;
        }

        String trimmed = rawLog.trim();

        if ((trimmed.startsWith("{") && trimmed.endsWith("}")) || (trimmed.startsWith("[") && trimmed.endsWith("]"))) {
            return DetectedFormat.JSON;
        }

        if (CEF_PATTERN.matcher(trimmed).find()) {
            return DetectedFormat.CEF;
        }

        if (LEEF_PATTERN.matcher(trimmed).find()) {
            return DetectedFormat.LEEF;
        }

        if (FORTINET_PATTERN.matcher(trimmed).find()) {
            return DetectedFormat.FORTINET;
        }

        if (CISCO_PATTERN.matcher(trimmed).find()) {
            return DetectedFormat.CISCO;
        }

        if (PALO_ALTO_PATTERN.matcher(trimmed).find()) {
            return DetectedFormat.PALO_ALTO;
        }

        return DetectedFormat.GENERIC;
    }
}

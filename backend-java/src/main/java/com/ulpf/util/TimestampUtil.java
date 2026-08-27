package com.ulpf.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class TimestampUtil {

    public static Instant parseOrNow(String timestampStr) {
        if (timestampStr == null || timestampStr.isBlank()) {
            return Instant.now();
        }
        try {
            return Instant.parse(timestampStr);
        } catch (Exception e) {
            try {
                return DateTimeFormatter.ISO_DATE_TIME.parse(timestampStr, Instant::from);
            } catch (Exception ex) {
                return Instant.now();
            }
        }
    }
}

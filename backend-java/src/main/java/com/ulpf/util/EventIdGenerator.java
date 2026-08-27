package com.ulpf.util;

import java.util.UUID;

public class EventIdGenerator {

    public static String generateEventId(String vendor) {
        String prefix = (vendor != null && !vendor.isBlank()) ? vendor.toUpperCase().replaceAll("[^A-Z0-9]", "") : "EVT";
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        return prefix + "-" + System.currentTimeMillis() + "-" + uuidPart;
    }
}

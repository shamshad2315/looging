package com.ulpf.normalization;

import com.ulpf.model.NormalizedEvent;
import org.springframework.stereotype.Component;

@Component
public class EventValidator {

    public boolean validate(NormalizedEvent event) {
        if (event == null) {
            return false;
        }
        if (event.getEventId() == null || event.getEventId().isBlank()) {
            return false;
        }
        if (event.getVendor() == null || event.getVendor().isBlank()) {
            return false;
        }
        return true;
    }
}

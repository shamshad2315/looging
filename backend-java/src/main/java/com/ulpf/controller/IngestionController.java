package com.ulpf.controller;

import com.ulpf.detection.DetectedFormat;
import com.ulpf.detection.FormatDetector;
import com.ulpf.dto.common.ApiResponse;
import com.ulpf.dto.ingestion.SingleLogIngestRequest;
import com.ulpf.model.NormalizedEvent;
import com.ulpf.model.RawLog;
import com.ulpf.normalization.FieldMapper;
import com.ulpf.repository.NormalizedEventRepository;
import com.ulpf.repository.RawLogRepository;
import com.ulpf.util.HashUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ingest")
@CrossOrigin(origins = "*")
public class IngestionController {

    private static final Logger log = LoggerFactory.getLogger(IngestionController.class);

    private final RawLogRepository rawLogRepository;
    private final NormalizedEventRepository eventRepository;
    private final FormatDetector formatDetector;
    private final FieldMapper fieldMapper;

    public IngestionController(RawLogRepository rawLogRepository,
                               NormalizedEventRepository eventRepository,
                               FormatDetector formatDetector,
                               FieldMapper fieldMapper) {
        this.rawLogRepository = rawLogRepository;
        this.eventRepository = eventRepository;
        this.formatDetector = formatDetector;
        this.fieldMapper = fieldMapper;
    }

    @PostMapping("/text")
    public ResponseEntity<ApiResponse<NormalizedEvent>> ingestSingleLog(@Valid @RequestBody SingleLogIngestRequest request) {
        String rawMsg = request.getRawMessage();
        String hash = HashUtil.sha256(rawMsg);

        RawLog rawLog = new RawLog();
        rawLog.setRawMessage(rawMsg);
        rawLog.setHash(hash);
        rawLog.setSource(request.getSourceIp() != null ? request.getSourceIp() : "127.0.0.1");
        rawLog.setProtocol("HTTP");
        rawLog.setCreatedAt(Instant.now());
        RawLog savedRaw = rawLogRepository.save(rawLog);

        DetectedFormat detected = formatDetector.detect(rawMsg);
        String vendor = (request.getVendorHint() != null && !request.getVendorHint().isBlank())
                ? request.getVendorHint().toUpperCase()
                : detected.getCode();

        Map<String, Object> kvMap = fieldMapper.parseRawToKeyValue(rawMsg, vendor);

        NormalizedEvent event = new NormalizedEvent();
        event.setEventId("EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        event.setRawLogId(savedRaw.getId());
        event.setTimestamp(Instant.now());
        event.setVendor(vendor);
        event.setLogType("FIREWALL");
        event.setSeverity(kvMap.containsKey("severity") ? kvMap.get("severity").toString().toUpperCase() : "HIGH");
        event.setSourceIp(kvMap.containsKey("src_ip") ? kvMap.get("src_ip").toString() : (kvMap.containsKey("sourceip") ? kvMap.get("sourceip").toString() : "192.168.1.100"));
        event.setDestinationIp(kvMap.containsKey("dst_ip") ? kvMap.get("dst_ip").toString() : (kvMap.containsKey("destinationip") ? kvMap.get("destinationip").toString() : "10.0.0.1"));
        event.setAction(kvMap.containsKey("action") ? kvMap.get("action").toString().toUpperCase() : "DENY");
        event.setRawMessage(rawMsg);
        event.setMetadata(kvMap);
        event.setCreatedAt(Instant.now());

        NormalizedEvent savedEvent = eventRepository.save(event);
        log.info("Successfully ingested log: eventId={}", savedEvent.getEventId());

        return ResponseEntity.ok(ApiResponse.ok(savedEvent));
    }
}

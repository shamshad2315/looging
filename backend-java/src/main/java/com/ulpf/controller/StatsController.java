package com.ulpf.controller;

import com.ulpf.dto.common.ApiResponse;
import com.ulpf.dto.stats.DashboardStatsResponse;
import com.ulpf.model.NormalizedEvent;
import com.ulpf.repository.NormalizedEventRepository;
import com.ulpf.repository.ProcessingErrorRepository;
import com.ulpf.repository.RawLogRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    private final NormalizedEventRepository eventRepository;
    private final RawLogRepository rawLogRepository;
    private final ProcessingErrorRepository errorRepository;
    private final MongoTemplate mongoTemplate;

    public StatsController(NormalizedEventRepository eventRepository,
                           RawLogRepository rawLogRepository,
                           ProcessingErrorRepository errorRepository,
                           MongoTemplate mongoTemplate) {
        this.eventRepository = eventRepository;
        this.rawLogRepository = rawLogRepository;
        this.errorRepository = errorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        long totalEvents = eventRepository.count();
        long totalRawLogs = rawLogRepository.count();
        long failedEvents = errorRepository.count();

        double successRate = 100.0;
        if (totalRawLogs > 0) {
            successRate = Math.round(((double) totalEvents / totalRawLogs) * 10000.0) / 100.0;
        }

        List<NormalizedEvent> allEvents = eventRepository.findAll();
        Map<String, Long> severityCounts = new HashMap<>();
        Map<String, Long> vendorCounts = new HashMap<>();
        Map<String, Long> sourceIpCounts = new HashMap<>();

        for (NormalizedEvent e : allEvents) {
            if (e.getSeverity() != null) {
                severityCounts.put(e.getSeverity(), severityCounts.getOrDefault(e.getSeverity(), 0L) + 1);
            }
            if (e.getVendor() != null) {
                vendorCounts.put(e.getVendor(), vendorCounts.getOrDefault(e.getVendor(), 0L) + 1);
            }
            if (e.getSourceIp() != null) {
                sourceIpCounts.put(e.getSourceIp(), sourceIpCounts.getOrDefault(e.getSourceIp(), 0L) + 1);
            }
        }

        DashboardStatsResponse stats = new DashboardStatsResponse(
                totalEvents, totalRawLogs, failedEvents, successRate,
                severityCounts, vendorCounts, sourceIpCounts
        );

        return ResponseEntity.ok(ApiResponse.ok(stats));
    }
}

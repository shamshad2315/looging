package com.ulpf.controller;

import com.ulpf.dto.common.ApiResponse;
import com.ulpf.dto.common.PageResponse;
import com.ulpf.dto.event.EventResponse;
import com.ulpf.dto.event.EventSearchRequest;
import com.ulpf.model.NormalizedEvent;
import com.ulpf.repository.NormalizedEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@CrossOrigin(origins = "*")
public class EventController {

    private final MongoTemplate mongoTemplate;
    private final NormalizedEventRepository normalizedEventRepository;

    public EventController(MongoTemplate mongoTemplate, NormalizedEventRepository normalizedEventRepository) {
        this.mongoTemplate = mongoTemplate;
        this.normalizedEventRepository = normalizedEventRepository;
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<EventResponse>>> searchEvents(@RequestBody EventSearchRequest request) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (request.getVendor() != null && !request.getVendor().isBlank()) {
            criteriaList.add(Criteria.where("vendor").regex(request.getVendor(), "i"));
        }
        if (request.getSeverity() != null && !request.getSeverity().isBlank()) {
            criteriaList.add(Criteria.where("severity").regex(request.getSeverity(), "i"));
        }
        if (request.getLogType() != null && !request.getLogType().isBlank()) {
            criteriaList.add(Criteria.where("logType").regex(request.getLogType(), "i"));
        }
        if (request.getSourceIp() != null && !request.getSourceIp().isBlank()) {
            criteriaList.add(Criteria.where("sourceIp").is(request.getSourceIp()));
        }
        if (request.getDestinationIp() != null && !request.getDestinationIp().isBlank()) {
            criteriaList.add(Criteria.where("destinationIp").is(request.getDestinationIp()));
        }
        if (request.getAction() != null && !request.getAction().isBlank()) {
            criteriaList.add(Criteria.where("action").regex(request.getAction(), "i"));
        }
        if (request.getStartTime() != null || request.getEndTime() != null) {
            Criteria timeCriteria = Criteria.where("timestamp");
            if (request.getStartTime() != null) timeCriteria.gte(request.getStartTime());
            if (request.getEndTime() != null) timeCriteria.lte(request.getEndTime());
            criteriaList.add(timeCriteria);
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, NormalizedEvent.class);
        query.with(PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "timestamp")));

        List<NormalizedEvent> events = mongoTemplate.find(query, NormalizedEvent.class);
        List<EventResponse> responseList = events.stream().map(EventResponse::fromEntity).collect(Collectors.toList());

        PageResponse<EventResponse> pageResponse = PageResponse.of(responseList, request.getPage(), request.getSize(), total);
        return ResponseEntity.ok(ApiResponse.ok(pageResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable String id) {
        return normalizedEventRepository.findById(id)
                .map(e -> ResponseEntity.ok(ApiResponse.ok(EventResponse.fromEntity(e))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportEvents(
            @RequestParam(required = false) String vendor,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false, defaultValue = "json") String format
    ) {
        List<NormalizedEvent> events = normalizedEventRepository.findAll();
        StringBuilder sb = new StringBuilder();
        for (NormalizedEvent event : events) {
            sb.append(event.toString()).append("\n");
        }
        String filename = "events_export_" + Instant.now().getEpochSecond() + "." + format;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(sb.toString());
    }
}

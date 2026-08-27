package com.ulpf.normalization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ulpf.dto.parser.MappingRuleDto;
import com.ulpf.dto.parser.ParserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FieldMapper {

    private static final Logger log = LoggerFactory.getLogger(FieldMapper.class);

    private final Map<String, ParserInfoResponse> parserConfigs = new ConcurrentHashMap<>();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @PostConstruct
    public void loadYamlMappings() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:mappings/*.yml");
            for (Resource resource : resources) {
                try (InputStream is = resource.getInputStream()) {
                    Map<String, Object> mapData = yamlMapper.readValue(is, new TypeReference<>() {});
                    String vendor = (String) mapData.get("vendor");
                    String description = (String) mapData.get("description");

                    List<MappingRuleDto> rules = new ArrayList<>();
                    List<Map<String, String>> rawRules = (List<Map<String, String>>) mapData.get("fieldMappings");
                    if (rawRules != null) {
                        for (Map<String, String> r : rawRules) {
                            rules.add(new MappingRuleDto(r.get("sourceField"), r.get("targetField"), r.get("transform")));
                        }
                    }

                    Map<String, String> severityMap = (Map<String, String>) mapData.get("severityMapping");
                    if (severityMap == null) {
                        severityMap = new HashMap<>();
                    }

                    if (vendor != null) {
                        ParserInfoResponse info = new ParserInfoResponse(vendor, description, rules, severityMap);
                        parserConfigs.put(vendor.toUpperCase(), info);
                        log.info("Loaded mapping rules for vendor: {}", vendor);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to load YAML field mappings: ", e);
        }
    }

    public Map<String, Object> parseRawToKeyValue(String rawMessage, String vendor) {
        Map<String, Object> kvMap = new HashMap<>();
        if (rawMessage == null || rawMessage.isBlank()) {
            return kvMap;
        }

        String trimmed = rawMessage.trim();

        // 1. JSON parsing
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            try {
                return jsonMapper.readValue(trimmed, new TypeReference<Map<String, Object>>() {});
            } catch (Exception ignored) {
            }
        }

        // 2. Key-Value pairs parsing (e.g. key1=val1 key2="val 2" srcip=1.1.1.1)
        Pattern kvPattern = Pattern.compile("([a-zA-Z0-9_.-]+)=([\"']?[^\"'\\s]+[\"']?|[^\\s]+)");
        Matcher matcher = kvPattern.matcher(rawMessage);
        while (matcher.find()) {
            String key = matcher.group(1).toLowerCase();
            String val = matcher.group(2).replaceAll("^[\"']|[\"']$", "");
            kvMap.put(key, val);
        }

        // 3. Fallback extraction for IP and Port regex if kv empty
        if (kvMap.isEmpty()) {
            extractIpAndPortsRegex(rawMessage, kvMap);
        }

        return kvMap;
    }

    private void extractIpAndPortsRegex(String text, Map<String, Object> kvMap) {
        Pattern ipPattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
        Matcher matcher = ipPattern.matcher(text);
        List<String> ips = new ArrayList<>();
        while (matcher.find()) {
            ips.add(matcher.group());
        }
        if (!ips.isEmpty()) {
            kvMap.put("src_ip", ips.get(0));
            if (ips.size() > 1) {
                kvMap.put("dst_ip", ips.get(1));
            }
        }
    }

    public ParserInfoResponse getParserInfo(String vendor) {
        if (vendor == null) return parserConfigs.get("GENERIC");
        return parserConfigs.getOrDefault(vendor.toUpperCase(), parserConfigs.get("GENERIC"));
    }

    public Map<String, ParserInfoResponse> getAllParsers() {
        return Collections.unmodifiableMap(parserConfigs);
    }
}

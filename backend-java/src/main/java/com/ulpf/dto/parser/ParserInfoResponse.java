package com.ulpf.dto.parser;

import java.util.List;
import java.util.Map;

public class ParserInfoResponse {
    private String vendor;
    private String description;
    private List<MappingRuleDto> fieldMappings;
    private Map<String, String> severityMapping;

    public ParserInfoResponse() {
    }

    public ParserInfoResponse(String vendor, String description, List<MappingRuleDto> fieldMappings, Map<String, String> severityMapping) {
        this.vendor = vendor;
        this.description = description;
        this.fieldMappings = fieldMappings;
        this.severityMapping = severityMapping;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MappingRuleDto> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(List<MappingRuleDto> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public Map<String, String> getSeverityMapping() {
        return severityMapping;
    }

    public void setSeverityMapping(Map<String, String> severityMapping) {
        this.severityMapping = severityMapping;
    }
}

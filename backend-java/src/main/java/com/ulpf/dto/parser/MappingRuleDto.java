package com.ulpf.dto.parser;

public class MappingRuleDto {
    private String sourceField;
    private String targetField;
    private String transform;

    public MappingRuleDto() {
    }

    public MappingRuleDto(String sourceField, String targetField, String transform) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.transform = transform;
    }

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }
}

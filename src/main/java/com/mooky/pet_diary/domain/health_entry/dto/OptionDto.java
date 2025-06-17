package com.mooky.pet_diary.domain.health_entry.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "value", "display", "sortOrder" })
public interface OptionDto {
    Integer getId();

    String getValue();

    String getDisplay();

    Integer getSortOrder();
}
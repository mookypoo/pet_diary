package com.mooky.pet_diary.domain.health_entry.dto;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class ActivityRequest {
    private final Integer activityTypeId;
    private final Integer duration;
    private final LocalTime startAt;
    private final String notes;
}

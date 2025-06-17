package com.mooky.pet_diary.domain.health_entry.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mooky.pet_diary.domain.health_entry.entity.HealthEntryActivity;

import lombok.Getter;

@Getter
public class ActivityResponse {
    private final Long id;
    private final Integer activityTypeId;
    private final String value;
    private final String display;
    private final Integer duration;

    @JsonFormat(pattern = "hh:mm")
    private final LocalTime startAt;
    private final String notes;

    public ActivityResponse(HealthEntryActivity a) {
        this.id = a.getId();
        this.activityTypeId = a.getActivityType().getId();
        this.value = a.getActivityType().getValue();
        this.display = a.getActivityType().getDisplay();
        this.duration = a.getDuration();
        this.startAt = a.getStartAt();
        this.notes = a.getNotes();
    }
}

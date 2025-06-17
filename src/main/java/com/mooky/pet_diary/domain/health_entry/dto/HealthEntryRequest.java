package com.mooky.pet_diary.domain.health_entry.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class HealthEntryRequest {

    @NotNull(message = "need petId")
    @Positive(message = "petId must be positive")
    private final Long petId;

    @NotNull(message = "need entry date")
    private final LocalDate entryDate;
    private final Float weight;
    
    private final String weightUnit;

    @NotNull(message = "default energy level: 'normal'")
    @Positive(message = "energyId must be positive")
    private final Integer energyLevelId;
    private final String notes;
    private final List<ActivityRequest> activities;
}

package com.mooky.pet_diary.domain.health_entry.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mooky.pet_diary.domain.health_entry.entity.HealthEntry;

import io.jsonwebtoken.lang.Collections;
import lombok.Getter;

@Getter
public class HealthEntryResponse {

    private final Long id;
    private final Long petId;
    private final String petName;
    private final LocalDate entryDate;
    private final Float weight;
    private final String weightUnit;
    private final EnergyLevel energyLevel;
    private final List<ActivityResponse> activities;
    private final String notes;

    public record EnergyLevel(Integer id, String value, String display) {
    }
    

    public HealthEntryResponse(HealthEntry entry) {
        EnergyLevel energyLevel = new EnergyLevel(
                entry.getEnergyLevel().getId(),
                entry.getEnergyLevel().getValue(),
                entry.getEnergyLevel().getDisplay());

        List<ActivityResponse> activities = Optional.ofNullable(entry.getActivities())
            .orElse(Collections.emptyList())
            .stream().map(ActivityResponse::new).toList();

        this.id = entry.getId();
        this.petId = entry.getPet().getId();
        this.petName = entry.getPet().getName();
        this.entryDate = entry.getEntryDate();
        this.weight = entry.getWeight();
        this.weightUnit = entry.getWeightUnit();
        this.energyLevel = energyLevel;
        this.notes = entry.getNotes();
        this.activities = activities;
    }

}

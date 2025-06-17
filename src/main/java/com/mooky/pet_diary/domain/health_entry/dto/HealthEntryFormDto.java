package com.mooky.pet_diary.domain.health_entry.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HealthEntryFormDto {

    private final List<OptionDto> energyLevels;
    private final List<OptionDto> activityTypes;
    
}

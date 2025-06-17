package com.mooky.pet_diary.domain.health_entry.dto;

public interface PetHealthEntryForm {
    Long getPetId();

    String getPetName();
    
    Float getWeight();

    String getWeightUnit();
}
package com.mooky.pet_diary.domain.user.dto;

public interface UserWithPetSummaryProjection {
    Long getUserId();

    String getUsername();

    String getEmail();

    Long getPetId();

    String getPetName();

    String getPetProfilePhoto();

}
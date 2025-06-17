package com.mooky.pet_diary.domain.health_entry.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@SuppressWarnings("unused")
@Getter
public class ActivityType {
    
    @Id private Integer id;
    private String value;
    private String display;
    private Integer sortOrder;
    private Boolean isActive;

}

package com.mooky.pet_diary.domain.health_entry.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table
@NoArgsConstructor
@SuppressWarnings("unused")
public class EnergyLevel {

    @Id private Integer id;
    private String value;
    private String display;
    private Integer sortOrder;
    private Boolean isActive;

}

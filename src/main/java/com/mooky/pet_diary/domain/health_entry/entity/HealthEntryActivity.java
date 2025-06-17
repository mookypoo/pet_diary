package com.mooky.pet_diary.domain.health_entry.entity;

import java.time.LocalTime;

import com.mooky.pet_diary.domain.health_entry.dto.ActivityRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthEntryActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "health_entry_id")
    private HealthEntry healthEntry;

    @ManyToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    private Integer duration;

    private LocalTime startAt;

    private String notes;

    public static HealthEntryActivity newActivity(ActivityRequest dto, HealthEntry entry, ActivityType activityTypeRef) {
        return HealthEntryActivity.builder()
                .healthEntry(entry)
                .activityType(activityTypeRef)
                .duration(dto.getDuration())
                .startAt(dto.getStartAt())
                .notes(dto.getNotes())
                .build();
    }
}

/*
 * In the above code I gave you regarding health entry and HealthEntryActivity,
 * I used JPA's @ManyToOne
 */
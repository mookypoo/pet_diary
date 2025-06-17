package com.mooky.pet_diary.domain.health_entry.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mooky.pet_diary.domain.health_entry.dto.HealthEntryRequest;
import com.mooky.pet_diary.domain.pet.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
public class HealthEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private Long petId;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private Long userId;

    private LocalDate entryDate;

    private Float weight;

    private String weightUnit;

    //private Integer energyLevelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "energy_level_id")
    private EnergyLevel energyLevel;

    private String notes;

    // TODO learnnn
    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "healthEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HealthEntryActivity> activities = new ArrayList<>();

    /**
     * returns List.copyOf --> unmodifiable --> use addActivity method
     * will throw UnsupportedOperationException if modifying the list through the getter
     */
    public List<HealthEntryActivity> getActivities() {
        return List.copyOf(this.activities);
    }

    public static HealthEntry newEntry(HealthEntryRequest req, Long userId, Pet petRef, EnergyLevel energyLevelRef) {
        return HealthEntry.builder()
                .userId(userId)
                .pet(petRef)
                .entryDate(req.getEntryDate())
                .weight(req.getWeight())
                .weightUnit(req.getWeightUnit())
                .energyLevel(energyLevelRef)
                .build();
    }

    public void addActivity(HealthEntryActivity activity) {
        
        this.activities.add(activity);
    }

}

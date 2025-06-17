package com.mooky.pet_diary.domain.pet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mooky.pet_diary.domain.health_entry.entity.HealthEntry;
import com.mooky.pet_diary.domain.pet.dto.PetDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO does getting Pet info get HealthEntries as well?
@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @Column(name = "pet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Setter Long id;

    private Long ownerId;

    private String name;

    private String species;

    private String breed;

    private LocalDate birthDate;

    private LocalDate adoptionDate;

    private String description;

    private String profilePhoto;

    private Float weight;

    private String weightUnit;

    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthEntry> healthEntries;

    public static Pet fromPetDto(PetDto petDto, Long ownerId) {
        return new PetBuilder()
                .ownerId(ownerId)
                .name(petDto.getName())
                .species(petDto.getSpecies())
                .breed(petDto.getBreed())
                .birthDate(petDto.getBirthDate())
                .adoptionDate(petDto.getAdoptionDate())
                .description(petDto.getDescription())
                .profilePhoto(petDto.getProfilePhoto())
                .build();
    }
   
}
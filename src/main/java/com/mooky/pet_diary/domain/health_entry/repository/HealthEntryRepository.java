package com.mooky.pet_diary.domain.health_entry.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mooky.pet_diary.domain.health_entry.dto.OptionDto;
import com.mooky.pet_diary.domain.health_entry.dto.PetHealthEntryForm;
import com.mooky.pet_diary.domain.health_entry.entity.HealthEntry;

@Repository
public interface HealthEntryRepository extends JpaRepository<HealthEntry, Long> {

    @Query("""
            SELECT el.id as id, el.value as value, el.display as display, el.sortOrder as sortOrder
            FROM EnergyLevel el
            WHERE el.isActive = true
            ORDER BY el.sortOrder
            """)
    List<OptionDto> findActiveEnergyLevels();

    @Query("""
            SELECT at.id as id, at.value as value, at.display as display, at.sortOrder as sortOrder
            FROM ActivityType at
            WHERE at.isActive = true
            ORDER BY at.sortOrder
            """)
    List<OptionDto> findActiveActivityTypes();

    @Query("""
            SELECT p.id as petId, p.name as petName, p.weight as weight, p.weightUnit as weightUnit
            FROM Pet p
            WHERE p.id=:petId AND p.ownerId=:ownerId
            """)
    Optional<PetHealthEntryForm> findPetHealthFormData(@Param("petId") Long petId, @Param("ownerId") Long userId);
    
    @Query("""
            SELECT he FROM HealthEntry he
            JOIN FETCH he.pet
            JOIN FETCH he.energyLevel
            LEFT JOIN FETCH he.activities a
            LEFT JOIN FETCH a.activityType
            WHERE he.id = :entryId AND he.userId = :userId
            """)
    Optional<HealthEntry> findHealthEntryById(@Param("userId") Long userId, @Param("entryId") Long entryId);
}

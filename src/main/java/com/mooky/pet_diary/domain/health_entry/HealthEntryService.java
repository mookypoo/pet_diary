package com.mooky.pet_diary.domain.health_entry;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mooky.pet_diary.domain.health_entry.dto.ActivityRequest;
import com.mooky.pet_diary.domain.health_entry.dto.HealthEntryFormDto;
import com.mooky.pet_diary.domain.health_entry.dto.HealthEntryRequest;
import com.mooky.pet_diary.domain.health_entry.dto.HealthEntryResponse;
import com.mooky.pet_diary.domain.health_entry.dto.OptionDto;
import com.mooky.pet_diary.domain.health_entry.dto.PetHealthEntryForm;
import com.mooky.pet_diary.domain.health_entry.entity.ActivityType;
import com.mooky.pet_diary.domain.health_entry.entity.EnergyLevel;
import com.mooky.pet_diary.domain.health_entry.entity.HealthEntry;
import com.mooky.pet_diary.domain.health_entry.entity.HealthEntryActivity;
import com.mooky.pet_diary.domain.health_entry.repository.HealthEntryRepository;
import com.mooky.pet_diary.domain.pet.Pet;
import com.mooky.pet_diary.global.exception.NotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HealthEntryService {

    @Autowired
    private EntityManager entityManager;
    private final HealthEntryRepository heRepository;
    
    public HealthEntryFormDto getHealthEntryForm() {
        List<OptionDto> energyLevels = this.heRepository.findActiveEnergyLevels();
        List<OptionDto> activityTypes = this.heRepository.findActiveActivityTypes();
        return new HealthEntryFormDto(energyLevels, activityTypes);
    }

    public PetHealthEntryForm getPetHealthEntryFormData(Long petId, Long userId) {
        PetHealthEntryForm data = this.heRepository.findPetHealthFormData(petId, userId).orElseThrow(
                () -> NotFoundException.matchingPetAndOwner(petId, userId));
        return data;
    }
    
    public HealthEntryResponse createHealthEntry(HealthEntryRequest req, Long userId) {
        Pet petRef = this.getEntityReference(Pet.class, req.getPetId(), "pet");
        EnergyLevel energyLevelRef = this.getEntityReference(EnergyLevel.class, req.getEnergyLevelId().longValue(), "energyLevel");

        HealthEntry entry = HealthEntry.newEntry(req, userId, petRef, energyLevelRef);
        
        if (req.getActivities() != null && !req.getActivities().isEmpty()) {
            for (ActivityRequest activityReq : req.getActivities()) {
                ActivityType activityTypeRef = this.getEntityReference(ActivityType.class, activityReq.getActivityTypeId().longValue(), "activityType");
                HealthEntryActivity activity = HealthEntryActivity.newActivity(activityReq, entry, activityTypeRef);
                entry.addActivity(activity);
            }
        }
        
        HealthEntry savedEntry = this.heRepository.save(entry);
        // if I use the above savedEntry to create a response, hibernate will execute select
        // when I access the relevant entities (Pet, EnergyLevel, ActivityType)
        return new HealthEntryResponse(savedEntry);
    }
    
    private <T> T getEntityReference(Class<T> entityClass, Long id, String entityName) {
        try {
            return this.entityManager.getReference(entityClass, id);
        } catch (EntityNotFoundException e) {
            throw NotFoundException.resource(entityName, "no " + entityName + " with id=" + id, id.toString());
        }
    }

    public void getHealthEntryForPetByDate(Long petId, Long userId, LocalDate date) {
        //this.heRepository.findById(userId)
    }

    public HealthEntryResponse getHealthEntryById(Long userId, Long entryId) {
        HealthEntry entry = this.heRepository.findHealthEntryById(userId, entryId).orElseThrow(
                () -> NotFoundException.healthEntry("matching health entry not found for user",
                        "entryId=" + entryId + " userId=" + userId));
        return new HealthEntryResponse(entry);
    }
    
}

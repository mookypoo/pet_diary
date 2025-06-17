package com.mooky.pet_diary.domain.health_entry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mooky.pet_diary.domain.health_entry.dto.HealthEntryRequest;
import com.mooky.pet_diary.global.ApiResponse;
import com.mooky.pet_diary.global.security.CurrentUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/*
 * (1) user presses the create new health entry button
 * (2) prompted to choose from owned pets 
 * (client should already have a list of owned pets from UserProfile)
 * (2) in the new entry page, there should be following things:
 * - today's date shown as default
 * - weight of the pet 
 * - energy level selection ("normal" as default)
 * - activity type selection --> once selected, can add duration and notes 
 * (3) 
 */
@RestController
@RequestMapping("${mooky.endpoint}/v1/entry/health")
@RequiredArgsConstructor
public class HealthEntryController {

    private final HealthEntryService healthEntryService;

    /**
     * general health entry form TODO cache
     * @return HealthEntryForm
     */
    @GetMapping("/form")
    public ApiResponse getHealthEntryForm() {
        return ApiResponse.ok(this.healthEntryService.getHealthEntryForm());
    }

    /**
     * health entry general data for each pet
     * @return PetHealthEntryForm
     */
    // TODO change url 
    @GetMapping("/{petId}/form")
    public ApiResponse getPetHealthEntryFormData(@PathVariable("petId") Long petId, @CurrentUser Long userId) {
        return ApiResponse.ok(this.healthEntryService.getPetHealthEntryFormData(petId, userId));
    }

    @PostMapping
    public ApiResponse createHealthEntry(@Valid @RequestBody HealthEntryRequest req, @CurrentUser Long userId) {
        return ApiResponse.ok(this.healthEntryService.createHealthEntry(req, userId));
    }

    // @GetMapping("/{petId}")
    // public ApiResponse getHealthEntryForPetByDate(
    //         @PathVariable("petId") Long petId,
    //         @CurrentUser Long userId,
    //         @RequestParam("date") LocalDate date) {
    //     return ApiResponse.ok(this.healthEntryService.getHealthEntryForPetByDate(petId, userId, date));
    // }

    @GetMapping("/query/{entryId}")
    public ApiResponse getHealthEntryById(@PathVariable("entryId") Long entryId, @CurrentUser Long userId) {
        return ApiResponse.ok(this.healthEntryService.getHealthEntryById(userId, entryId));
    }

}

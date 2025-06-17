package com.mooky.pet_diary.domain.pet;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mooky.pet_diary.domain.pet.dto.PetDto;
import com.mooky.pet_diary.global.ApiResponse;
import com.mooky.pet_diary.global.security.CurrentUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${mooky.endpoint}/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    
    @PostMapping
    public ApiResponse createPet(@Valid @RequestBody PetDto req, @CurrentUser Long currentUser) {
        PetDto pet = this.petService.createPet(req, currentUser);
        return ApiResponse.ok(pet);
    }

    @GetMapping("/{petId}")
    public ApiResponse getPetDetails(@PathVariable("petId") Long petId, @CurrentUser Long currentUser) {
        PetDto pet = this.petService.getPetDetails(petId, currentUser);
        return ApiResponse.ok(pet);
    }

    @PutMapping("/{petId}")
    public ApiResponse updatePet(@PathVariable("petId") Long petId, @Validated(UpdatePet.class) @RequestBody PetDto petDto, @CurrentUser Long currentUser) {
        PetDto pet = this.petService.updatePet(petId, petDto, currentUser);
        return ApiResponse.ok(pet);
    }
}

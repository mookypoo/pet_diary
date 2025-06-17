package com.mooky.pet_diary.domain.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mooky.pet_diary.domain.pet.dto.PetDto;

public interface PetRepository extends JpaRepository<Pet, Long> {
    
    @Modifying
    @Query("UPDATE Pet p SET p.name = :#{#petDto.name}, p.species = :#{#petDto.species}, p.breed = :#{#petDto.breed}, " +
        " p.birthDate = :#{#petDto.birthDate}, p.adoptionDate = :#{#petDto.adoptionDate}, p.description = :#{#petDto.description}, " + 
        "p.profilePhoto = :#{#petDto.profilePhoto}, p.modifiedAt = CURRENT_TIMESTAMP WHERE p.id = :petId AND p.ownerId = :ownerId")
    int updatePet(@Param("petId") Long petId, @Param("ownerId") Long ownerId, @Param("petDto") PetDto petDto);
    
}

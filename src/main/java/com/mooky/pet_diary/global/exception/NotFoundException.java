package com.mooky.pet_diary.global.exception;

/**
 * style: [resource]_not_found
 * <p> 
 * eg: pet_not_found
 */
public class NotFoundException extends ApiException {

    public static NotFoundException resource(String resource, String errorMessage, String errorValue) {
        String error = resource + "_not_found";
        return new NotFoundException(error, errorMessage, errorValue, null);
    }

    public static NotFoundException healthEntry(String errorMessage, String errorValue) {
        return new NotFoundException("healthEntry_not_found", errorMessage, errorValue, null);
    }

    public static NotFoundException matchingPetAndOwner(Long petId, Long userId) {
        return new NotFoundException(
            "pet_not_found", 
            "either no pet found or not its owner", 
            "petId=" + petId + " userId=" + userId,
            null);
    }

    private NotFoundException(String error, String errorMessage, String errorValue, String errorTitle) {
        super(error, errorMessage, "COM_003", errorValue, errorTitle, 404);
    }

}

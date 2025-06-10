package com.lksnext.parkingplantilla.utils;

public class InputValidator {

    public static ValidationResult validateNotEmpty(String fieldValue) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            return ValidationResult.error("Por favor, rellena todos los campos.");
        }
        return ValidationResult.ok();
    }
}

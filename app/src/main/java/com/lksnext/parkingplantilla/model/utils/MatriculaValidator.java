package com.lksnext.parkingplantilla.model.utils;

public class MatriculaValidator {

    // Patrón: 4 dígitos seguidos de 3 consonantes (sin vocales ni Ñ)
    private static final String MATRICULA_REGEX = "^[0-9]{4}[BCDFGHJKLMNPQRSTVWXYZ]{3}$";

    public static ValidationResult validate(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            return ValidationResult.error("La matrícula no puede estar vacía.");
        }

        if (!matricula.matches(MATRICULA_REGEX)) {
            return ValidationResult.error("Formato de matrícula inválido. Ejemplo válido: 1234BCD");
        }

        return ValidationResult.ok();
    }
}
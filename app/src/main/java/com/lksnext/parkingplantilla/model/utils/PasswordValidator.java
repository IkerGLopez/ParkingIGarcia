package com.lksnext.parkingplantilla.model.utils;

public class PasswordValidator {
    public static ValidationResult passwordValidator(String psw, String psw2) {
        if (!psw.equals(psw2)) {
            return ValidationResult.error("Las contraseñas no coinciden.");
        }
        return ValidationResult.ok();
    }
}

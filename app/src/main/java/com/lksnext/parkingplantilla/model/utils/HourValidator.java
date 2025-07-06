package com.lksnext.parkingplantilla.model.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.ZoneId;

public class HourValidator {
    private static final LocalTime HORA_MIN = LocalTime.of(9, 0);
    private static final LocalTime HORA_MAX = LocalTime.of(22, 0);
    private static final int MAX_HORAS = 8;

    public static LocalTime getMinStartTime(ZoneId zoneId) {
        LocalTime now = LocalTime.now(zoneId).truncatedTo(ChronoUnit.MINUTES);
        int minute = now.getMinute();

        if (minute == 0) {
            return now;
        } else if (minute <= 30) {
            return now.withMinute(30);
        } else {
            return now.plusHours(1).withMinute(0);
        }
    }

    // Solo permite bajar la hora de inicio si no se sale del rango permitido
    public static boolean canDecreaseStart(LocalTime selectedStartTime, LocalDate selectedDate, ZoneId zoneId) {
        LocalTime minStartTime = HORA_MIN;
        if (selectedDate != null && selectedDate.equals(LocalDate.now(zoneId))) {
            minStartTime = getMinStartTime(zoneId).isAfter(HORA_MIN) ? getMinStartTime(zoneId) : HORA_MIN;
        }
        return selectedStartTime.isAfter(minStartTime);
    }

    // Solo permite subir la hora de fin si no se sale del rango permitido
    public static boolean canIncreaseEnd(LocalTime selectedEndTime) {
        return selectedEndTime.isBefore(HORA_MAX);
    }

    public static boolean canDecreaseEnd(LocalTime selectedStartTime, LocalTime selectedEndTime) {
        return selectedEndTime.isAfter(selectedStartTime.plusMinutes(30));
    }

    public static boolean isValidRange(LocalTime start, LocalTime end, LocalDate selectedDate, ZoneId zoneId) {
        if (start == null || end == null) return false;
        LocalTime minStart = HORA_MIN;
        if (selectedDate != null && selectedDate.equals(LocalDate.now(zoneId))) {
            minStart = getMinStartTime(zoneId).isAfter(HORA_MIN) ? getMinStartTime(zoneId) : HORA_MIN;
        }
        if (start.isBefore(minStart) || end.isAfter(HORA_MAX)) return false;
        if (!end.isAfter(start)) return false;
        return java.time.Duration.between(start, end).toHours() <= MAX_HORAS;
    }

    public static boolean canIncreaseEnd(LocalTime selectedStartTime, LocalTime selectedEndTime) {
        if (selectedStartTime == null || selectedEndTime == null) return false;
        // No puede superar las 8 horas ni pasar de las 22:00
        LocalTime nextEnd = selectedEndTime.plusMinutes(30);
        long horas = java.time.Duration.between(selectedStartTime, nextEnd).toHours();
        return !nextEnd.isAfter(HORA_MAX) && horas <= MAX_HORAS;
    }
}
package com.lksnext.parkingplantilla.model.utils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.ZoneId;

public class HourValidator {

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

    public static boolean canDecreaseStart(LocalTime selectedStartTime, ZoneId zoneId) {
        LocalTime minStartTime = getMinStartTime(zoneId);
        return selectedStartTime.isAfter(minStartTime);
    }

    public static boolean canDecreaseEnd(LocalTime selectedStartTime, LocalTime selectedEndTime) {
        return selectedEndTime.isAfter(selectedStartTime.plusMinutes(30));
    }
}
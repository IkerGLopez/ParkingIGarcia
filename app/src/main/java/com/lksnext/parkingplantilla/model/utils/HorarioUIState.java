package com.lksnext.parkingplantilla.model.utils;

import java.time.LocalTime;

public class HorarioUIState {
    public final LocalTime startTime;
    public final LocalTime endTime;
    public final boolean canDecreaseStart;
    public final boolean canDecreaseEnd;
    public final boolean canIncreaseEnd;
    public final boolean isValid;

    public HorarioUIState(LocalTime startTime, LocalTime endTime, boolean canDecreaseStart, boolean canDecreaseEnd, boolean canIncreaseEnd, boolean isValid) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.canDecreaseStart = canDecreaseStart;
        this.canDecreaseEnd = canDecreaseEnd;
        this.canIncreaseEnd = canIncreaseEnd;
        this.isValid = isValid;
    }
}

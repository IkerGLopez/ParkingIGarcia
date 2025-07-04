package com.lksnext.parkingplantilla.model.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.lksnext.parkingplantilla.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class DisabledPastDaysDecorator implements DayViewDecorator {

    private final CalendarDay today = CalendarDay.today();
    private final int color;

    public DisabledPastDaysDecorator(Context context) {
        // Obtener color desde recursos y guardarlo como int
        color = context.getResources().getColor(R.color.NotMoradito);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.isBefore(today);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(color));
        view.setDaysDisabled(true);
    }
}
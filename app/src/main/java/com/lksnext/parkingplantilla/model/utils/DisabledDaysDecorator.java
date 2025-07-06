package com.lksnext.parkingplantilla.model.utils;

import android.content.Context;
import android.text.style.ForegroundColorSpan;

import com.lksnext.parkingplantilla.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.time.LocalDate;

public class DisabledDaysDecorator implements DayViewDecorator {

    private final CalendarDay today;
    private final CalendarDay lastAvailableDay;
    private final int color;

    public DisabledDaysDecorator(Context context) {
        LocalDate now = LocalDate.now();
        today = CalendarDay.from(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());

        LocalDate lastDay = now.plusDays(6); // Hoy + 6 = 7 días totales
        lastAvailableDay = CalendarDay.from(lastDay.getYear(), lastDay.getMonthValue() - 1, lastDay.getDayOfMonth());

        color = context.getResources().getColor(R.color.NotMoradito);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.isBefore(today) || day.isAfter(lastAvailableDay);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(color));
        view.setDaysDisabled(true);
    }
}

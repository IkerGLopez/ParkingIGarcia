// MainViewModel.java
package com.lksnext.parkingplantilla.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.parkingplantilla.model.domain.Hora;
import com.lksnext.parkingplantilla.model.domain.Plaza;
import com.lksnext.parkingplantilla.model.domain.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MainViewModel extends ViewModel {

    // ================================== RESERVAR ==================================

    private final MutableLiveData<LocalTime> selectedStartTime = new MutableLiveData<>();
    private final MutableLiveData<LocalTime> selectedEndTime = new MutableLiveData<>();


    public LiveData<LocalTime> getSelectedStartTime() {
        return selectedStartTime;
    }

    public LiveData<LocalTime> getSelectedEndTime() {
        return selectedEndTime;
    }

    public void setupInitialTime(ZoneId zoneId) {
        LocalTime now = LocalTime.now(zoneId).truncatedTo(ChronoUnit.MINUTES);
        int minute = now.getMinute();
        LocalTime startTime;

        if (minute == 0) {
            startTime = now;
        } else if (minute <= 30) {
            startTime = now.withMinute(30);
        } else {
            startTime = now.plusHours(1).withMinute(0);
        }

        LocalTime endTime = startTime.plusMinutes(30);

        selectedStartTime.setValue(startTime);
        selectedEndTime.setValue(endTime);
    }
    // ================================== RESERVAR ==================================
    private final MutableLiveData<LocalDate> selectedDate =
            new MutableLiveData<>(LocalDate.now(ZoneId.of("Europe/Madrid")));
    private final MutableLiveData selectedPlaza =
            new MutableLiveData<>();
    private final MutableLiveData selectedHour = new MutableLiveData<>(null);
    private final MutableLiveData<LocalDate> newSelectedDate =
            new MutableLiveData<>(LocalDate.now(ZoneId.of("Europe/Madrid")));
    private final MutableLiveData reservaData = new MutableLiveData<>();

    // Nuevos campos
    private final MutableLiveData reservaSeleccionada = new MutableLiveData<>();
    private final MutableLiveData reservaDocIdSeleccionada = new MutableLiveData<>();

    public MutableLiveData getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(@NonNull LocalDate date) {
        selectedDate.setValue(date);
    }

    public void setSelectedDateDay(int selectedDateDay) {
        final LocalDate newLocalDate = Objects.requireNonNull(newSelectedDate.getValue())
                .withDayOfMonth(selectedDateDay);
        selectedDate.setValue(newLocalDate);
    }

    public MutableLiveData getSelectedHour() {
        return selectedHour;
    }

    public void setSelectedHour(Hora hora) {
        selectedHour.setValue(hora);
    }

    public MutableLiveData getReservaData() {
        return reservaData;
    }

    public void setSelectedPlaza(Plaza plaza) {
        selectedPlaza.setValue(plaza);
    }

    public LiveData getSelectedPlaza() {
        return selectedPlaza;
    }

    public void setReservaData(LocalDate date, LocalTime start, LocalTime end) {
        reservaData.setValue(new Reserva(date, start, end));
    }

    public MutableLiveData getReservaSeleccionada() {
        return reservaSeleccionada;
    }

    public void setReservaSeleccionada(Reserva reserva) {
        reservaSeleccionada.setValue(reserva);
    }

    public MutableLiveData getReservaDocIdSeleccionada() {
        return reservaDocIdSeleccionada;
    }

    public void setReservaDocIdSeleccionada(String docId) {
        reservaDocIdSeleccionada.setValue(docId);
    }
}




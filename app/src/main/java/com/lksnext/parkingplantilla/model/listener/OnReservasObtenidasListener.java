package com.lksnext.parkingplantilla.model.listener;

import com.lksnext.parkingplantilla.model.domain.Reserva;

import java.util.Map;

public interface OnReservasObtenidasListener {
    void onComplete(Map<String, Reserva> reservas);
}

package com.lksnext.parkingplantilla.model.listener;

import com.lksnext.parkingplantilla.model.domain.Reserva;

public interface ReservaListener {
    void onCancelar(Reserva reserva, String docId);
}


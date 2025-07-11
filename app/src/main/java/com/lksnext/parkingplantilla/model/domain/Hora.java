package com.lksnext.parkingplantilla.model.domain;

public class Hora {

    private long horaInicio;
    private long horaFin;

    public Hora() {

    }

    public Hora(long horaInicio, long horaFin) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public long getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(long horaInicio) {
        this.horaInicio = horaInicio;
    }

    public long getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(long horaFin) {
        this.horaFin = horaFin;
    }
}

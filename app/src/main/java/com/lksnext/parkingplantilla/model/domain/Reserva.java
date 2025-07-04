package com.lksnext.parkingplantilla.model.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Reserva {
    private String fecha;        // formato ISO: "2025-07-01"
    private String horaInicio;   // formato: "17:00"
    private String horaFin;      // formato: "18:00"
    private Plaza plaza;         // debe ser serializable

    public Reserva() {}

    public Reserva(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.fecha = fecha.toString();             // "2025-07-01"
        this.horaInicio = horaInicio.toString();   // "17:00"
        this.horaFin = horaFin.toString();         // "18:00"
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Plaza getPlaza() {
        return plaza;
    }

    public void setPlaza(Plaza plaza) {
        this.plaza = plaza;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "fecha='" + fecha + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                ", horaFin='" + horaFin + '\'' +
                ", plaza=" + plaza +
                '}';
    }

    public String getPlazaId() {
        return plaza.getId();
    }
}

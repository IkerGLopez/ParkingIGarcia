package com.lksnext.parkingplantilla.domain;

import java.util.Date;

public class Reserva {
    public String id;
    public Usuario usuario;
    public Date fecha;
    public Hora horaInicio;
    public Hora horaFin;
    public Plaza plaza;
    public Parking parking;

    public Reserva() {

    }

    public Reserva(String id, Usuario u, Date f, Hora hi, Hora hf, Plaza pz, Parking pk) {
        this.id = id;
        this.usuario = u;
        this.fecha = f;
        this.horaInicio = hi;
        this.horaFin = hf;
        this.plaza = pz;
        this.parking = pk;
    }

    public Reserva(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Hora getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Hora horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Hora getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Hora horaFin) {
        this.horaFin = horaFin;
    }

    public Plaza getPlaza() {
        return plaza;
    }

    public void setPlaza(Plaza plaza) {
        this.plaza = plaza;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
}

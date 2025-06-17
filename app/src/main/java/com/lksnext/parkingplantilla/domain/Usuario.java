package com.lksnext.parkingplantilla.domain;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombre;
    private String DNI;
    private String email;
    private String contrasena;
    private Vehiculo vehiculo;
    private List<Reserva> reservas = new ArrayList<>();
    //QR

    public Usuario(){

    }

    public Usuario(String n, String DNI, String e, String c, Vehiculo v, List<Reserva> r) {
        this.nombre = n;
        this.DNI = DNI;
        this.email = e;
        this.contrasena = c;
        this.vehiculo = v;
        this.reservas = r;
    }

    public Usuario(String DNI) {
        this.DNI = DNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}
package com.lksnext.parkingplantilla.domain;
public class Vehiculo {
    public String marca;
    public String modelo;
    public String matricula;
    public boolean electrico;
    public boolean discapacidad;

    public Vehiculo() {

    }

    public Vehiculo(String mc, String mo, String mt, boolean e, boolean d) {
        this.marca = mc;
        this.modelo = mo;
        this.matricula = mt;
        this.electrico = e;
        this.discapacidad = d;
    }

    public Vehiculo(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public boolean isElectrico() {
        return electrico;
    }

    public void setElectrico(boolean electrico) {
        this.electrico = electrico;
    }

    public boolean isDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(boolean discapacidad) {
        this.discapacidad = discapacidad;
    }
}
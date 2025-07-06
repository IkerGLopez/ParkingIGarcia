package com.lksnext.parkingplantilla.model.domain;
public class Plaza {
    public enum Tipo { NORMAL, MOTO, ELECTRICO, MINUSVALIDO }
    public enum Estado { LIBRE, OCUPADA, SELECCIONADA, INACCESIBLE }

    private String id;
    private Tipo tipo;
    private Estado estado;



    public Plaza(String id, Tipo tipo, Estado estado) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Plaza() {

    }

    public Tipo getTipo() {
        return tipo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }
}

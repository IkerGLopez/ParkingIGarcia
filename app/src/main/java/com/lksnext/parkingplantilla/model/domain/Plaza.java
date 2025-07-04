package com.lksnext.parkingplantilla.model.domain;
public class Plaza {
    public enum Tipo { NORMAL, MOTO, ELECTRICO, MINUSVALIDO }
    public enum Estado { LIBRE, OCUPADA, SELECCIONADA }

    private final String id;
    private final Tipo tipo;
    private Estado estado;

    public Plaza(String id, Tipo tipo, Estado estado) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
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

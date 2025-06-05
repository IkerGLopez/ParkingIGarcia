package com.lksnext.parkingplantilla.domain;
public class Plaza {

    public String id;
    public boolean electrico;
    public boolean discapacidad;

    public Plaza() {

    }

    public Plaza(String id, boolean e, boolean d) {
        this.id = id;
        this.electrico = e;
        this.discapacidad = d;
    }

    public Plaza(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
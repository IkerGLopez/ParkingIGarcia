package com.lksnext.parkingplantilla.domain;
import java.util.ArrayList;
import java.util.List;

public class Parking {
    private String id;
    private String nombre;
    private List<Plaza> plazas = new ArrayList<>();

    public Parking() {

    }

    public Parking(String id, String n, String d, List<Plaza> p) {
        this.id = id;
        this.nombre = n;
        this.plazas = p;
    }

    public Parking(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Plaza> getPlazas() {
        return plazas;
    }

    public void setPlazas(List<Plaza> plazas) {
        this.plazas = plazas;
    }
}

package com.example.trackem.Auxiliares;

public class Suceso {
    String descripción;
    long fecha;

    public Suceso(String descripción, long fecha) {
        this.descripción = descripción;
        this.fecha = fecha;
    }

    public Suceso() {
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }
}

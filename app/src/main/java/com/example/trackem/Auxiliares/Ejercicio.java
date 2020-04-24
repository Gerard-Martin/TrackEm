package com.example.trackem.Auxiliares;

public class Ejercicio {
    public String descripcion;
    public int tiempo;
    public float promedio;
    long fecha;

    public Ejercicio(String descripcion, int tiempo, float promedio, long fecha) {
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.promedio = promedio;
        this.fecha = fecha;
    }

    public Ejercicio() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public float getPromedio() {
        return promedio;
    }

    public void setPromedio(float promedio) {
        this.promedio = promedio;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }
}

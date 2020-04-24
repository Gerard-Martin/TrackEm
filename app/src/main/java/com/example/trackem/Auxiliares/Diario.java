package com.example.trackem.Auxiliares;

import java.io.Serializable;

public class Diario implements Serializable {
    public float concentracion, fatiga, memoria, animo, energia, sueño;
    public long time;

    public Diario() {
    }

    public Diario(float concentracion, float fatiga, float memoria, float animo, float energia, float sueño, long time) {
        this.concentracion = concentracion;
        this.fatiga = fatiga;
        this.memoria = memoria;
        this.animo = animo;
        this.energia = energia;
        this.sueño = sueño;
        this.time = time;
    }

    public float getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(float concentracion) {
        this.concentracion = concentracion;
    }

    public float getFatiga() {
        return fatiga;
    }

    public void setFatiga(float fatiga) {
        this.fatiga = fatiga;
    }

    public float getMemoria() {
        return memoria;
    }

    public void setMemoria(float memoria) {
        this.memoria = memoria;
    }

    public float getAnimo() {
        return animo;
    }

    public void setAnimo(float animo) {
        this.animo = animo;
    }

    public float getEnergia() {
        return energia;
    }

    public void setEnergia(float energia) {
        this.energia = energia;
    }

    public float getSueño() {
        return sueño;
    }

    public void setSueño(float sueño) {
        this.sueño = sueño;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

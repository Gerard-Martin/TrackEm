package com.example.trackem.Auxiliares;

import com.github.sundeepk.compactcalendarview.domain.Event;

public class EventPersonalizado extends Event {
    private String IDPaciente;
    private String IDDoctor = "";
    public EventPersonalizado(int color, long timeInMillis) {
        super(color, timeInMillis);
    }

    public EventPersonalizado(int color, long timeInMillis, Object data) {
        super(color, timeInMillis, data);
    }

    public EventPersonalizado(int color, long timeInMillis, String data, String IDDoctor, String IDPaciente) {
        super(color, timeInMillis, data);
        this.IDDoctor = IDDoctor;
        this.IDPaciente = IDPaciente;
    }

    public EventPersonalizado(int color, long timeInMillis, String data,  String IDPaciente) {
        super(color, timeInMillis, data);
        this.IDPaciente = IDPaciente;
    }

    public String getIDPaciente() {
        return IDPaciente;
    }

    public void setIDPaciente(String IDPaciente) {
        this.IDPaciente = IDPaciente;
    }

    public String getIDDoctor() {
        return IDDoctor;
    }

    public void setIDDoctor(String IDDoctor) {
        this.IDDoctor = IDDoctor;
    }

    public EventPersonalizado() {
        super (0, 0);
    }
}


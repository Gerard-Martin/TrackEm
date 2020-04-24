package com.example.trackem.Auxiliares;

public class Usuario {
    public String nombre, imagen, numero;
    private int tipo;

    public Usuario(String nombre, String imagen, String numero, int tipo) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.numero = numero;
        this.tipo = tipo;
    }

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}

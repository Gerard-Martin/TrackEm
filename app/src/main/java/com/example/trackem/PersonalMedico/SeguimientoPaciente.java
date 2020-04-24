package com.example.trackem.PersonalMedico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trackem.Compartidas.Historial;
import com.example.trackem.R;

import java.util.Objects;

public class SeguimientoPaciente extends AppCompatActivity {
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_seguimiento_paciente);
        id = getIntent().getStringExtra("id");
    }

    public void sucesoExtraordinario(View view) {
        startActivity(new Intent(SeguimientoPaciente.this, ListaSucesosExtraordinarios.class).putExtra("id", id));
    }

    public void verHistorial(View view) {
        startActivity(new Intent(SeguimientoPaciente.this, Historial.class).putExtra("id", id));
    }

    public void ejerciciosPaciente(View view) {
        startActivity(new Intent(SeguimientoPaciente.this, ListaEjercicios.class).putExtra("id", id));
    }
}

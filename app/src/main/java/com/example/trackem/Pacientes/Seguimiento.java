package com.example.trackem.Pacientes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trackem.Compartidas.Historial;
import com.example.trackem.R;

import java.util.Objects;

public class Seguimiento extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_seguimiento);
    }

    public void seguimientoDiario(View view) {
        startActivity(new Intent(Seguimiento.this, SeguimientoDiario.class));
    }

    public void sucesoExtraordinario(View view) {
        startActivity(new Intent(Seguimiento.this, SucesoExtraordinario.class));
    }

    public void verHistorial(View view) {
        startActivity(new Intent(Seguimiento.this, Historial.class));
    }
}

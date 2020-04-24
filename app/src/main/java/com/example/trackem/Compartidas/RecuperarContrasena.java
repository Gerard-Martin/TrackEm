package com.example.trackem.Compartidas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trackem.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RecuperarContrasena extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_recuperar_contrasena);
    }

    public void recuperar(View view) {
        EditText e = findViewById(R.id.rec_email);
        FirebaseAuth.getInstance().sendPasswordResetEmail(e.getText().toString());
        Toast.makeText(getApplicationContext(), "Se ha enviado el correo de recuperación! Revise su buzón.", Toast.LENGTH_LONG).show();
        finish();
    }
}

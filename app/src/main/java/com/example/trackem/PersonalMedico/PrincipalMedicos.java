package com.example.trackem.PersonalMedico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trackem.Compartidas.Calendario;
import com.example.trackem.Compartidas.Configuracion;
import com.example.trackem.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalMedicos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_principal_medicos);
        cargarInformación();
    }

    private void cargarInformación() {
        TextView tv = findViewById(R.id.bienvenida);
        CircleImageView cv = findViewById(R.id.imgProfilePicture);

        tv.setText(String.format("Bienvenido/a %s!", getIntent().getStringExtra("nombre")));
        Glide.with(this).load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl())
                .fallback(R.drawable.ic_fallback_user)
                .skipMemoryCache(true)
                .into(cv);
    }

    public void gestionarUsuarios(View view) {
        startActivity(new Intent(PrincipalMedicos.this, ListaPacientes.class));
    }

    public void calendario(View view) {
        Intent i = new Intent(PrincipalMedicos.this, Calendario.class);
        i.putExtra("doctor", true);
        startActivity(i);
    }

    public void configuracion(View view) {
        startActivity(new Intent(PrincipalMedicos.this, Configuracion.class));
    }

    public void seguimiento(View view) {
        startActivity(new Intent(PrincipalMedicos.this, ListaPacientes.class).putExtra("consulta",true));
    }
}

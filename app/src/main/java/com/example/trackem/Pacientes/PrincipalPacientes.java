package com.example.trackem.Pacientes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.trackem.Compartidas.Calendario;
import com.example.trackem.Compartidas.Configuracion;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalPacientes extends AppCompatActivity {
    String n = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_principal_pacientes);
        cargarInformación();
    }

    private void cargarInformación() {
        TextView tv = findViewById(R.id.bienvenida);
        CircleImageView cv = findViewById(R.id.imgProfilePicture);
        n = getIntent().getStringExtra("nombre");
        tv.setText(String.format("Bienvenido/a %s!",n));
        Glide.with(this).load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl())
            .fallback(R.drawable.ic_fallback_user)
            .skipMemoryCache(true)
            .into(cv);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("tok", instanceIdResult.getToken());
                FirebaseFirestore
                        .getInstance()
                        .collection("Tokens")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(hm);
                createNotificationChannel();

            }
        });

    }

    public void seguimiento(View view) {
        startActivity(new Intent(PrincipalPacientes.this, Seguimiento.class));
    }

    public void calendario(View view) {
        startActivity(new Intent(PrincipalPacientes.this, Calendario.class));
    }

    public void chat(View view) {
        startActivity(new Intent(PrincipalPacientes.this, Chat.class).putExtra(getResources().getString(R.string.nombre),n));
    }
    public void ejercicio(View view) {
        startActivity(new Intent(PrincipalPacientes.this, RegistroEjercicio.class));
    }

    public void configuracion(View view) {
        startActivity(new Intent(PrincipalPacientes.this, Configuracion.class));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.cnombre);
            String description = getString(R.string.cdesc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.cnombre), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

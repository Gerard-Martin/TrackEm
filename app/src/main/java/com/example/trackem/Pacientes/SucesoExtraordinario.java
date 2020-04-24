package com.example.trackem.Pacientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trackem.Auxiliares.Suceso;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SucesoExtraordinario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suceso_extraordinario);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    public void guardar(View view) {
        EditText e = findViewById(R.id.informacion);
        if (e.getText().toString().isEmpty()) {
            e.setError(getString(R.string.info));
            return;
        }
        Suceso s = new Suceso(e.getText().toString(), System.currentTimeMillis());

        FirebaseFirestore.getInstance().collection("Datos").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).
                collection("Sucesos").add(s).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SucesoExtraordinario.this, "Suceso guardado correctamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SucesoExtraordinario.this, "Ha ocurrido un error! Intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

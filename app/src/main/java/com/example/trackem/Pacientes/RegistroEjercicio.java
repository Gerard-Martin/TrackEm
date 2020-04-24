package com.example.trackem.Pacientes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trackem.Auxiliares.Ejercicio;
import com.example.trackem.Compartidas.SeleccionarDuracion;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.Objects;

public class RegistroEjercicio extends AppCompatActivity {

    private int duracion = 0;
    private SmileRating s1;
    private SmileRating s2;
    private SmileRating s3;
    private boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_ejercicios_paciente);
        s1 = findViewById(R.id.smile_animo);
        s2 = findViewById(R.id.smile_dificultad);
        s3 = findViewById(R.id.smile_fatiga);
        arreglarSmiles(s1);
        arreglarSmiles(s2);
        arreglarSmiles(s3);
    }

    private void arreglarSmiles(SmileRating r) {
        r.setNameForSmile(BaseRating.TERRIBLE, "Mal");
        r.setNameForSmile(BaseRating.BAD, "Regular");
        r.setNameForSmile(BaseRating.OKAY, "Normal");
        r.setNameForSmile(BaseRating.GOOD, "Bien");
        r.setNameForSmile(BaseRating.GREAT, "Genial");

    }

    public void guardarEjercicio(View view) {
        EditText e = findViewById(R.id.informacion);
        if(!selected){
            Toast.makeText(this, "Introduzca una duración", Toast.LENGTH_SHORT).show();
            return;
        }
        if(s1.getRating() == 0 || s2.getRating() == 0 || s3.getRating() == 0){
            Toast.makeText(this, "Introduzca en la escala como se ha sentido", Toast.LENGTH_SHORT).show();
            return;
        }
        float average = (float)(s1.getRating()+s2.getRating()+s3.getRating())/3;
        if (e.getText().toString().isEmpty()) {
            e.setError(getString(R.string.info));
            return;
        }
        Ejercicio ej = new Ejercicio(e.getText().toString(),duracion, average, System.currentTimeMillis());

        FirebaseFirestore.getInstance().collection("Datos").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Ejercicios").add(ej).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistroEjercicio.this, "Ejercicio guardado correctamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegistroEjercicio.this, "Ha ocurrido un error! Intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void seleccionarDuracion(View view) {
        startActivityForResult(new Intent(RegistroEjercicio.this, SeleccionarDuracion.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                selected = true;
                duracion = (int) data.getLongExtra("duracion", 0);
                TextView v = findViewById(R.id.dur);
                v.setText(String.format("%d minutos", duracion));
            }
        }
    }
}

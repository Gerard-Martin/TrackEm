package com.example.trackem.Pacientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackem.Auxiliares.Diario;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.fluidslider.FluidSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class SeguimientoDiario extends AppCompatActivity {
    private List<FluidSlider> ls;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_seguimiento_diario);
        db = FirebaseFirestore.getInstance();
        inicializar();
    }

    public void guardar(View view) {
        Diario d = new Diario(ls.get(0).getPosition()*100,ls.get(1).getPosition()*100, ls.get(2).getPosition()*100, ls.get(3).getPosition()*100, ls.get(4).getPosition()*100, ls.get(5).getPosition()*100, System.currentTimeMillis());
        db.collection("Datos").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).collection("Seguimiento").add(d).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SeguimientoDiario.this, "Se ha guardado correctamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SeguimientoDiario.this, "Ha ocurrido un error! Intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inicializar(){
        ls = new ArrayList<>();
        final List<TextView> lt = new ArrayList<>();
        ls.add((FluidSlider) findViewById(R.id.fs1));
        ls.add((FluidSlider) findViewById(R.id.fs2));
        ls.add((FluidSlider) findViewById(R.id.fs3));
        ls.add((FluidSlider) findViewById(R.id.fs4));
        ls.add((FluidSlider) findViewById(R.id.fs5));
        ls.add((FluidSlider) findViewById(R.id.fs6));
        lt.add((TextView) findViewById(R.id.tv1));
        lt.add((TextView) findViewById(R.id.tv2));
        lt.add((TextView) findViewById(R.id.tv3));
        lt.add((TextView) findViewById(R.id.tv4));
        lt.add((TextView) findViewById(R.id.tv5));
        lt.add((TextView) findViewById(R.id.tv6));

        for(int a = 0; a<ls.size(); a++){
            final int finalA = a;
            ls.get(a).setEndTrackingListener(new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    lt.get(finalA).setVisibility(View.VISIBLE);
                    return Unit.INSTANCE;
                }
            });
            ls.get(a).setBeginTrackingListener(new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    lt.get(finalA).setVisibility(View.INVISIBLE);
                    return Unit.INSTANCE;
                }
            });
        }
    }
}

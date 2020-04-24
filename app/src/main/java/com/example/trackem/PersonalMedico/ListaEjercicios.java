package com.example.trackem.PersonalMedico;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackem.Auxiliares.AdapterEjercicio;
import com.example.trackem.Auxiliares.Ejercicio;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaEjercicios extends AppCompatActivity {
    private AdapterEjercicio arrayAdapter;
    private final List<Ejercicio> list = new ArrayList<>();
    private RecyclerView lv;
    private TextView tv;
    private KProgressHUD k;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ejercicios);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        lv = findViewById(R.id.hej);
        tv = findViewById(R.id.nej);
        k = KProgressHUD.create(ListaEjercicios.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Cargando datos...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        id = getIntent().getStringExtra("id");
        obtenerEjercicios();
    }

    private void obtenerEjercicios() {
        FirebaseFirestore.getInstance().collection("Datos").document(id).collection("Ejercicios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                            for (DocumentSnapshot document : l) {
                                Ejercicio e = document.toObject(Ejercicio.class);
                                list.add(e);
                            }
                            if(list.isEmpty()){
                                lv.setVisibility(View.GONE);
                                tv.setVisibility(View.VISIBLE);
                                k.dismiss();
                            }else {
                                tv.setVisibility(View.GONE);
                                lv.setVisibility(View.VISIBLE);
                                lv.setHasFixedSize(true);
                                lv.setLayoutManager(new LinearLayoutManager(ListaEjercicios.this));
                                arrayAdapter = new AdapterEjercicio(list, getApplicationContext());
                                lv.setAdapter(arrayAdapter);
                                k.dismiss();
                            }
                        }
                    }
                });
    }
}

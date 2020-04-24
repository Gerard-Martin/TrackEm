package com.example.trackem.PersonalMedico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trackem.Auxiliares.AdapterSucesoExtraordinario;
import com.example.trackem.Auxiliares.Suceso;
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

public class ListaSucesosExtraordinarios extends AppCompatActivity {
    private AdapterSucesoExtraordinario arrayAdapter;
    private final List<Suceso> list = new ArrayList<>();
    private RecyclerView lv;
    private TextView tv;
    private KProgressHUD k;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sucesos_extraordinarios);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        lv = findViewById(R.id.he);
        tv = findViewById(R.id.ne);
        k = KProgressHUD.create(ListaSucesosExtraordinarios.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Cargando datos...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        id = getIntent().getStringExtra("id");
        obtenerSucesos();
    }

    private void obtenerSucesos() {
        FirebaseFirestore.getInstance().collection("Datos").document(id).collection("Sucesos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                            for (DocumentSnapshot document : l) {
                                Suceso s = document.toObject(Suceso.class);
                                list.add(s);
                            }
                            if(list.isEmpty()){
                                tv.setVisibility(View.VISIBLE);
                                lv.setVisibility(View.GONE);
                                k.dismiss();
                            }else{
                                tv.setVisibility(View.GONE);
                                lv.setVisibility(View.VISIBLE);
                                lv.setHasFixedSize(true);
                                lv.setLayoutManager(new LinearLayoutManager(ListaSucesosExtraordinarios.this));
                                arrayAdapter = new AdapterSucesoExtraordinario(list);
                                lv.setAdapter(arrayAdapter);
                                k.dismiss();
                            }
                        }
                    }
                });
    }
}

package com.example.trackem.Compartidas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackem.Auxiliares.AdapterEvento;
import com.example.trackem.Auxiliares.EventPersonalizado;
import com.example.trackem.PersonalMedico.CrearCita;
import com.example.trackem.R;
import com.github.nikartm.button.FitButton;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Calendario extends AppCompatActivity {

    private final List<EventPersonalizado> list = new ArrayList<>();
    private CompactCalendarView cv;
    private final Map<EventPersonalizado, String> keys = new HashMap<>();
    private TextView tv;
    private TextView tv2;
    private boolean doctor = false;
    private Date f;
    private AlertDialog.Builder dialog;
    private String idActual = "";
    private AdapterEvento arrayAdapter;
    private RecyclerView lv;
    private KProgressHUD k;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_calendar);
        cv = findViewById(R.id.calendar);
        tv = findViewById(R.id.tv);
        f = new Date();
        FitButton fitButton = findViewById(R.id.crearevento);
        lv = findViewById(R.id.hc);
        dialog = new AlertDialog.Builder(this);
        tv2 = findViewById(R.id.nc);
        idActual = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        if(getIntent().getBooleanExtra("doctor", false)){
            fitButton.setVisibility(View.VISIBLE);
            doctor = true;
            recuperar("iddoctor");
        }else{
            recuperar("idpaciente");
        }
        cv.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                mostrar(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mostrar(firstDayOfNewMonth);
            }
        });
    }

    private void mostrar(Date fecha) {
        f = fecha;
        SimpleDateFormat dateFormat = new SimpleDateFormat("d LLLL y", Locale.getDefault());
        tv.setText(dateFormat.format(fecha));
        final List<EventPersonalizado> le = new ArrayList<>();
        for (Event e: cv.getEvents(fecha)
             ) {
            le.add((EventPersonalizado) e);
        }
        if(le.isEmpty()){
            lv.setVisibility(View.GONE);
            tv2.setVisibility(View.VISIBLE);
        }else{
            tv2.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            arrayAdapter = new AdapterEvento(new ArrayList<>(le));
            if(doctor)
                arrayAdapter.setOnItemClickListener(new AdapterEvento.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        final EventPersonalizado event = le.get(position);
                        PopupMenu p = new PopupMenu(Calendario.this, v);
                        p.getMenuInflater().inflate(R.menu.popup_menu_cita, p.getMenu());
                        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.toString().equals("Eliminar")) {
                                    dialog.setTitle("Eliminar Cita")
                                            .setMessage("¿Seguro que quiere eliminar la cita?")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    FirebaseFirestore.getInstance().collection("Calendario").document(Objects.requireNonNull(keys.get(event))).delete() ;
                                                    Toast.makeText(getApplicationContext(), "Se ha eliminado la cita correctamente.", Toast.LENGTH_LONG).show();
                                                    recuperar("iddoctor");
                                                }})
                                            .setNegativeButton(android.R.string.no, null).show();
                                }
                                return false;
                            }
                        });
                        p.show();
                    }
                });
            lv.setHasFixedSize(true);
            lv.setLayoutManager(new LinearLayoutManager(this));
            lv.setAdapter(arrayAdapter);
        }
    }


    private void recuperar(String campo){
        k = KProgressHUD.create(Calendario.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Espere...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        if(!list.isEmpty()){
            list.clear();
            cv.removeAllEvents();
            keys.clear();
            arrayAdapter.clear();
        }
        FirebaseFirestore.getInstance().collection("Calendario")
                .whereEqualTo(campo, idActual)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> l = Objects.requireNonNull(task.getResult()).getDocuments();
                            for (DocumentSnapshot document : l) {
                                EventPersonalizado eventPersonalizado = document.toObject(EventPersonalizado.class);
                                keys.put(eventPersonalizado, document.getId());
                                list.add(eventPersonalizado);
                            }
                            for (EventPersonalizado e: list
                                 ) {
                                cv.addEvent(e);
                            }
                            mostrar(f);
                            k.dismiss();
                        }
                    }
                });
    }

    public void createEvent(View view) {
        Intent i = new Intent(Calendario.this, CrearCita.class);
        i.putExtra("fecha", f.getTime());
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                recuperar("iddoctor");
            }
        }
    }
}

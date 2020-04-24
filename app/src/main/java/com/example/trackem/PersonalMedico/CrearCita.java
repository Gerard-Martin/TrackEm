package com.example.trackem.PersonalMedico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackem.Auxiliares.EventPersonalizado;
import com.example.trackem.Compartidas.SeleccionarDuracion;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.isapanah.awesomespinner.AwesomeSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CrearCita extends AppCompatActivity {

    private AwesomeSpinner my_spinner;
    private String id = "";
    private long fecha;
    private boolean s1 = false;
    private boolean s2 = false;
    private String inicio;
    private int duracion;
    private int spinner = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cita);
        Objects.requireNonNull(getSupportActionBar()).hide();
        my_spinner = findViewById(R.id.my_spinner);
        List<String> categories = new ArrayList<String>();
        categories.add("Cita");
        categories.add("Medicación");
        final LinearLayout l = findViewById(R.id.tiempo);
        fecha = getIntent().getLongExtra("fecha", 0);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        my_spinner.setAdapter(categoriesAdapter);
        my_spinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                if(itemAtPosition.equals("Cita")){
                    spinner = 0;
                    l.setVisibility(View.VISIBLE);
                }else{
                    spinner = 1;
                    l.setVisibility(View.GONE);
                }
            }
        });
        my_spinner.setSpinnerHint("Seleccione el tipo de evento");
    }

    public void createEvent(View view) {
        EditText et = findViewById(R.id.titulo);
        if(et.getText().toString().equals("") || !my_spinner.isSelected() ||!s1 ||(spinner == 0 && !s2)){
            Toast.makeText(this, "Introduzca la información necesaria", Toast.LENGTH_SHORT).show();
            return;
        }
        EventPersonalizado e = null;
        if(spinner == 0){
            Date da = new Date(fecha);
            SimpleDateFormat d1, d2;
            d1 = new SimpleDateFormat("dd M yyyy");
            d2 = new SimpleDateFormat("dd M yyyy HH:mm");
            String fe1 = d1.format(da);
            fe1 = fe1 + " " + inicio;
            Date def;
            try {
                def = d2.parse(fe1);
                e = new EventPersonalizado(Color.parseColor("#4EA5FB"), Objects.requireNonNull(def).getTime(), et.getText().toString() + '\n' + String.format("(Duración %d minutos)", duracion), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),id);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }else{
            e = new EventPersonalizado(Color.parseColor("#FBB44E"),fecha, et.getText().toString(), id);
        }
        if(e != null) FirebaseFirestore.getInstance().collection("Calendario").add(e).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CrearCita.this, "Se ha creado el evento correctamente", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK,getIntent());
                    finish();
                }else{
                    Toast.makeText(CrearCita.this, "Ha sucedido un error, intentelo más tarde", Toast.LENGTH_SHORT).show();
                }
            }
        });
        else Toast.makeText(CrearCita.this, "Ha sucedido un error, intentelo más tarde", Toast.LENGTH_SHORT).show();
    }

    public void seleccionarDuracion(View view) {
        startActivityForResult(new Intent(CrearCita.this, SeleccionarDuracion.class), 2);

    }

    public void seleccionpaciente(View view) {
        Intent i = new Intent(CrearCita.this, ListaPacientes.class);
        i.putExtra("cita", true);
        startActivityForResult(i, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                s1 = true;
                id = data.getStringExtra("id");
                String nombre = data.getStringExtra("nombre");
                TextView v = findViewById(R.id.pacientesel);
                v.setText(nombre);
            }
        }else{
            if(resultCode == Activity.RESULT_OK){
                s2 = true;
                inicio = data.getStringExtra("inicio");
                duracion = (int) data.getLongExtra("duracion", 0);
                TextView v = findViewById(R.id.dur);
                v.setText(String.format("%s - %d minutos", inicio, duracion));
            }
        }
    }
}

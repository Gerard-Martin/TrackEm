package com.example.trackem.PersonalMedico;

import androidx.annotation.NonNull;
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

import com.example.trackem.Auxiliares.AdapterPaciente;
import com.example.trackem.Auxiliares.Usuario;
import com.example.trackem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaPacientes extends AppCompatActivity {
    private AdapterPaciente arrayAdapter;
    private RecyclerView lv;
    private TextView tv;
    private final List<Usuario> usuarioList = new ArrayList<>();
    private final List<String> clavesUsuario = new ArrayList<>();
    private KProgressHUD k;
    private boolean cita = false;
    private boolean consulta = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_lista_pacientes);
        lv = findViewById(R.id.hp);
        k = KProgressHUD.create(ListaPacientes.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Cargando datos...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        tv = findViewById(R.id.np);
        getSupportActionBar().hide();
        if(getIntent().getBooleanExtra("cita", false)) cita = true;
        if(getIntent().getBooleanExtra("consulta", false)) consulta = true;
        obtenerPacientes();
    }

    private void obtenerPacientes() {
        FirebaseDatabase.getInstance().getReference("Usuarios")
                .orderByChild("tipo").equalTo(0)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!usuarioList.isEmpty()){
                            usuarioList.clear();
                            clavesUsuario.clear();
                            arrayAdapter.clear();
                        }
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Usuario usuario = postSnapshot.getValue(Usuario.class);
                            usuarioList.add(usuario);
                            clavesUsuario.add(postSnapshot.getKey());
                        }
                        if(usuarioList.isEmpty()){
                            lv.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                            k.dismiss();
                        }else {
                            tv.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                            lv.setHasFixedSize(true);
                            lv.setLayoutManager(new LinearLayoutManager(ListaPacientes.this));
                            arrayAdapter = new AdapterPaciente(usuarioList);
                            arrayAdapter.setOnItemClickListener(new AdapterPaciente.ClickListener() {
                                @Override
                                public void onItemClick(final int position, View v) {
                                    if (cita) {
                                        creacionCita(clavesUsuario.get(position), usuarioList.get(position).nombre);
                                    } else if (consulta) {
                                        Intent i = new Intent(ListaPacientes.this, SeguimientoPaciente.class);
                                        i.putExtra("id", clavesUsuario.get(position));
                                        startActivity(i);
                                    } else {
                                        PopupMenu p = new PopupMenu(ListaPacientes.this, v);
                                        p.getMenuInflater().inflate(R.menu.popup_menu, p.getMenu());
                                        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                if (item.toString().equals("Editar")) {
                                                    editarPerfil(usuarioList.get(position), clavesUsuario.get(position));
                                                } else {
                                                    eliminarPerfil(usuarioList.get(position), clavesUsuario.get(position));
                                                }
                                                return false;
                                            }
                                        });
                                        p.show();
                                    }
                                }
                            });
                            k.dismiss();
                            lv.setAdapter(arrayAdapter);
                        }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void creacionCita(String id, String nombre) {
        Intent i = getIntent();
        i.putExtra("id", id);
        i.putExtra("nombre", nombre);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    private void editarPerfil(Usuario usuario, String s) {
        Intent i = new Intent(ListaPacientes.this, CrearPaciente.class);
        i.putExtra(getString(R.string.nombre), usuario.nombre);
        i.putExtra(getString(R.string.numero), usuario.numero);
        i.putExtra(getString(R.string.imagen), usuario.imagen);
        i.putExtra(getString(R.string.id), s);
        startActivityForResult(i, 2);
    }

    private void eliminarPerfil(Usuario usuario, final String id) {
         new AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage(String.format("Seguro que desea eliminar a %s del sistema?",usuario.nombre))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseDatabase.getInstance().getReference("Usuarios")
                                .child(id)
                                .child("tipo")
                                .setValue(2);
                        arrayAdapter.clear();
                        arrayAdapter.notifyDataSetChanged();
                        obtenerPacientes();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void crearPaciente(View view) {
        startActivity(new Intent(ListaPacientes.this, CrearPaciente.class));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                obtenerPacientes();
            }
        }
    }

}

package com.example.trackem.Compartidas;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trackem.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Configuracion extends AppCompatActivity {
    private CircleImageView c;
    private KProgressHUD k;
    private Uri imagen;
    private String nombre ="";
    private String telefono= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        setContentView(R.layout.activity_configuracion);
        Objects.requireNonNull(actionBar).hide();
        c = findViewById(R.id.imagen);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl() != null)
        Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .fallback(R.drawable.ic_fallback_user)
                .skipMemoryCache(true)
                .into(c);
        k = KProgressHUD.create(Configuracion.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Subiendo foto...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        FirebaseDatabase.getInstance().getReference("Usuarios")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                k.dismiss();
                nombre = dataSnapshot.child("nombre").getValue(String.class);
                telefono = dataSnapshot.child("numero").getValue(String.class);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("Numero", telefono);
                edit.putString("Nombre", nombre);
                edit.apply();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new prefsFragment())
                        .commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static class prefsFragment extends PreferenceFragmentCompat{
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
            EditTextPreference n, m;
            n = (EditTextPreference) findPreference("Nombre");
            m = (EditTextPreference) findPreference("Numero");
            n.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    FirebaseDatabase.getInstance().getReference("Usuarios")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child("nombre")
                            .setValue(newValue);
                    return false;
                }
            });
            m.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    FirebaseDatabase.getInstance().getReference("Usuarios")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child("numero")
                            .setValue(newValue);
                    return false;
                }
            });
        }

        @Override
        public boolean onPreferenceTreeClick ( Preference preference)
        {
            String key = preference.getKey();
            if(key.equals("Contraseña")){

                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("Cambiar Contraseña")
                        .setMessage("Seguro que desea cambiar la contraseña?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseAuth.getInstance().sendPasswordResetEmail(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));
                                Toast.makeText(getContext(), "Se ha enviado el correo de recuperación! Revise su buzón.", Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;

            }
            return false;
        }
    }

    public void informacion(View view){

        new AlertDialog.Builder(Configuracion.this)
                .setTitle("Información Adicional")
                .setMessage("Desarrollado por Gerard Martín López.\n\nAño 2019-2020.\n\nEsta aplicación ha estado pensada y desarrollada para mejorar la vida de todos los pacientes que sufren Esclerosis Múltiple.")
                .setPositiveButton("Cerrar", null)
                .create().show();
    }

    public void cerrar(View view){

        new AlertDialog.Builder(Configuracion.this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Seguro que quiere cerrar la sesión?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Configuracion.this, Login.class));
                        finishAffinity();
                        Toast.makeText(Configuracion.this, getString(R.string.cerrarsesion), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", null)
                .create().show();
    }

    public void editarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            boolean shouldProvideRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(Configuracion.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
            if (shouldProvideRationale) {
                new AlertDialog.Builder(Configuracion.this)
                        .setTitle("Permiso")
                        .setMessage("Se necesitan permisos para usar esta aplicación.")
                        .setPositiveButton("Conceder", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Configuracion.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        2);
                            }
                        })
                        .setNegativeButton("Más tarde", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "No se han podido conceder los permisos", Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(Configuracion.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
            }
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escoja una imagen"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null && resultCode == RESULT_OK) {
            imagen = data.getData();
            updatePhotograph();
        }
    }

    private void updatePhotograph() {
        k.show();
        Glide.with(this).load(imagen)
                .fallback(R.drawable.ic_fallback_user)
                .skipMemoryCache(true)
                .into(c);
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("Images/profile/" + FirebaseAuth.getInstance().getUid())
                .child(Objects.requireNonNull(imagen.getLastPathSegment()));

        ref.putFile(imagen).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    final Uri downloadUri = task.getResult();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri)
                            .build();
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("Usuarios")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("imagen")
                                        .setValue(Objects.requireNonNull(downloadUri).toString());
                                Toast.makeText(Configuracion.this, getString(R.string.correctaimagen), Toast.LENGTH_LONG).show();
                                k.dismiss();
                            } else {
                                Toast.makeText(Configuracion.this, getString(R.string.errorimagen), Toast.LENGTH_LONG).show();
                                k.dismiss();
                                finish();
                            }
                        }
                    });
                }
            }
         });
    }
}
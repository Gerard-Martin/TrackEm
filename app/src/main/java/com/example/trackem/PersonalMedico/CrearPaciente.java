package com.example.trackem.PersonalMedico;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trackem.Auxiliares.Usuario;
import com.example.trackem.Compartidas.Login;
import com.example.trackem.R;
import com.github.nikartm.button.FitButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CrearPaciente extends AppCompatActivity {

    private EditText nombre;
    private EditText email;
    private EditText telefono;
    private EditText contraseña1;
    private EditText contraseña2;
    private Uri imagen = Uri.parse("android.resource://com.example.trackem/drawable/profile");
    private KProgressHUD k;
    private String id = "";
    private FitButton fb;
    private CircleImageView c;
    private boolean intent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_crear_paciente);
        nombre = findViewById(R.id.nombre);
        email = findViewById(R.id.email);
        telefono = findViewById(R.id.telefono);
        contraseña1 = findViewById(R.id.contraseña);
        c = findViewById(R.id.imagen);
        fb = findViewById(R.id.editButton);
        contraseña2 = findViewById(R.id.contraseña2);
        if(getIntent().hasExtra("id")){
            intent = true;
            id = getIntent().getStringExtra(getString(R.string.id));
            initialize(getIntent().getStringExtra(getString(R.string.nombre)),getIntent().getStringExtra(getString(R.string.numero)), getIntent().getStringExtra(getString(R.string.imagen)));
        }
    }

    private void initialize(String no, String nu, String im) {
        nombre.setText(no);
        telefono.setText(nu);
        email.setVisibility(View.GONE);
        contraseña1.setVisibility(View.GONE);
        contraseña2.setVisibility(View.GONE);
        fb.setVisibility(View.GONE);
        Glide.with(this).load(im)
                .fallback(R.drawable.ic_fallback_user)
                .skipMemoryCache(true)
                .into(c);
    }

    public void guardarPaciente(View view) {
        final String snombre = nombre.getText().toString().trim();
        final String semail = email.getText().toString().trim();
        String scontraseña1 = contraseña1.getText().toString().trim();
        String scontraseña2 = contraseña2.getText().toString().trim();
        final String stelefono = telefono.getText().toString().trim();

        if(!intent) {

            if (snombre.isEmpty()) {
                nombre.setError(getString(R.string.nombre));
                return;
            }

            if (semail.isEmpty()) {
                email.setError(getString(R.string.corr));
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
                email.setError(getString(R.string.corr));
                return;
            }

            if (scontraseña1.isEmpty()) {
                contraseña1.setError(getString(R.string.cont));
                return;
            }

            if (scontraseña2.isEmpty()) {
                contraseña2.setError(getString(R.string.cont));
                return;
            }

            if (contraseña1.length() < 6) {
                contraseña1.setError(getString(R.string.corta));
                return;
            }

            if (!Objects.equals(scontraseña1, scontraseña2)) {
                contraseña1.setError(getString(R.string.nomisma));
                contraseña2.setError(getString(R.string.nomisma));
                return;
            }


            if (stelefono.isEmpty()) {
                telefono.setError(getString(R.string.notel));
                return;
            }

            if (stelefono.length() != 9) {
                telefono.setError(getString(R.string.valtelf));
                return;
            }
            k = KProgressHUD.create(CrearPaciente.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Creando usuario...")
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
            k.show();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(semail, scontraseña1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                infoExtra(snombre, stelefono);
                            } else {
                                k.dismiss();
                                Toast.makeText(CrearPaciente.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else{
            if (snombre.isEmpty()) {
                nombre.setError(getString(R.string.nombre));
                return;
            }


            if (stelefono.isEmpty()) {
                telefono.setError(getString(R.string.notel));
                return;
            }

            if (stelefono.length() != 9) {
                telefono.setError(getString(R.string.valtelf));
                return;
            }

            k = KProgressHUD.create(CrearPaciente.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Guardando los cambios...")
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);

            k.show();

            FirebaseDatabase.getInstance().getReference("Usuarios")
                    .child(id)
                    .child("nombre")
                    .setValue(snombre);
            FirebaseDatabase.getInstance().getReference("Usuarios")
                    .child(id)
                    .child("numero")
                    .setValue(stelefono);
            Toast.makeText(getApplicationContext(), "Se han modificado los datos correctamente!", Toast.LENGTH_SHORT).show();
            k.dismiss();
            setResult(3);
            finish();
        }
    }

    private void infoExtra(final String snombre, final String stelefono) {
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
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseDatabase.getInstance().getReference("Usuarios")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(new Usuario(snombre, Objects.requireNonNull(downloadUri).toString(),stelefono, 0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(CrearPaciente.this, getString(R.string.erregistro), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    k.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CrearPaciente.this, getString(R.string.regcorr), Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(CrearPaciente.this, Login.class));
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(CrearPaciente.this, getString(R.string.erregistro), Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            });
                } else {
                    k.dismiss();
                    Toast.makeText(CrearPaciente.this, getString(R.string.erregistro), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void editarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            boolean shouldProvideRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(CrearPaciente.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
            if (shouldProvideRationale) {
                new AlertDialog.Builder(CrearPaciente.this)
                        .setTitle("Permiso")
                        .setMessage("Se necesitan permisos para usar esta aplicación.")
                        .setPositiveButton("Conceder", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(CrearPaciente.this,
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
                ActivityCompat.requestPermissions(CrearPaciente.this,
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
        if (requestCode == 1 && data.getData() != null && resultCode == RESULT_OK) {
            imagen = data.getData();
            Glide.with(this).load(imagen)
                    .fallback(R.drawable.ic_fallback_user)
                    .skipMemoryCache(true)
                    .into(c);

        }
    }

    public void reset(View view) {
    }
}

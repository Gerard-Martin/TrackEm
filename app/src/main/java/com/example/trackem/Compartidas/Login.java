package com.example.trackem.Compartidas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trackem.Pacientes.PrincipalPacientes;
import com.example.trackem.PersonalMedico.PrincipalMedicos;
import com.example.trackem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private EditText textoemail;
    private EditText textocontraseña;
    private CircularProgressButton botonlogin;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private KProgressHUD k;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.login);
        k = KProgressHUD.create(Login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Espere...")
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        k.show();
        botonlogin = findViewById(R.id.login);
        textocontraseña = findViewById(R.id.input_password);
        textoemail = findViewById(R.id.input_email);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
           comprobar();
        }else{
            k.dismiss();
            botonlogin.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

        }

    }

    private void login() {
        if(validate()){
            botonlogin.showProgress();
            botonlogin.setIndeterminateProgressMode(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                botonlogin.showComplete();
                                Toast.makeText(getApplicationContext(), R.string.correctologin, Toast.LENGTH_LONG).show();
                                comprobar();
                            }
                            else {
                                botonlogin.showError();
                                Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                botonlogin.showIdle();
                            }
                        }
                    });
        }else
            Toast.makeText(getApplicationContext(), R.string.errorlogin, Toast.LENGTH_LONG).show();
    }

    private void comprobar() {
        FirebaseDatabase.getInstance().getReference("Usuarios")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                k.dismiss();
                String nombre = dataSnapshot.child("nombre").getValue(String.class);
                if (dataSnapshot.child("tipo").getValue(Long.class) == 0) {
                    startActivity(new Intent(Login.this, PrincipalPacientes.class).putExtra("nombre", nombre));
                    finish();
                } else if (dataSnapshot.child("tipo").getValue(Long.class) == 1) {
                    startActivity(new Intent(Login.this, PrincipalMedicos.class).putExtra("nombre", nombre));
                    finish();
                } else {
                    Toast.makeText(Login.this, getString(R.string.eliminado), Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private boolean validate() {
        boolean valid = true;

        email = textoemail.getText().toString();
        password = textocontraseña.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textoemail.setError(getString(R.string.email));
            valid = false;
        } else {
            textoemail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            textocontraseña.setError(getString(R.string.errorcontraseña));
            valid = false;
        } else {
            textocontraseña.setError(null);
        }

        return valid;
    }

    public void olvidado(View view) {
        startActivity(new Intent(Login.this, RecuperarContrasena.class));
    }
}

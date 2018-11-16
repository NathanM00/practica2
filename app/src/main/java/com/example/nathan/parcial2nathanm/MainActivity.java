package com.example.nathan.parcial2nathanm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase db;

    EditText edt_correo;
    EditText edt_contraseña;
    EditText edt_nombre;

    Button btn_ingresar;
    Button btn_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        btn_ingresar = findViewById(R.id.btn_iniciar);
        btn_registrar = findViewById(R.id.btn_registrar);
        edt_contraseña = findViewById(R.id.edt_contraseña);
        edt_correo = findViewById(R.id.edt_correo);
        edt_nombre = findViewById(R.id.edt_nombre);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombre = edt_nombre.getText().toString();
                final String correo = edt_correo.getText().toString();
                final String contra = edt_contraseña.getText().toString();

                auth.createUserWithEmailAndPassword(correo,contra).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            String uid= auth.getCurrentUser().getUid();
                            Usuario usuario = new Usuario(correo,contra);
                            usuario.uid = uid;
                            DatabaseReference reference = db.getReference();

                            reference.child("Usuarios").child(uid).setValue(usuario);

                            Intent intent = new Intent(MainActivity.this, Chat.class);
                            startActivity(intent);
                        }

                    }
                });

            }
        });


        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nombre = edt_nombre.getText().toString();
                final String correo = edt_correo.getText().toString();
                final String contra = edt_contraseña.getText().toString();

                auth.signInWithEmailAndPassword(correo,contra).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, Chat.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

    }
}

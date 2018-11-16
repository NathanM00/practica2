package com.example.nathan.parcial2nathanm;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Chat extends AppCompatActivity {

    EditText CHat;
    ImageButton enviar;
    ListView lista;
    FirebaseListAdapter listAdapter;
    FirebaseDatabase db;
    FirebaseAuth auth;
    String nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        CHat = findViewById(R.id.edt_caja);
        enviar = findViewById(R.id.btn_enviar);
        lista = findViewById(R.id.list_mensajes);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = db.getReference();
        final Query chat = reference.child("chats");

        FirebaseListOptions options = new FirebaseListOptions.Builder<Mensaje>()
                .setLayout(R.layout.renglon)
                .setQuery(chat, Mensaje.class)
                .build();
                listAdapter = new FirebaseListAdapter<Mensaje>(options) {
                    @Override
                    protected void populateView(@NonNull View v, @NonNull Mensaje model, final int position) {

                        TextView name = v.findViewById(R.id.tv_nombre);
                        TextView msg = v.findViewById(R.id.tv_mensaje);

                        name.setText(model.nombre);
                        msg.setText(model.mensaje);

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listAdapter.getRef(position).removeValue();
                            }
                        });

                    }
                };

                lista.setAdapter(listAdapter);

                FirebaseUser user = auth.getCurrentUser();

                DatabaseReference ref = db.getReference()
                        .child("usuarios").child(user.getUid());

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dato : dataSnapshot.getChildren()){
                            Usuario user = dato.getValue(Usuario.class);
                            nameUser = user.nombre;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       String mesa = CHat.getText().toString();

                       Mensaje m = new Mensaje(nameUser,mesa);

                       DatabaseReference publicar = db.getReference();
                       publicar.child("chat").push().setValue(m);
                    }
                });

    }


    @Override
    protected void onStart() {
        listAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        listAdapter.stopListening();
        super.onStop();
    }
}

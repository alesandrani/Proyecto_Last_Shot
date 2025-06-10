package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Actividad para chat con Firebase Realtime Database.
 * Permite enviar y recibir mensajes en tiempo real.
 */
public class ActivityChatFireBase extends AppCompatActivity {

    private TextView tvTituloChat;
    private RecyclerView recyclerMensajes;
    private EditText editMensaje;
    private ImageButton btnEnviar;
    private ProgressBar progressBar;

    private ArrayList<Mensaje> listaMensajes = new ArrayList<>();
    private MensajeAdapter adapter;

    // Datos del chat
    private String nombreJugadorActual;
    private String idSala;

    private DatabaseReference mensajesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_firebase);

        // Inicializar vistas
        tvTituloChat = findViewById(R.id.tvTituloChat);
        recyclerMensajes = findViewById(R.id.recyclerMensajes);
        editMensaje = findViewById(R.id.editMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        progressBar = findViewById(R.id.progressBar);

        // Obtener datos del Intent
        nombreJugadorActual = getIntent().getStringExtra("nombreJugadorActual");
        idSala = getIntent().getStringExtra("idSala");

        if (nombreJugadorActual == null || idSala == null) {
            Toast.makeText(this, "Error: faltan datos de usuario o sala", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Configurar UI
        tvTituloChat.setText("Chat de la Sala");
        recyclerMensajes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MensajeAdapter(listaMensajes, nombreJugadorActual);
        recyclerMensajes.setAdapter(adapter);

        // Configurar referencia Firebase
        mensajesRef = FirebaseDatabase.getInstance()
                .getReference("salas")
                .child(idSala)
                .child("chat_grupal");

        // Configurar listeners
        btnEnviar.setOnClickListener(v -> enviarMensaje());
        escucharMensajes();
    }

    private void enviarMensaje() {
        String texto = editMensaje.getText().toString().trim();
        if (!TextUtils.isEmpty(texto)) {
            String mensajeId = mensajesRef.push().getKey();
            if (mensajeId != null) {
                Mensaje mensaje = new Mensaje(
                    texto,
                    nombreJugadorActual,
                    null,
                    System.currentTimeMillis(),
                    idSala,
                    "grupal"
                );
                mensaje.setId(mensajeId);

                mensajesRef.child(mensajeId).setValue(mensaje)
                    .addOnSuccessListener(aVoid -> {
                        editMensaje.setText("");
                        Log.d("ChatFirebase", "Mensaje enviado correctamente");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
                        Log.e("ChatFirebase", "Error al enviar mensaje: " + e.getMessage());
                    });
            }
        }
    }

    private void escucharMensajes() {
        progressBar.setVisibility(View.VISIBLE);
        
        // Ordenar mensajes por timestamp
        Query query = mensajesRef.orderByChild("timestamp");
        
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Mensaje mensaje = snapshot.getValue(Mensaje.class);
                if (mensaje != null) {
                    mensaje.setId(snapshot.getKey());
                    listaMensajes.add(mensaje);
                    Collections.sort(listaMensajes, Comparator.comparingLong(Mensaje::getTimestamp));
                    adapter.notifyDataSetChanged();
                    recyclerMensajes.scrollToPosition(listaMensajes.size() - 1);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                Mensaje mensajeActualizado = snapshot.getValue(Mensaje.class);
                if (mensajeActualizado != null) {
                    mensajeActualizado.setId(snapshot.getKey());
                    for (int i = 0; i < listaMensajes.size(); i++) {
                        if (listaMensajes.get(i).getId().equals(mensajeActualizado.getId())) {
                            listaMensajes.set(i, mensajeActualizado);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String mensajeId = snapshot.getKey();
                for (int i = 0; i < listaMensajes.size(); i++) {
                    if (listaMensajes.get(i).getId().equals(mensajeId)) {
                        listaMensajes.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ActivityChatFireBase.this, 
                    "Error al cargar mensajes: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
                Log.e("ChatFirebase", "Error en Firebase: " + error.getMessage());
            }
        });
    }
}

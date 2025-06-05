package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatPrivadoActivity extends AppCompatActivity {

    private static final String TAG = "ChatPrivadoActivity";

    private TextView tvTituloChat;
    private RecyclerView recyclerMensajes;
    private EditText editMensaje;
    private ImageButton btnEnviar;

    private DatabaseReference chatRef;
    private MensajeAdapter mensajeAdapter;
    private List<Mensaje> listaMensajes = new ArrayList<>();

    private String nombreActual;
    private String nombreDestino;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_privado);

        tvTituloChat = findViewById(R.id.tvTituloChat);
        recyclerMensajes = findViewById(R.id.recyclerMensajes);
        editMensaje = findViewById(R.id.editMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        // Recibir los nombres desde el Intent
        nombreActual = getIntent().getStringExtra("nombreJugadorActual");
        nombreDestino = getIntent().getStringExtra("nombreJugadorDestino");

        Log.d(TAG, "nombreJugadorActual: " + nombreActual);
        Log.d(TAG, "nombreJugadorDestino: " + nombreDestino);

        if (nombreActual == null || nombreDestino == null) {
            Toast.makeText(this, "Error: faltan datos de usuario", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error: nombreJugadorActual o nombreJugadorDestino es null");
            finish();
            return;
        }

        // Mostrar título con el nombre del jugador destino
        tvTituloChat.setText("Chat con " + nombreDestino);

        // Crear un id único para la conversación (mismo para ambos jugadores)
        chatId = generarChatId(nombreActual, nombreDestino);
        Log.d(TAG, "chatId generado: " + chatId);

        chatRef = FirebaseDatabase.getInstance().getReference("chats_privados").child(chatId).child("mensajes");

        mensajeAdapter = new MensajeAdapter(listaMensajes, nombreActual);
        recyclerMensajes.setLayoutManager(new LinearLayoutManager(this));
        recyclerMensajes.setAdapter(mensajeAdapter);

        btnEnviar.setOnClickListener(v -> enviarMensaje());

        escucharMensajes();
    }

    private void enviarMensaje() {
        String texto = editMensaje.getText().toString().trim();
        if (!texto.isEmpty()) {
            String mensajeId = chatRef.push().getKey();
            Mensaje mensaje = new Mensaje(texto, nombreActual, nombreDestino, System.currentTimeMillis());
            chatRef.child(mensajeId).setValue(mensaje);
            editMensaje.setText("");
            Log.d(TAG, "Mensaje enviado: " + texto);
        } else {
            Log.d(TAG, "Intento de enviar mensaje vacío");
        }
    }

    private void escucharMensajes() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaMensajes.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Mensaje m = snap.getValue(Mensaje.class);
                    listaMensajes.add(m);
                }
                mensajeAdapter.notifyDataSetChanged();
                recyclerMensajes.scrollToPosition(listaMensajes.size() - 1);
                Log.d(TAG, "Mensajes actualizados, total: " + listaMensajes.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error escuchando mensajes: " + error.getMessage());
            }
        });
    }

    private String generarChatId(String a, String b) {
        // Ordena los nombres para garantizar el mismo chatId para ambos usuarios
        return (a.compareTo(b) < 0) ? a + "_" + b : b + "_" + a;
    }
}

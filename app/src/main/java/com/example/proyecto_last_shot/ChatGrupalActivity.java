package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ChatGrupalActivity gestiona el chat grupal dentro de una sala.
 * Permite enviar y recibir mensajes en tiempo real usando Firebase Realtime Database.
 * Los mensajes se muestran en un RecyclerView y se sincronizan automáticamente.
 */
public class ChatGrupalActivity extends AppCompatActivity {

    private static final String TAG = "ChatGrupalActivity";

    private TextView tvTituloChat;
    private RecyclerView recyclerMensajes;
    private EditText editMensaje;
    private ImageButton btnEnviar;
    private ProgressBar progressBar;

    private DatabaseReference salaRef;
    private DatabaseReference mensajesRef;
    private MensajeAdapter mensajeAdapter;
    private ArrayList<Mensaje> listaMensajes = new ArrayList<>();

    private String nombreJugador; // Nombre del jugador actual
    private String idSala;        // ID de la sala (nodo en Firebase)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_grupal);

        // Inicializar vistas
        tvTituloChat = findViewById(R.id.tvTituloChat);
        recyclerMensajes = findViewById(R.id.recyclerMensajes);
        editMensaje = findViewById(R.id.editMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        progressBar = findViewById(R.id.progressBar);

        // Obtener datos del Intent
        nombreJugador = getIntent().getStringExtra("nombreJugador");
        idSala = getIntent().getStringExtra("idSala");

        if (nombreJugador == null || idSala == null) {
            Toast.makeText(this, "Error: Faltan datos necesarios", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Iniciando chat grupal - Jugador: " + nombreJugador + ", Sala: " + idSala);

        // Configurar UI
        tvTituloChat.setText("Chat de la Sala");
        recyclerMensajes.setLayoutManager(new LinearLayoutManager(this));
        mensajeAdapter = new MensajeAdapter(listaMensajes, nombreJugador);
        recyclerMensajes.setAdapter(mensajeAdapter);

        // Configurar Firebase con la URL correcta
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://lastshot-proyecto-default-rtdb.europe-west1.firebasedatabase.app");
        salaRef = database.getReference("salas").child(idSala);
        mensajesRef = salaRef.child("chat_grupal");

        // Cargar información de la sala
        cargarInfoSala();

        // Configurar listener para mensajes
        configurarListenerMensajes();

        // Configurar botón de enviar
        btnEnviar.setOnClickListener(v -> enviarMensaje());
    }

    /**
     * Carga la información de la sala (nombre, etc) desde Firebase.
     */
    private void cargarInfoSala() {
        salaRef.child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombreSala = dataSnapshot.child("nombre").getValue(String.class);
                    if (nombreSala != null) {
                        tvTituloChat.setText(nombreSala);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error al cargar info de sala: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Configura el listener para recibir mensajes en tiempo real y actualiza la UI.
     */
    private void configurarListenerMensajes() {
        mensajesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaMensajes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mensaje mensaje = snapshot.getValue(Mensaje.class);
                    if (mensaje != null) {
                        mensaje.setId(snapshot.getKey());
                        listaMensajes.add(mensaje);
                    }
                }
                mensajeAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error al cargar mensajes: " + databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Envía un mensaje al chat grupal si el campo de texto no está vacío.
     * El mensaje se almacena en Firebase y se limpia el campo de entrada.
     */
    private void enviarMensaje() {
        String texto = editMensaje.getText().toString().trim();
        if (!texto.isEmpty()) {
            String mensajeId = mensajesRef.push().getKey();
            if (mensajeId != null) {
                Mensaje mensaje = new Mensaje(
                    texto,
                    nombreJugador,
                    null, // receptor
                    System.currentTimeMillis(),
                    idSala,
                    "grupal"
                );
                mensaje.setId(mensajeId);

                mensajesRef.child(mensajeId).setValue(mensaje)
                    .addOnSuccessListener(aVoid -> {
                        editMensaje.setText("");
                        Log.d(TAG, "Mensaje enviado correctamente");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al enviar mensaje: " + e.getMessage());
                        Toast.makeText(ChatGrupalActivity.this, 
                            "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
                    });
            }
        }
    }
}

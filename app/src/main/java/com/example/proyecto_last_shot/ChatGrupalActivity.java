package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad para chat grupal dentro de una sala.
 * Muestra los mensajes en un ListView y permite enviar nuevos mensajes.
 * Los mensajes se sincronizan en tiempo real con Firebase Realtime Database.
 */
public class ChatGrupalActivity extends AppCompatActivity {

    private static final String TAG = "ChatGrupalActivity";

    private ListView listViewMensajes;
    private EditText editMensaje;
    private Button btnEnviar;

    private List<String> listaMensajes = new ArrayList<>();
    private MensajeListAdapter adapter;

    private DatabaseReference chatSalaRef;

    private String nombreJugador; // Nombre del jugador actual
    private String idSala;        // ID de la sala (nodo en Firebase)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_grupal);

        // Referencias UI
        listViewMensajes = findViewById(R.id.listViewMensajes);
        editMensaje = findViewById(R.id.editMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        // Obtener datos del Intent
        nombreJugador = getIntent().getStringExtra("nombreJugador");
        idSala = getIntent().getStringExtra("idSala");

        if (nombreJugador == null || idSala == null) {
            Toast.makeText(this, "Error: faltan datos de usuario o sala", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Inicializar referencia Firebase para la sala de chat grupal
        chatSalaRef = FirebaseDatabase.getInstance().getReference("chats_grupales").child(idSala).child("mensajes");

        // Inicializar adaptador para el ListView
        adapter = new MensajeListAdapter(this, listaMensajes, nombreJugador);
        listViewMensajes.setAdapter(adapter);

        // Botón enviar mensaje
        btnEnviar.setOnClickListener(v -> enviarMensaje());

        // Escuchar mensajes en Firebase
        escucharMensajes();
    }

    /**
     * Envía un mensaje a Firebase si el EditText no está vacío.
     * Limpia el EditText después de enviar.
     */
    private void enviarMensaje() {
        String texto = editMensaje.getText().toString().trim();
        if (!texto.isEmpty()) {
            // Crear un nuevo mensaje con ID único
            String mensajeId = chatSalaRef.push().getKey();

            if (mensajeId == null) {
                Log.e(TAG, "Error al generar ID para mensaje");
                return;
            }

            // Crear objeto Mensaje con texto, emisor, receptor null (chat grupal) y timestamp actual
            Mensaje mensaje = new Mensaje(texto, nombreJugador, null, System.currentTimeMillis());

            // Guardar mensaje en Firebase
            chatSalaRef.child(mensajeId).setValue(mensaje);

            // Limpiar campo texto
            editMensaje.setText("");
        } else {
            Toast.makeText(this, "No puedes enviar un mensaje vacío", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Escucha los cambios en Firebase para actualizar la lista de mensajes en tiempo real.
     */
    private void escucharMensajes() {
        chatSalaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaMensajes.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Mensaje mensaje = snap.getValue(Mensaje.class);
                    if (mensaje != null) {
                        // Formatear el mensaje para mostrar: "Emisor: texto"
                        String textoMostrar = mensaje.getEmisor() + ": " + mensaje.getTexto();
                        listaMensajes.add(textoMostrar);
                    }
                }

                adapter.notifyDataSetChanged();

                // Desplazar al último mensaje visible
                listViewMensajes.smoothScrollToPosition(listaMensajes.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al leer mensajes de Firebase: " + error.getMessage());
                Toast.makeText(ChatGrupalActivity.this, "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

/**
 * SeleccionarJugadorChatActivity permite al usuario seleccionar un jugador de la sala para iniciar un chat privado.
 * Muestra la lista de jugadores en un RecyclerView y permite abrir el chat privado o el chat grupal.
 */
public class SeleccionarJugadorChatActivity extends AppCompatActivity {
    private RecyclerView recyclerJugadores;
    private JugadorAdapter adapter;
    private ArrayList<String> listaJugadores = new ArrayList<>();
    private String idSala;
    private String nombreJugadorActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_chat);

        recyclerJugadores = findViewById(R.id.recyclerJugadores);
        recyclerJugadores.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JugadorAdapter(listaJugadores, nombre -> abrirChatPrivado(nombre));
        recyclerJugadores.setAdapter(adapter);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnChat = findViewById(R.id.btnChat);
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatGrupalActivity.class);
            intent.putExtra("nombreJugador", nombreJugadorActual);
            intent.putExtra("idSala", idSala);
            startActivity(intent);
        });

        // Obtener datos del intent
        idSala = getIntent().getStringExtra("idSala");
        nombreJugadorActual = getIntent().getStringExtra("nombreJugadorActual");

        if (idSala == null || nombreJugadorActual == null) {
            Toast.makeText(this, "Faltan datos de sala o usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarJugadores();
    }

    /**
     * Carga la lista de jugadores de la sala desde Firebase y actualiza la UI.
     */
    private void cargarJugadores() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://lastshot-proyecto-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("salas")
                .child(idSala)
                .child("jugadores");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaJugadores.clear();
                for (DataSnapshot jugadorSnap : snapshot.getChildren()) {
                    String nombre = jugadorSnap.getKey();
                    if (nombre != null && !nombre.equals(nombreJugadorActual)) {
                        listaJugadores.add(nombre);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SeleccionarJugadorChatActivity.this, "Error al cargar jugadores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Abre el chat privado con el jugador seleccionado.
     * @param nombreDestino Nombre del jugador destino
     */
    private void abrirChatPrivado(String nombreDestino) {
        Intent intent = new Intent(this, ChatPrivadoActivity.class);
        intent.putExtra("nombreJugadorActual", nombreJugadorActual);
        intent.putExtra("nombreJugadorDestino", nombreDestino);
        intent.putExtra("idSala", idSala);
        startActivity(intent);
    }
} 
package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityChatSeleccion extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnChat;
    private RecyclerView recyclerJugadores;
    private ArrayList<String> listaJugadores;
    private String nombreJugadorActual;  // Nombre del jugador actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_chat); // Cambia por tu XML real

        btnBack = findViewById(R.id.btnBack);
        btnChat = findViewById(R.id.btnChat);
        recyclerJugadores = findViewById(R.id.recyclerJugadores);

        // Recibir el nombre del jugador actual que llega desde la actividad anterior
        nombreJugadorActual = getIntent().getStringExtra("nombreJugadorActual");

        listaJugadores = getIntent().getStringArrayListExtra("listaJugadores");
        if (listaJugadores == null) {
            listaJugadores = new ArrayList<>();
        }

        recyclerJugadores.setLayoutManager(new LinearLayoutManager(this));
        JugadorAdapter adapter = new JugadorAdapter(this, listaJugadores, jugadorNombre -> {
            // Abrir chat privado con el jugador seleccionado, enviando ambos nombres
            Intent intent = new Intent(ActivityChatSeleccion.this, ChatPrivadoActivity.class);
            intent.putExtra("nombreJugadorActual", nombreJugadorActual);
            intent.putExtra("nombreJugadorDestino", jugadorNombre);
            startActivity(intent);
        });
        recyclerJugadores.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
        btnChat.setOnClickListener(v -> {
            // Acción para abrir chat grupal
        });
    }
}

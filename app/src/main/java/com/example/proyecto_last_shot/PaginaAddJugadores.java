package com.example.proyecto_last_shot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class PaginaAddJugadores extends AppCompatActivity {
    private EditText cuadroTexto;
    private LinearLayout container;
    private ImageButton btnAddJugador, btnNext;
    private ImageView btnBack;
    private ArrayList<String> listaJugadores = new ArrayList<>();
    private static final int MAX_JUGADORES = 10;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los elementos de la interfaz y configura los eventos.
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_add_jugadores);

        cuadroTexto = findViewById(R.id.cuadroTexto);
        btnAddJugador = findViewById(R.id.btnAddJugador);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.back);
        container = findViewById(R.id.container);

        // Borra la lista de jugadores almacenada al iniciar la actividad
        borrarJugadoresDePreferencias();

        btnAddJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarJugador();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuarJuego();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Agrega un jugador a la lista si no se ha alcanzado el máximo permitido.
     * Muestra un mensaje si se supera el límite.
     */
    private void agregarJugador() {
        String texto = cuadroTexto.getText().toString().trim();
        if (!texto.isEmpty()) {
            if (listaJugadores.size() < MAX_JUGADORES) {
                listaJugadores.add(texto);
                actualizarUI();
                guardarJugadoresEnPreferencias();
                cuadroTexto.setText(""); // Limpiar campo de texto
            } else {
                Toast.makeText(PaginaAddJugadores.this, "Máximo 10 jugadores", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Verifica si hay jugadores en la lista y, si es así, inicia la siguiente actividad.
     * Si la lista está vacía, muestra un mensaje de error.
     */
    private void continuarJuego() {
        if (listaJugadores.isEmpty()) {
            Toast.makeText(PaginaAddJugadores.this, "Debes agregar al menos un jugador", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(PaginaAddJugadores.this, PaginaJuegos.class);
            startActivity(intent);
        }
    }

    /**
     * Actualiza la interfaz de usuario mostrando la lista de jugadores agregados.
     */
    private void actualizarUI() {
        container.removeAllViews();
        for (String jugador : listaJugadores) {
            TextView nuevoJugador = new TextView(this);
            nuevoJugador.setText(jugador);
            nuevoJugador.setTextSize(18);
            nuevoJugador.setTextColor(getResources().getColor(R.color.black));
            nuevoJugador.setPadding(10, 10, 10, 10);
            container.addView(nuevoJugador);
        }
    }

    /**
     * Guarda la lista de jugadores en las preferencias compartidas utilizando JSON.
     */
    private void guardarJugadoresEnPreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaJugadores);
        editor.putString("listaJugadores", json);
        editor.apply();
    }

    /**
     * Borra la lista de jugadores almacenada en las preferencias compartidas y limpia la lista en memoria.
     */
    private void borrarJugadoresDePreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("listaJugadores"); // Borra la clave almacenada
        editor.apply();
        listaJugadores.clear(); // Limpia la lista en memoria
        actualizarUI();
    }
}

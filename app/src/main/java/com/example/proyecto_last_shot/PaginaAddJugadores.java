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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_add_jugadores);

        cuadroTexto = findViewById(R.id.cuadroTexto);
        btnAddJugador = findViewById(R.id.btnAddJugador);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.back);
        container = findViewById(R.id.container);

        //BORRAR LISTA AL INICIAR
        borrarJugadoresDePreferencias();

        btnAddJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = cuadroTexto.getText().toString().trim();
                if (!texto.isEmpty()) {
                    if (listaJugadores.size() < MAX_JUGADORES) {
                        listaJugadores.add(texto);
                        actualizarUI();
                        guardarJugadoresEnPreferencias();
                        cuadroTexto.setText(""); // Limpiar campo
                    } else {
                        Toast.makeText(PaginaAddJugadores.this, "Máximo 10 jugadores", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaJugadores.isEmpty()) {
                    Toast.makeText(PaginaAddJugadores.this, "Debes agregar al menos un jugador", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PaginaAddJugadores.this, PaginaJuegos.class);
                    startActivity(intent);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void actualizarUI() {
        container.removeAllViews();
        for (String jugador : listaJugadores) {
            TextView nuevoJugador = new TextView(this);
            nuevoJugador.setText(jugador);
            nuevoJugador.setTextSize(18);
            nuevoJugador.setPadding(10, 10, 10, 10);
            container.addView(nuevoJugador);
        }
    }

    private void guardarJugadoresEnPreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaJugadores);
        editor.putString("listaJugadores", json);
        editor.apply();
    }

    // 🛑 NUEVO: BORRAR JUGADORES AL INICIAR
    private void borrarJugadoresDePreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("listaJugadores"); // Borra la clave
        editor.apply();
        listaJugadores.clear(); // Limpia la lista en memoria
        actualizarUI();
    }
}

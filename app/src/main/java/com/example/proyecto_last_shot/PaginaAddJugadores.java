package com.example.proyecto_last_shot;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class PaginaAddJugadores extends AppCompatActivity {
    private EditText cuadroTexto;
    private LinearLayout container; // Contenedor donde se agregarán los nombres
    private ImageButton btnAddJugador, btnNext;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_add_jugadores);

        // Referencias a los elementos del XML
        cuadroTexto = findViewById(R.id.cuadroTexto);
        btnAddJugador = findViewById(R.id.btnAddJugador);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.back);
        container = findViewById(R.id.container);

        // Botón para añadir un nombre a la lista
        btnAddJugador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = cuadroTexto.getText().toString().trim();
                if (!texto.isEmpty()) {
                    // Crear un nuevo TextView con el nombre ingresado
                    TextView nuevoJugador = new TextView(PaginaAddJugadores.this);
                    nuevoJugador.setText(texto);
                    nuevoJugador.setTextSize(18);
                    nuevoJugador.setPadding(10, 10, 10, 10);

                    // Agregarlo al contenedor
                    container.addView(nuevoJugador);
                    cuadroTexto.setText(""); // Limpiar el campo de texto
                }
            }
        });

        // Botón para ir a la pantalla de juegos
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaginaAddJugadores.this, PaginaJuegos.class);
                startActivity(intent);
            }
        });

        // Botón para volver atrás
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad y vuelve a la anterior
            }
        });
    }
}
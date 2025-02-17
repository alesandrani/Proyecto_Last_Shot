package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaginaPrincipal extends AppCompatActivity {

    private ImageView menu, logo, cerveza;
    private LinearLayout boton;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial); // Asegúrate de usar el layout correcto

        // Referencias a los elementos del XML
        menu = findViewById(R.id.menu);
        logo = findViewById(R.id.logo);
        cerveza = findViewById(R.id.cerveza);
        boton = findViewById(R.id.boton);
        texto = findViewById(R.id.texto);

        // Evento al presionar el botón
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.setText("¡Salud! 🍻"); // Cambia el texto al hacer clic

                // Abrir la nueva actividad AddJugadores
                Intent intent = new Intent(PaginaPrincipal.this, CrearSalaUnirse.class);
                startActivity(intent);
            }
        });
    }
}
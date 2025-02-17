package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class PaginaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial); // Asegúrate de que este archivo XML sea el correcto

        // Referencia al botón
        Button boton = findViewById(R.id.boton);

        // Funcionalidad del botón para ir a AddJugadores
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaginaPrincipal.this, CrearSalaUnirse.class);
                startActivity(intent);
            }
        });

    }
}

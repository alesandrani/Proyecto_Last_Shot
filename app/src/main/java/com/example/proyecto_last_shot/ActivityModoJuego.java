package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityModoJuego extends AppCompatActivity {

    public class CrearSalaUnirse extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_modo_juego);


            ImageView btnBack = findViewById(R.id.btn_back);
            LinearLayout monodisp = findViewById(R.id.monodisp);
            LinearLayout multidisp = findViewById(R.id.multidisp);


            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to PaginaPrincipal
                    Intent intent = new Intent(CrearSalaUnirse.this, PaginaPrincipal.class);
                    startActivity(intent);
                    finish();
                }
            });

            monodisp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to AddJugadores
                    Intent intent = new Intent(CrearSalaUnirse.this, PaginaAddJugadores.class);
                    startActivity(intent);
                }
            });

            multidisp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CrearSalaUnirse.this, CrearSalaUnirse.class);
                    startActivity(intent);
                }
            });


            ImageView btnCrearSala = findViewById(R.id.btnCrearSala);
            ImageView btnUnirseSala = findViewById(R.id.btnUnirseSala);

            btnCrearSala.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CrearSalaUnirse.this, PaginaAddJugadores.class);
                    startActivity(intent);
                }
            });

            btnUnirseSala.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CrearSalaUnirse.this, CrearSalaUnirse.class);
                    startActivity(intent);
                }
            });
        }
    }}
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

import java.util.Random;

public class PaginaJuegos extends AppCompatActivity {
    private ImageView btnBack;
    private LinearLayout btnMechero, btnVerdadReto, btnNumeroMaestro, btnJuegoRandom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_juegos);

        // Referencias a los elementos del layout
        btnBack = findViewById(R.id.btn_back);
        btnMechero = findViewById(R.id.btn_mechero);
        btnVerdadReto = findViewById(R.id.btn_verdad_reto);
        btnNumeroMaestro = findViewById(R.id.btn_numero_maestro);
        btnJuegoRandom = findViewById(R.id.btn_juego_random);


        // Configurar eventos de clic
        btnBack.setOnClickListener(view -> finish()); // Cierra la actividad y vuelve atrás

        btnMechero.setOnClickListener(view -> {
            Intent intent = new Intent(PaginaJuegos.this, ActivityJuegoMoneda.class);
            startActivity(intent);
        });

        btnVerdadReto.setOnClickListener(view -> {
            Intent intent = new Intent(PaginaJuegos.this, VerdadReto.class);
            startActivity(intent);
        });

        btnNumeroMaestro.setOnClickListener(view -> {
            Intent intent = new Intent(PaginaJuegos.this, NumeroMaestro.class);
            startActivity(intent);
        });

        btnJuegoRandom.setOnClickListener(view -> {
            // Generar número aleatorio entre 0 y 2
            int randomGame = new Random().nextInt(3);

            Class<?> selectedActivity;
            switch (randomGame) {
                case 0:
                    selectedActivity = ActivityJuegoMoneda.class;
                    break;
                case 1:
                    selectedActivity = VerdadReto.class;
                    break;
                case 2:
                default:
                    selectedActivity = NumeroMaestro.class;
                    break;
            }

            Intent intent = new Intent(PaginaJuegos.this, selectedActivity);
            startActivity(intent);
        });
    }
}

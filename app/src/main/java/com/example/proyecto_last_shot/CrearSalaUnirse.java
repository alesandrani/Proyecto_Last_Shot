package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase CrearSalaUnirse que permite a los usuarios crear o unirse a una sala.
 * La actividad presenta dos botones: uno para crear una nueva sala y otro para unirse a una existente.
 */
public class CrearSalaUnirse extends AppCompatActivity {

    private ImageView btnCrearSala; // Botón para crear una sala
    private ImageView btnUnirseSala; // Botón para unirse a una sala
    private ImageView btnBack; // Botón de retroceso para volver atrás

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_crear_unirse);  // Establece el layout asociado a la actividad

        // Inicialización de vistas
        btnBack = findViewById(R.id.btn_back);
        btnCrearSala = findViewById(R.id.btnCrearSala);
        btnUnirseSala = findViewById(R.id.btnUnirseSala);

        // Configuración del botón de retroceso
        btnBack.setOnClickListener(v -> finish());

        // Configuración del botón para crear sala
        btnCrearSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad PaginaCrearSala
                Intent intentCrearSala = new Intent(CrearSalaUnirse.this, PaginaCrearSala.class);
                startActivity(intentCrearSala);
            }
        });

        // Configuración del botón para unirse a una sala
        btnUnirseSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad PaginaUnirseSala
                Intent intentUnirseSala = new Intent(CrearSalaUnirse.this, PaginaUnirseSala.class);
                startActivity(intentUnirseSala);
            }
        });
    }
}

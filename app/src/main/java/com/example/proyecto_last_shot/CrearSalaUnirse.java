package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CrearSalaUnirse extends AppCompatActivity {

    private ImageView btnCrearSala;
    private ImageView btnUnirseSala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_crear_unirse);  // Asegúrate de que el layout tenga los botones

        btnCrearSala = findViewById(R.id.btnCrearSala);
        btnUnirseSala = findViewById(R.id.btnUnirseSala);

        // Configurar el botón para crear sala
        btnCrearSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a PaginaCrearSala
                Intent intentCrearSala = new Intent(CrearSalaUnirse.this, PaginaCrearSala.class);
                startActivity(intentCrearSala);
            }
        });

        // Configurar el botón para unirse a una sala
        btnUnirseSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a PaginaUnirseSala
                Intent intentUnirseSala = new Intent(CrearSalaUnirse.this, PaginaUnirseSalaActivity.class);
                startActivity(intentUnirseSala);
            }
        });
    }
}

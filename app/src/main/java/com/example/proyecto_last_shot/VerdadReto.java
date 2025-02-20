package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class VerdadReto extends AppCompatActivity {
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.verdad_reto);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> finish());
        // Configuración para el botón RETO
        findViewById(R.id.btn_reto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad RetoActivity
                Intent intent = new Intent(VerdadReto.this, RetoActivity.class);
                startActivity(intent);
            }
        });

        // Configuración para el botón VERDAD
        findViewById(R.id.btn_verdad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad VerdadActivity
                Intent intent = new Intent(VerdadReto.this, VerdadActivity.class);
                startActivity(intent);
            }
        });

    }
}
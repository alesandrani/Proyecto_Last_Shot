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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddJugadores extends AppCompatActivity {
    private EditText cuadroTexto;
    private LinearLayout container; // Contenedor donde se agregarán los nombres
    private ImageButton btnAddJugador, btnNext;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_add_jugadores);

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
                    TextView nuevoJugador = new TextView(AddJugadores.this);
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
                Intent intent = new Intent(AddJugadores.this, PaginaJuegos.class);
                startActivity(intent);
            }
        });

        // Botón para volver atrás
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class PaginaCrearSala extends AppCompatActivity {

    private EditText nombreSala, inputCodigoSala, inputNombreJugador;
    private ImageView btnCrearSala;
    private String claveGenerada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala);

        nombreSala = findViewById(R.id.nombreSala);
        inputCodigoSala = findViewById(R.id.input_codigo_sala);
        inputNombreJugador = findViewById(R.id.input_nombre_jugador);
        btnCrearSala = findViewById(R.id.btn_crearSala);

        // Generar clave automática al abrir la actividad
        claveGenerada = generarClave();
        inputCodigoSala.setText(claveGenerada);

        btnCrearSala.setOnClickListener(view -> crearSala());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Generar una nueva clave cada vez que el usuario regrese a la pantalla
        claveGenerada = generarClave();
        inputCodigoSala.setText(claveGenerada);
    }

    private String generarClave() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder clave = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            clave.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return clave.toString();
    }

    private void crearSala() {
        String nombreDeSala = nombreSala.getText().toString().trim();
        String nombreJugador = inputNombreJugador.getText().toString().trim();

        if (nombreDeSala.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un nombre para la sala", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nombreJugador.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simular creación de sala (sin Firebase)
        String salaId = generarClave(); // Generamos un ID de sala temporal

        // Crear un Intent y pasar los datos a la siguiente actividad
        iniciarPaginaJuegos(salaId, nombreJugador);
    }

    // Método para iniciar la actividad PaginaJuegos
    private void iniciarPaginaJuegos(String salaId, String nombreJugador) {
        if (claveGenerada != null && !claveGenerada.isEmpty() && salaId != null && nombreJugador != null && !nombreJugador.isEmpty()) {
            // Intent a PaginaJuegos
            Intent intent = new Intent(PaginaCrearSala.this, PaginaJuegos.class);
            intent.putExtra("claveSala", claveGenerada);
            intent.putExtra("nombreSala", salaId);
            intent.putExtra("jugadores", nombreJugador);
            startActivity(intent);
            finish(); // Cerrar la actividad actual
        } else {
            Log.e("Error", "Error al pasar los datos al Intent");
            Toast.makeText(this, "Error al pasar los datos", Toast.LENGTH_SHORT).show();
        }
    }
}

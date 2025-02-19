package com.example.proyecto_last_shot;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaginaCrearSala extends AppCompatActivity {
    private EditText nombreSala, inputCodigoSala, inputNombreJugador;
    private ImageView btnCrearSala, btnBack;
    private String claveGenerada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala);

        nombreSala = findViewById(R.id.nombreSala);
        inputCodigoSala = findViewById(R.id.input_codigo_sala);
        inputNombreJugador = findViewById(R.id.input_nombre_jugador);
        btnCrearSala = findViewById(R.id.btn_crearSala);
        btnBack = findViewById(R.id.btn_back); // Inicializa el botón de retroceso

        // Generar clave automática al abrir la actividad
        claveGenerada = generarClave();
        inputCodigoSala.setText(claveGenerada);

        // Configurar el clic del botón de crear sala
        btnCrearSala.setOnClickListener(view -> crearSala());

        // Configurar el clic del botón de retroceso
        btnBack.setOnClickListener(view -> mostrarDialogoConfirmacion());
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

        // Instancia de Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un ID único para la sala (FireStore lo hace automáticamente)
        String salaId = db.collection("salas").document().getId();

        // Crear un mapa con la información de la sala
        Map<String, Object> sala = new HashMap<>();
        sala.put("nombre", nombreDeSala);
        sala.put("clave", claveGenerada);
        sala.put("maxJugadores", 10); // Establecer máximo de jugadores
        sala.put("jugadores", null);  // Puede ser una lista vacía o null al principio

        // Guardar la sala en Firebase Firestore
        db.collection("salas").document(salaId).set(sala)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "✅ Sala creada correctamente en Firestore");

                    // Crear el Intent y pasar los datos a la siguiente actividad
                    iniciarPaginaJuegos(salaId, nombreJugador);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "❌ Error al crear la sala en Firestore", e);
                    Toast.makeText(PaginaCrearSala.this, "Error al crear la sala", Toast.LENGTH_SHORT).show();
                });
    }

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

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle("Salir")
                .setMessage("¿Estás seguro de que quieres salir de la creación de la sala?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    finish(); // Cerrar la actividad actual
                })
                .setNegativeButton(android.R.string.no, null) // Si el usuario cancela, no hace nada
                .show();
    }
}

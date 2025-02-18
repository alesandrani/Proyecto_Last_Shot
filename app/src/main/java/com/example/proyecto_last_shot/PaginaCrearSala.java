package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Random;

public class PaginaCrearSala extends AppCompatActivity {

    private EditText nombreSala, inputCodigoSala, inputNombreJugador;
    private ImageView btnCrearSala;
    private DatabaseReference salaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala);

        // Referencias a los elementos de la UI
        nombreSala = findViewById(R.id.nombreSala);
        inputCodigoSala = findViewById(R.id.input_codigo_sala);
        inputNombreJugador = findViewById(R.id.input_nombre_jugador);
        btnCrearSala = findViewById(R.id.btn_crearSala);

        // Inicializar referencia a Firebase
        salaRef = FirebaseDatabase.getInstance().getReference("salas");

        // Configurar el listener del botón
        btnCrearSala.setOnClickListener(view -> crearSala());
    }

    // Método para generar una clave aleatoria de 6 caracteres
    private String generarClave() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder clave = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            clave.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return clave.toString();
    }

    // Método para crear una sala y redirigir a la página de juegos
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

        // Generar clave de sala
        String claveGenerada = generarClave();
        inputCodigoSala.setText(claveGenerada); // Mostramos el código en el campo de texto

        // Crear un ID único para la sala
        String salaId = salaRef.push().getKey();

        // Verificar que el ID no sea nulo antes de continuar
        if (salaId == null) {
            Toast.makeText(this, "Error al generar la sala", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un HashMap con los datos de la sala
        HashMap<String, Object> sala = new HashMap<>();
        sala.put("nombre", nombreDeSala);
        sala.put("clave", claveGenerada);
        sala.put("jugadores", new HashMap<String, Object>()); // Inicialmente sin jugadores

        // Guardar en Firebase
        salaRef.child(salaId).setValue(sala).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Sala creada exitosamente con ID: " + salaId);
                Toast.makeText(this, "Sala creada con éxito. Código: " + claveGenerada, Toast.LENGTH_LONG).show();

                // Lanzar la nueva actividad PaginaJuegos con la clave de la sala
                Intent intent = new Intent(PaginaCrearSala.this, PaginaJuegos.class);
                intent.putExtra("clave_sala", claveGenerada);
                intent.putExtra("sala_id", salaId);
                intent.putExtra("nombre_jugador", nombreJugador);
                startActivity(intent);
                finish(); // Opcional: cerrar esta actividad
            } else {
                Log.e("Firebase", "Error al crear la sala", task.getException());
                Toast.makeText(this, "Error al crear la sala", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

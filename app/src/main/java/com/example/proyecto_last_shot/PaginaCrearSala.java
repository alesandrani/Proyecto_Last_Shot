package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Random;

public class PaginaCrearSala extends AppCompatActivity {

    private EditText nombreSala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala);

        nombreSala = findViewById(R.id.nombreSala); // EditText para el nombre de la sala
    }

    // Metodo para generar una clave aleatoria
    public String generarClave() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder clave = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            clave.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return clave.toString();
    }

    // Metodo para crear la sala
    public void crearSala(View view) {
        String nombreSala = nombreSala.getText().toString().trim();

        // Verificar que el nombre de la sala no esté vacío
        if (nombreSala.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre para la sala", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generar la clave aleatoria
        String claveGenerada = generarClave();

        // Obtener una referencia a la base de datos de Firebase
        DatabaseReference salaRef = FirebaseDatabase.getInstance().getReference("salas");

        // Crear un ID único para la sala
        String salaId = salaRef.push().getKey();  // Genera un ID único para la sala

        // Crear un HashMap con los datos de la sala
        HashMap<String, Object> sala = new HashMap<>();
        sala.put("nombre", nombreSala);  // El nombre de la sala proporcionado por el host
        sala.put("clave", claveGenerada);  // La clave generada automáticamente
        sala.put("jugadores", new HashMap<>());  // Inicialmente no hay jugadores

        // Guardar la sala en Firebase
        assert salaId != null;
        salaRef.child(salaId).setValue(sala)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Sala creada exitosamente con ID: " + salaId);
                        Toast.makeText(PaginaCrearSala.this, "Sala creada con éxito. ID: " + salaId + " y clave: " + claveGenerada, Toast.LENGTH_LONG).show();
                        // Aquí podrías redirigir al host a la actividad donde se verán los jugadores.
                    } else {
                        Log.e("Firebase", "Error al crear la sala", task.getException());
                        Toast.makeText(PaginaCrearSala.this, "Error al crear la sala", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

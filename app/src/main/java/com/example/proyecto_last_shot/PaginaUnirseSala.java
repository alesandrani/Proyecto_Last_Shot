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

public class PaginaUnirseSalaActivity extends AppCompatActivity {

    private EditText salaId, clave, nombreJugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unirse_sala);

        salaId = findViewById(R.id.salaId);
        clave = findViewById(R.id.clave);
        nombreJugador = findViewById(R.id.nombreJugador);
    }

    // Método para unirse a la sala
    public void unirseASala(View view) {
        String salaId = salaId.getText().toString().trim();
        String clave = clave.getText().toString().trim();
        String nombreJugador = nombreJugador.getText().toString().trim();

        if (salaId.isEmpty() || clave.isEmpty() || nombreJugador.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener una referencia a la base de datos de Firebase
        DatabaseReference salaRef = FirebaseDatabase.getInstance().getReference("salas").child(salaId);

        // Comprobar si la sala existe y si la clave es correcta
        salaRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    String claveCorrecta = task.getResult().child("clave").getValue(String.class);
                    if (clave.equals(claveCorrecta)) {
                        // La clave es correcta, agregar el jugador
                        DatabaseReference jugadoresRef = salaRef.child("jugadores");
                        String jugadorId = jugadoresRef.push().getKey();  // Crear un ID único para el jugador
                        HashMap<String, String> jugador = new HashMap<>();
                        jugador.put("nombre", nombreJugador);

                        jugadoresRef.child(jugadorId).setValue(jugador)
                                .addOnCompleteListener(joinTask -> {
                                    if (joinTask.isSuccessful()) {
                                        Log.d("Firebase", "Jugador unido a la sala con éxito.");
                                        Toast.makeText(PaginaUnirseSalaActivity.this, "¡Te has unido a la sala!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PaginaUnirseSalaActivity.this, "Error al unirse a la sala", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Clave incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "La sala no existe", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("Firebase", "Error al acceder a la sala", task.getException());
                Toast.makeText(this, "Error al acceder a la sala", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.proyecto_last_shot;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NumeroMaestro extends AppCompatActivity {

    private TextView playerNameTextView, playerScoreTextView, loserNameTextView;
    private ImageView btnBack;
    private DatabaseReference salaRef;
    private String salaId = "ID_DE_TU_SALA"; // Reemplázalo dinámicamente cuando la sala se cree

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_maestro);

        playerNameTextView = findViewById(R.id.playerName);
        playerScoreTextView = findViewById(R.id.playerScore);
        loserNameTextView = findViewById(R.id.loserName);
        btnBack = findViewById(R.id.btn_back);

        // Conectar con Firebase
        salaRef = FirebaseDatabase.getInstance().getReference("salas").child(salaId);

        btnBack.setOnClickListener(v -> finish()); // Cerrar actividad al pulsar el botón

        asignarNumerosAJugadores();
    }

    private void asignarNumerosAJugadores() {
        salaRef.child("jugadores").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                Map<String, Integer> jugadoresConNumeros = new HashMap<>();
                String perdedor = "";
                int minNumero = Integer.MAX_VALUE;

                // Asignar números aleatorios a los jugadores
                for (DataSnapshot jugadorSnapshot : snapshot.getChildren()) {
                    String nombreJugador = jugadorSnapshot.child("nombre").getValue(String.class);
                    int numeroAleatorio = new Random().nextInt(20) + 1;  // Número entre 1 y 20

                    // Guardar los números generados
                    jugadoresConNumeros.put(nombreJugador, numeroAleatorio);

                    // Verificar cuál es el perdedor (el jugador con el número más bajo)
                    if (numeroAleatorio < minNumero) {
                        minNumero = numeroAleatorio;
                        perdedor = nombreJugador;
                    }
                }

                // Obtener el primer jugador registrado y mostrarlo
                if (!jugadoresConNumeros.isEmpty()) {
                    Map.Entry<String, Integer> primerJugador = jugadoresConNumeros.entrySet().iterator().next();
                    playerNameTextView.setText(primerJugador.getKey());
                    mostrarNumeroAnimado(primerJugador.getValue());  // Mostrar el número animado

                    // Mostrar el perdedor en la UI
                    loserNameTextView.setText(perdedor);

                    // Guardar los números de los jugadores en Firebase
                    salaRef.child("resultados").setValue(jugadoresConNumeros);
                }

            } else {
                Toast.makeText(NumeroMaestro.this, "Error al obtener jugadores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarNumeroAnimado(int numero) {
        // Aquí animamos el cambio de texto en playerScoreTextView con un ObjectAnimator
        ObjectAnimator anim = ObjectAnimator.ofInt(playerScoreTextView, "text", 1, numero);
        anim.setDuration(4000); // Duración de la animación
        anim.start();
    }
}

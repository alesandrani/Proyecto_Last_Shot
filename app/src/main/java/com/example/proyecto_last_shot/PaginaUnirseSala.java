package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PaginaUnirseSala extends AppCompatActivity {

    private EditText salaNombre, clave, nombreJugador;
    private ImageView btnUnirseSala;
    private DatabaseReference salaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unirse_sala);

        salaNombre = findViewById(R.id.salaNombre);
        clave = findViewById(R.id.clave);
        nombreJugador = findViewById(R.id.nombreJugador);
        btnUnirseSala = findViewById(R.id.btn_unirseSala);

        salaRef = FirebaseDatabase.getInstance().getReference("salas");

        btnUnirseSala.setOnClickListener(view -> unirseASala());
    }

    private void unirseASala() {
        String nombreSala = salaNombre.getText().toString().trim();
        String claveIngresada = clave.getText().toString().trim();
        String nombreJugadorIngresado = nombreJugador.getText().toString().trim();

        if (nombreSala.isEmpty() || claveIngresada.isEmpty() || nombreJugadorIngresado.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        salaRef.orderByChild("clave").equalTo(claveIngresada).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String salaId = snapshot.getKey();
                        String nombreRealSala = snapshot.child("nombre").getValue(String.class);

                        if (nombreRealSala != null && nombreRealSala.equals(nombreSala)) {
                            DatabaseReference jugadoresRef = salaRef.child(salaId).child("jugadores");
                            String jugadorId = jugadoresRef.push().getKey();
                            HashMap<String, String> jugador = new HashMap<>();
                            jugador.put("nombre", nombreJugadorIngresado);

                            jugadoresRef.child(jugadorId).setValue(jugador).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PaginaUnirseSala.this, "¡Te has unido a la sala!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PaginaUnirseSala.this, PaginaJuegos.class);
                                    intent.putExtra("clave_sala", claveIngresada);
                                    intent.putExtra("sala_id", salaId);
                                    intent.putExtra("nombre_jugador", nombreJugadorIngresado);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(PaginaUnirseSala.this, "Error al unirse a la sala", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                    }
                    Toast.makeText(PaginaUnirseSala.this, "El nombre de la sala no coincide con la clave ingresada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaginaUnirseSala.this, "La clave ingresada no corresponde a ninguna sala", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al acceder a Firebase", databaseError.toException());
                Toast.makeText(PaginaUnirseSala.this, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

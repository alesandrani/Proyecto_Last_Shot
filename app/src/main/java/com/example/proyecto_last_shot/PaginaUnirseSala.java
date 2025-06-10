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

public class PaginaUnirseSala extends AppCompatActivity {


    private static final String TAG = "UnirseSala";

    private EditText inputCodigoSala, inputNombreJugador;
    private ImageView btnUnirseSala, btnBack;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unirse_sala);

        inputCodigoSala    = findViewById(R.id.clave);
        inputNombreJugador = findViewById(R.id.nombreJugador);
        btnUnirseSala      = findViewById(R.id.btn_unirseSala);
        btnBack            = findViewById(R.id.btn_back);

        // Apunta al nodo raíz "salas"
        rootRef = FirebaseDatabase
                .getInstance("https://lastshot-proyecto-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("salas");

        btnUnirseSala.setOnClickListener(v -> unirseASala());
        btnBack.setOnClickListener(v -> finish());
    }

    private void unirseASala() {
        String codigoSala = inputCodigoSala.getText().toString().trim();
        String nombreJugador = inputNombreJugador.getText().toString().trim();

        if (codigoSala.isEmpty() || nombreJugador.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Intentando unir a sala: " + codigoSala + " jugador: " + nombreJugador);

        // Comprueba si existe /salas/{codigoSala}
        rootRef.child(codigoSala)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapSala) {
                        if (!snapSala.exists()) {
                            Toast.makeText(PaginaUnirseSala.this,
                                    "La sala \"" + codigoSala + "\" no existe",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Agregar al jugador bajo /salas/{codigoSala}/jugadores/{nombreJugador}: true
                        DatabaseReference jugadoresRef = rootRef
                                .child(codigoSala)
                                .child("jugadores")
                                .child(nombreJugador);
                        jugadoresRef.setValue(true)
                                .addOnSuccessListener(__ -> {
                                    Toast.makeText(PaginaUnirseSala.this,
                                            "¡Te has unido a la sala!",
                                            Toast.LENGTH_SHORT).show();
                                    // Lanzar PaginaJuegos
                                    Intent i = new Intent(PaginaUnirseSala.this, PaginaJuegos.class);
                                    i.putExtra("nombreJugadorActual", nombreJugador);
                                    i.putExtra("idSala", codigoSala);
                                    startActivity(i);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error al unirse", e);
                                    Toast.makeText(PaginaUnirseSala.this,
                                            "Error al unirse a la sala",
                                            Toast.LENGTH_SHORT).show();
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError err) {
                        Log.e(TAG, "Firebase error", err.toException());
                        Toast.makeText(PaginaUnirseSala.this,
                                "Error de base de datos",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

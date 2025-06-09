package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

/**
 * Actividad que permite crear una nueva sala, registrar el primer jugador y
 * guardar los datos en Firebase.
 */
public class PaginaCrearSala extends AppCompatActivity {

    private static final String TAG = "CrearSala";

    private EditText inputCodigoSala, inputNombreSala, inputNombreJugador;
    private ImageView btnCrearSala, btnBack;

    private FirebaseDatabase database;
    private DatabaseReference salasRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala);

        // Inicializar referencias UI
        inputCodigoSala    = findViewById(R.id.input_codigo_sala);
        inputNombreSala    = findViewById(R.id.nombreSala);
        inputNombreJugador = findViewById(R.id.input_nombre_jugador);
        btnCrearSala       = findViewById(R.id.btn_crearSala);
        btnBack            = findViewById(R.id.btn_back);

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance("https://lastshot-proyecto-default-rtdb.europe-west1.firebasedatabase.app");
        salasRef = database.getReference("salas");

        btnBack.setOnClickListener(v -> finish());

        btnCrearSala.setOnClickListener(view -> {
            Log.d(TAG, "Botón crear sala presionado");

            String codigoSala = inputCodigoSala.getText().toString().trim();
            String nombreSala = inputNombreSala.getText().toString().trim();
            String nombreJugador = inputNombreJugador.getText().toString().trim();

            if (codigoSala.isEmpty() || nombreSala.isEmpty() || nombreJugador.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            crearSalaEnFirebase(codigoSala, nombreSala, nombreJugador);
        });
    }

    /**
     * Crea una nueva sala en Firebase con los datos introducidos y añade al primer
     * jugador como host. Si la operación es exitosa, navega a la pantalla de juegos.
     */
    private void crearSalaEnFirebase(String codigoSala, String nombreSala, String nombreJugador) {
        String idSala = UUID.randomUUID().toString();

        // Datos de la sala
        HashMap<String, Object> datosSala = new HashMap<>();
        datosSala.put("codigo", codigoSala);
        datosSala.put("nombreSala", nombreSala);

        salasRef.child(idSala).setValue(datosSala)
                .addOnSuccessListener(unused -> {
                    // Añadir jugador host
                    DatabaseReference jugadoresRef = salasRef.child(idSala).child("jugadores");
                    String idJugador = UUID.randomUUID().toString();

                    HashMap<String, Object> datosJugador = new HashMap<>();
                    datosJugador.put("nombre", nombreJugador);
                    datosJugador.put("host", true);

                    jugadoresRef.child(idJugador).setValue(datosJugador)
                            .addOnSuccessListener(unused2 -> {
                                // Navegar a la siguiente pantalla
                                Intent intent = new Intent(PaginaCrearSala.this, PaginaJuegos.class);
                                intent.putExtra("claveGenerada", codigoSala);
                                intent.putExtra("nombreInternoSala", idSala);
                                intent.putExtra("nombreJugador", nombreJugador);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error al registrar jugador", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Error al guardar jugador: ", e);
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al crear sala", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error al guardar sala: ", e);
                });
    }
}

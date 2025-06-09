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

public class PaginaCrearSala extends AppCompatActivity {

    private static final String TAG = "CrearSala";
    private static final String RTDB_URL =
            "https://lastshot-proyecto-default-rtdb.europe-west1.firebasedatabase.app";

    private EditText inputCodigoSala, inputNombreSala, inputNombreJugador;
    private ImageView btnCrearSala, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_sala);

        // Referencias UI
        inputCodigoSala   = findViewById(R.id.input_codigo_sala);
        inputNombreSala   = findViewById(R.id.nombreSala);
        inputNombreJugador= findViewById(R.id.input_nombre_jugador);
        btnCrearSala      = findViewById(R.id.btn_crearSala);
        btnBack           = findViewById(R.id.btn_back);

        btnCrearSala.setOnClickListener(v -> {
            Log.d(TAG, "Botón crear sala presionado");
            crearSala();
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void crearSala() {
        String codigoSala   = inputCodigoSala.getText().toString().trim();
        String nombreSala   = inputNombreSala.getText().toString().trim();
        String nombreJugador= inputNombreJugador.getText().toString().trim();

        if (codigoSala.isEmpty() || nombreSala.isEmpty() || nombreJugador.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Código sala: " + codigoSala);
        Log.d(TAG, "Nombre sala: " + nombreSala);
        Log.d(TAG, "Jugador: " + nombreJugador);

        //Obtén la instancia apuntando a TU URL de Realtime DB
        FirebaseDatabase database = FirebaseDatabase.getInstance(RTDB_URL);

        DatabaseReference salaRef = database
                .getReference("salas")
                .child(codigoSala);

        // Datos de la sala
        HashMap<String, Object> datosSala = new HashMap<>();
        datosSala.put("info", new SalaInfo(nombreSala, nombreJugador));

        // Jugadores
        HashMap<String, Object> jugadores = new HashMap<>();
        jugadores.put(nombreJugador, true);
        datosSala.put("jugadores", jugadores);

        salaRef.setValue(datosSala)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Sala creada correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PaginaJuegos.class);
                    intent.putExtra("clave", codigoSala);
                    intent.putExtra("nombreSala", nombreSala);
                    intent.putExtra("nombreJugador", nombreJugador);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al crear sala", e);
                    Toast.makeText(this, "Error al crear la sala", Toast.LENGTH_SHORT).show();
                });
    }

    /** Contiene nombre y creador para /salas/{codigo}/info */
    public static class SalaInfo {
        public String nombre;
        public String creador;
        public SalaInfo() {}
        public SalaInfo(String nombre, String creador) {
            this.nombre = nombre;
            this.creador = creador;
        }
    }
}

package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

  private EditText inputCodigoSala, inputNombreSala, inputNombreJugador;
  private ImageView btnCrearSala, btnBack;

  private FirebaseDatabase database;
  private DatabaseReference salasRef;

  private static final String TAG = "CrearSala";

  /**
   * Método que se ejecuta al crear la actividad.
   * Inicializa los elementos de la interfaz, la base de datos y configura los
   * eventos de los botones.
   * 
   * @param savedInstanceState Estado guardado de la actividad.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.crear_sala);

    inputCodigoSala = findViewById(R.id.input_codigo_sala);
    inputNombreSala = findViewById(R.id.nombreSala);
    inputNombreJugador = findViewById(R.id.input_nombre_jugador);
    btnCrearSala = findViewById(R.id.btn_crearSala);
    btnBack = findViewById(R.id.btn_back);

    database = FirebaseDatabase.getInstance();
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
   * jugador como host.
   * Si la operación es exitosa, navega a la pantalla de juegos.
   * 
   * @param codigoSala    Código de la sala introducido por el usuario.
   * @param nombreSala    Nombre de la sala introducido por el usuario.
   * @param nombreJugador Nombre del primer jugador (host).
   */
  private void crearSalaEnFirebase(String codigoSala, String nombreSala, String nombreJugador) {
    String idSala = UUID.randomUUID().toString();

    // Datos de la sala como mapa (para Firebase)
    HashMap<String, Object> datosSala = new HashMap<>();
    datosSala.put("codigo", codigoSala);
    datosSala.put("nombreSala", nombreSala);

    // Creamos un nodo hijo con el ID generado
    salasRef.child(idSala).setValue(datosSala)
        .addOnSuccessListener(unused -> {
          // Añadimos al jugador como primer miembro
          DatabaseReference jugadoresRef = salasRef.child(idSala).child("jugadores");
          String idJugador = UUID.randomUUID().toString();

          HashMap<String, Object> datosJugador = new HashMap<>();
          datosJugador.put("nombre", nombreJugador);
          datosJugador.put("host", true);

          jugadoresRef.child(idJugador).setValue(datosJugador)
              .addOnSuccessListener(unused2 -> {
                // Ir a la siguiente actividad con los datos
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

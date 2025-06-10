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
                    @Override public void onDataChange(@NonNull DataSnapshot snapSala) {
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
                    @Override public void onCancelled(@NonNull DatabaseError err) {
                        Log.e(TAG, "Firebase error", err.toException());
                        Toast.makeText(PaginaUnirseSala.this,
                                "Error de base de datos",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
/**
 * Actividad que permite a un usuario unirse a una sala existente introduciendo
 * el nombre de la sala,
 * la clave y su nombre de jugador. Valida los datos y gestiona la lógica de
 * acceso a la sala en Firebase.
 */
public class PaginaUnirseSala extends AppCompatActivity {

  private EditText salaNombre, clave, nombreJugador;
  private ImageView btnUnirseSala, btnBack;
  private DatabaseReference salaRef;

  /**
   * Método que se ejecuta al crear la actividad.
   * Inicializa las vistas, la referencia a la base de datos y configura los
   * listeners de los botones.
   * 
   * @param savedInstanceState Estado guardado de la actividad.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.unirse_sala);

    salaNombre = findViewById(R.id.salaNombre);
    clave = findViewById(R.id.clave);
    nombreJugador = findViewById(R.id.nombreJugador);
    btnUnirseSala = findViewById(R.id.btn_unirseSala);
    btnBack = findViewById(R.id.btn_back);

    salaRef = FirebaseDatabase.getInstance().getReference("salas");

    btnUnirseSala.setOnClickListener(view -> unirseASala());
    btnBack.setOnClickListener(v -> finish());
  }

  /**
   * Intenta unir al usuario a una sala existente en Firebase usando la clave y el
   * nombre de la sala.
   * Valida los campos, verifica la existencia de la sala y añade al jugador si es
   * posible.
   */
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
            Long maxJugadores = snapshot.child("maxJugadores").getValue(Long.class);
            Long currentJugadores = snapshot.child("jugadores").getChildrenCount();

            if (currentJugadores < maxJugadores) {

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
            } else {
              Toast.makeText(PaginaUnirseSala.this, "La sala está llena", Toast.LENGTH_SHORT).show();
            }
            return;
          }
        } else {
          Toast.makeText(PaginaUnirseSala.this, "La clave ingresada no corresponde a ninguna sala", Toast.LENGTH_SHORT)
              .show();
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

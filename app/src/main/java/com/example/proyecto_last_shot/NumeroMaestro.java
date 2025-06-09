package com.example.proyecto_last_shot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NumeroMaestro extends AppCompatActivity {
  private TextView playerNameTextView;
  private TextView nombrePerdedorTextView;
  private ImageView btnBack;
  private ImageButton startButton;
  private ArrayList<String> listaJugadores;
  private WheelView wheelView;

  private Map<String, Integer> jugadoresConNumeros;

  /**
   * Método llamado al crear la actividad. Inicializa las vistas, recupera la
   * lista de jugadores, configura los listeners para el botón de girar y la ruleta,
   * y gestiona la visualización de los resultados.
   *
   * @param savedInstanceState Estado previamente guardado de la actividad, si existe.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.numero_maestro);

    playerNameTextView = findViewById(R.id.tvNumeroJugador);
    nombrePerdedorTextView = findViewById(R.id.nombrePerdedor);
    btnBack = findViewById(R.id.btn_back);
    startButton = findViewById(R.id.startButton);
    wheelView = findViewById(R.id.wheelView);

    btnBack.setOnClickListener(v -> finish());

    listaJugadores = obtenerJugadoresDePreferencias();

    if (listaJugadores.isEmpty()) {
      Toast.makeText(this, "No hay jugadores disponibles", Toast.LENGTH_SHORT).show();
    }

    // Listener que detecta el número salido tras la animación de la rueda
    wheelView.setOnNumberSelectedListener(numero -> {
      if (jugadoresConNumeros != null) {
        int numeroGanador = Integer.parseInt(numero);
        String perdedor = obtenerPerdedorPorNumero(jugadoresConNumeros, numeroGanador);
        mostrarJugadores(jugadoresConNumeros, numeroGanador);
        mostrarPerdedor(perdedor);
      }
    });

    startButton.setOnClickListener(v -> {
      Animation anim = AnimationUtils.loadAnimation(NumeroMaestro.this, R.anim.spin_bounce);
      startButton.startAnimation(anim);

      if (!listaJugadores.isEmpty()) {
        jugadoresConNumeros = asignarNumerosAleatorios(listaJugadores);
        wheelView.setNumerosDesdeLista(jugadoresConNumeros);
        mostrarJugadores(jugadoresConNumeros, -1); // Mostrar sin resaltar

        float currentAngle = wheelView.getRotationAngle();
        float randomAngle = (float) (Math.random() * 360 + 720);
        wheelView.rotateWheelWithAnimation(currentAngle, currentAngle + randomAngle);
      }
    });
  }

  private ArrayList<String> obtenerJugadoresDePreferencias() {
    SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
    Gson gson = new Gson();
    String json = prefs.getString("listaJugadores", null);
    Type type = new TypeToken<ArrayList<String>>() {}.getType();
    return json == null ? new ArrayList<>() : gson.fromJson(json, type);
  }

  private Map<String, Integer> asignarNumerosAleatorios(ArrayList<String> jugadores) {
    ArrayList<Integer> numeros = new ArrayList<>();
    for (int i = 1; i <= jugadores.size(); i++) {
      numeros.add(i);
    }
    Collections.shuffle(numeros);

    Map<String, Integer> asignacion = new HashMap<>();
    for (int i = 0; i < jugadores.size(); i++) {
      asignacion.put(jugadores.get(i), numeros.get(i));
    }
    return asignacion;
  }

  private void mostrarJugadores(Map<String, Integer> jugadoresConNumeros, int numeroResaltado) {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, Integer> entry : jugadoresConNumeros.entrySet()) {
      if (entry.getValue() == numeroResaltado) {
        builder.append("🎉 ");
      }
      builder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
    }
    playerNameTextView.setText(builder.toString().trim());
  }

  private void mostrarPerdedor(String perdedor) {
    if (perdedor.equals("Nadie")) {
      nombrePerdedorTextView.setText("");
    } else {
      nombrePerdedorTextView.setText("A BEBER: " + perdedor);
    }
  }

  private String obtenerPerdedorPorNumero(Map<String, Integer> jugadoresConNumeros, int numeroGanador) {
    for (Map.Entry<String, Integer> entry : jugadoresConNumeros.entrySet()) {
      if (entry.getValue() == numeroGanador) {
        return entry.getKey();
      }
    }
    return "Nadie";
  }
}

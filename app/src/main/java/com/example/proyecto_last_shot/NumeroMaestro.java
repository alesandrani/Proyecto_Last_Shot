package com.example.proyecto_last_shot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import java.util.Random;

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
   * lista de jugadores,
   * configura los listeners para el botón de girar y la ruleta, y gestiona la
   * visualización de los resultados.
   * 
   * @param savedInstanceState Estado previamente guardado de la actividad, si
   *                           existe.
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
        mostrarJugadores(jugadoresConNumeros, -1); // Mostrar sin resaltar

        float currentAngle = wheelView.getRotationAngle();
        // Gira entre 2 y 4 vueltas completas más un ángulo aleatorio para el resultado
        float randomAngle = (float) (Math.random() * 360 + 720);
        wheelView.rotateWheelWithAnimation(currentAngle, currentAngle + randomAngle);
      }
    });
  }

  /**
   * Recupera la lista de jugadores almacenada en las preferencias compartidas.
   * 
   * @return ArrayList con los nombres de los jugadores.
   */
  private ArrayList<String> obtenerJugadoresDePreferencias() {
    SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
    Gson gson = new Gson();
    String json = prefs.getString("listaJugadores", null);
    Type type = new TypeToken<ArrayList<String>>() {
    }.getType();
    return json == null ? new ArrayList<>() : gson.fromJson(json, type);
  }

  /**
   * Asigna números aleatorios a cada jugador de la lista.
   * 
   * @param jugadores Lista de nombres de jugadores.
   * @return Mapa con el nombre del jugador como clave y su número asignado como
   *         valor.
   */
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

  /**
   * Muestra la lista de jugadores junto a su número asignado. Si se ha
   * seleccionado un número ganador,
   * resalta al jugador correspondiente con un emoji.
   * 
   * @param jugadoresConNumeros Mapa de jugadores y sus números asignados.
   * @param numeroResaltado     Número que ha salido en la ruleta (se resalta el
   *                            jugador que lo tiene). Si es -1, no se resalta
   *                            ninguno.
   */
  private void mostrarJugadores(Map<String, Integer> jugadoresConNumeros, int numeroResaltado) {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, Integer> entry : jugadoresConNumeros.entrySet()) {
      if (entry.getValue() == numeroResaltado) {
        builder.append("\uD83C\uDF89 "); // Emoji para resaltar
      }
      builder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
    }
    playerNameTextView.setText(builder.toString().trim());
  }

  /**
   * Muestra el nombre del perdedor en el TextView correspondiente. Si no hay
   * perdedor, limpia el campo.
   * 
   * @param perdedor Nombre del jugador que ha perdido, o "Nadie" si no hay
   *                 perdedor.
   */
  private void mostrarPerdedor(String perdedor) {
    if (perdedor.equals("Nadie")) {
      nombrePerdedorTextView.setText("");
    } else {
      nombrePerdedorTextView.setText("A BEBER: " + perdedor);
    }
  }

  /**
   * Busca el nombre del jugador que tiene el número ganador.
   * 
   * @param jugadoresConNumeros Mapa de jugadores y sus números asignados.
   * @param numeroGanador       Número que ha salido en la ruleta.
   * @return Nombre del jugador que ha perdido, o "Nadie" si no hay coincidencia.
   */
  private String obtenerPerdedorPorNumero(Map<String, Integer> jugadoresConNumeros, int numeroGanador) {
    for (Map.Entry<String, Integer> entry : jugadoresConNumeros.entrySet()) {
      if (entry.getValue() == numeroGanador) {
        return entry.getKey();
      }
    }
    return "Nadie";
  }
}
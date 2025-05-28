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
            Toast.makeText(this, "Ha salido el número: " + numero, Toast.LENGTH_SHORT).show();
            if (jugadoresConNumeros != null) {
                String perdedor = obtenerPerdedorPorNumero(jugadoresConNumeros, Integer.parseInt(numero));
                mostrarJugadoresYPerdedor(jugadoresConNumeros, perdedor);
            }
        });

        startButton.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(NumeroMaestro.this, R.anim.spin_bounce);
            startButton.startAnimation(anim);

            if (!listaJugadores.isEmpty()) {
                jugadoresConNumeros = asignarNumerosAleatorios(listaJugadores);
                mostrarJugadores(jugadoresConNumeros);

                float currentAngle = wheelView.getRotationAngle();
                // Gira entre 2 y 4 vueltas completas más un ángulo aleatorio para el resultado
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

    private void mostrarJugadores(Map<String, Integer> jugadoresConNumeros) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : jugadoresConNumeros.entrySet()) {
            builder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }
        playerNameTextView.setText(builder.toString().trim());
        nombrePerdedorTextView.setText("");
    }

    private void mostrarJugadoresYPerdedor(Map<String, Integer> jugadoresConNumeros, String perdedor) {
        mostrarJugadores(jugadoresConNumeros);
        nombrePerdedorTextView.setText("A BEBER: " + perdedor);
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
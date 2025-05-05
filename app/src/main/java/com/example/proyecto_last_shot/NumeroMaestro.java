package com.example.proyecto_last_shot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
    private Button startButton;
    private ArrayList<String> listaJugadores;
    private WheelView wheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_maestro);

        playerNameTextView = findViewById(R.id.tvJugadores);
        nombrePerdedorTextView = findViewById(R.id.nombrePerdedor);
        btnBack = findViewById(R.id.btn_back);
        startButton = findViewById(R.id.startButton);
        wheelView = findViewById(R.id.wheelView);

        btnBack.setOnClickListener(v -> finish());

        // Obtener la lista de jugadores guardada
        listaJugadores = obtenerJugadoresDePreferencias();

        if (listaJugadores.isEmpty()) {
            Toast.makeText(this, "No hay jugadores disponibles", Toast.LENGTH_SHORT).show();
        }

        // Al pulsar el botón de girar
        startButton.setOnClickListener(v -> {
            // Animación al botón
            Animation anim = AnimationUtils.loadAnimation(NumeroMaestro.this, R.anim.spin_bounce);
            startButton.startAnimation(anim);

            if (!listaJugadores.isEmpty()) {
                // Asignar números aleatorios a jugadores y mostrar en pantalla
                Map<String, Integer> jugadoresConNumeros = asignarNumerosAleatorios(listaJugadores);
                mostrarJugadoresYPerdedor(jugadoresConNumeros);

                // Rotar la rueda
                float randomAngle = (float) (Math.random() * 360 + 360);
                wheelView.rotateWheelWithAnimation(wheelView.getRotationAngle(), wheelView.getRotationAngle() + randomAngle);
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

    private void mostrarJugadoresYPerdedor(Map<String, Integer> jugadoresConNumeros) {
        StringBuilder builder = new StringBuilder();
        String perdedor = "";
        int min = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : jugadoresConNumeros.entrySet()) {
            builder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
            if (entry.getValue() < min) {
                min = entry.getValue();
                perdedor = entry.getKey();
            }
        }

        playerNameTextView.setText(builder.toString().trim());
        nombrePerdedorTextView.setText("A BEBER: " + perdedor);
    }
}

package com.example.proyecto_last_shot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
    private ArrayList<String> listaJugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_maestro);

        playerNameTextView = findViewById(R.id.tvJugadores);
        nombrePerdedorTextView = findViewById(R.id.nombrePerdedor);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        listaJugadores = obtenerJugadoresDePreferencias();

        if (!listaJugadores.isEmpty()) {
            asignarNumerosYMostrar();
        } else {
            Toast.makeText(NumeroMaestro.this, "No hay jugadores disponibles", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> obtenerJugadoresDePreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("listaJugadores", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    private void asignarNumerosYMostrar() {
        ArrayList<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= listaJugadores.size(); i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros);

        Map<String, Integer> jugadoresConNumeros = new HashMap<>();
        StringBuilder jugadoresTexto = new StringBuilder();
        int minNumero = Integer.MAX_VALUE;
        String perdedor = "";

        for (int i = 0; i < listaJugadores.size(); i++) {
            String jugador = listaJugadores.get(i);
            int numero = numeros.get(i);
            jugadoresConNumeros.put(jugador, numero);
            jugadoresTexto.append(jugador).append(" - ").append(numero).append("\n");

            if (numero < minNumero) {
                minNumero = numero;
                perdedor = jugador;
            }
        }

        playerNameTextView.setText(jugadoresTexto.toString());
        nombrePerdedorTextView.setText("A BEBER: " + perdedor);
    }
}

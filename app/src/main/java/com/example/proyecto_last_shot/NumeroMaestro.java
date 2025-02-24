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

/**
 * Clase NumeroMaestro que gestiona la asignación aleatoria de números a jugadores y determina
 * quién recibe el número más bajo.
 */
public class NumeroMaestro extends AppCompatActivity {

    private TextView playerNameTextView; // TextView para mostrar los nombres y números de los jugadores
    private TextView nombrePerdedorTextView; // TextView para mostrar el jugador con el número más bajo
    private ImageView btnBack; // Botón de retroceso para cerrar la actividad
    private ArrayList<String> listaJugadores; // Lista de jugadores

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_maestro);

        // Inicialización de las vistas
        playerNameTextView = findViewById(R.id.tvJugadores);
        nombrePerdedorTextView = findViewById(R.id.nombrePerdedor);
        btnBack = findViewById(R.id.btn_back);

        // Configuración del botón de retroceso
        btnBack.setOnClickListener(v -> finish());

        // Obtener la lista de jugadores desde SharedPreferences
        listaJugadores = obtenerJugadoresDePreferencias();

        // Si hay jugadores disponibles, asignarles números y mostrarlos
        if (!listaJugadores.isEmpty()) {
            asignarNumerosYMostrar();
        } else {
            Toast.makeText(NumeroMaestro.this, "No hay jugadores disponibles", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Obtiene la lista de jugadores almacenada en SharedPreferences.
     *
     * @return Lista de nombres de jugadores.
     */
    private ArrayList<String> obtenerJugadoresDePreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("listaJugadores", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    /**
     * Asigna un número aleatorio único a cada jugador y muestra los resultados en pantalla.
     * También determina qué jugador tiene el número más bajo y lo muestra como "A BEBER".
     */
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

        // Asigna números a los jugadores y encuentra el de menor valor
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

        // Muestra la lista de jugadores con sus números asignados
        playerNameTextView.setText(jugadoresTexto.toString());
        // Muestra el jugador que debe beber
        nombrePerdedorTextView.setText("A BEBER: " + perdedor);
    }
}

package com.example.proyecto_last_shot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase VerdadReto que maneja la selección entre "Verdad" y "Reto".
 *
 * Esta actividad presenta dos opciones al usuario: elegir entre una pregunta de "Verdad"
 * o un desafío de "Reto". Dependiendo de la opción seleccionada, se redirige a la actividad
 * correspondiente (VerdadActivity o RetoActivity).
 *
 * También incluye un botón de retroceso para volver a la pantalla anterior.
 */
public class VerdadReto extends AppCompatActivity {
    private ImageView btnBack; // Botón para regresar a la pantalla anterior
    private TextView turno;  // TextView para mostrar el jugador aleatorio

    private List<String> listaJugadores = new ArrayList<>(); // Lista de jugadores

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los botones de navegación y configura sus acciones.
     *
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilita la visualización en pantalla completa
        setContentView(R.layout.verdad_reto); // Asigna el diseño XML de la actividad

        // Referencia al TextView para mostrar el jugador
        turno = findViewById(R.id.turno);

        // Configuración del botón para regresar a la pantalla anterior
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> finish()); // Finaliza la actividad y vuelve a la anterior

        // Cargar los jugadores desde las preferencias
        cargarJugadoresDesdePreferencias();

        // Mostrar un jugador aleatorio al iniciar la actividad
        mostrarJugadorAleatorio();

        // Configuración del botón "Reto"
        findViewById(R.id.btn_reto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad RetoActivity cuando se presione el botón "Reto"
                Intent intent = new Intent(VerdadReto.this, RetoActivity.class);
                startActivity(intent);
            }
        });

        // Configuración del botón "Verdad"
        findViewById(R.id.btn_verdad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad VerdadActivity cuando se presione el botón "Verdad"
                Intent intent = new Intent(VerdadReto.this, VerdadActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Muestra un jugador aleatorio en el TextView de "turno".
     */
    private void mostrarJugadorAleatorio() {
        if (!listaJugadores.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(listaJugadores.size());
            turno.setText("Turno de: " + listaJugadores.get(index)); // Muestra el jugador seleccionado aleatoriamente
        }
    }

    /**
     * Carga los jugadores desde las preferencias compartidas.
     */
    private void cargarJugadoresDesdePreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        String json = prefs.getString("listaJugadores", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            listaJugadores = gson.fromJson(json, ArrayList.class);
        }
    }
}

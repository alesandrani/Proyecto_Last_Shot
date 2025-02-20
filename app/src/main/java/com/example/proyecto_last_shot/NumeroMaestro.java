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

public class NumeroMaestro extends AppCompatActivity {

    private TextView playerNameTextView;
    private ImageView btnBack;
    private ArrayList<String> listaJugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numero_maestro); // Asegúrate de que el layout esté correcto

        // Referencias a los elementos de la interfaz
        playerNameTextView = findViewById(R.id.tvJugadores);
        btnBack = findViewById(R.id.btn_back);

        // Botón para cerrar la actividad
        btnBack.setOnClickListener(v -> finish());

        // Cargamos los jugadores desde SharedPreferences
        listaJugadores = obtenerJugadoresDePreferencias();

        // Verificamos si se cargaron jugadores
        if (!listaJugadores.isEmpty()) {
            // Mostrar la lista de jugadores
            mostrarJugadoresEnPantalla();
        } else {
            // Si no hay jugadores guardados, mostramos un mensaje
            Toast.makeText(NumeroMaestro.this, "No hay jugadores disponibles", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> obtenerJugadoresDePreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("listaJugadores", null);

        // Si no hay jugadores guardados, devolvemos una lista vacía
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    // Mostrar los jugadores en el TextView
    private void mostrarJugadoresEnPantalla() {
        // Unir todos los nombres de los jugadores en un solo texto separado por salto de línea
        String jugadoresTexto = TextUtils.join("\n", listaJugadores);

        // Mostrar el texto en el TextView
        playerNameTextView.setText(jugadoresTexto);
    }
}

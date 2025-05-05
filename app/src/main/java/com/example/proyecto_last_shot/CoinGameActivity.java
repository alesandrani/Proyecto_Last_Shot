package com.example.proyectolastshot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_last_shot.ActivityChat;
import com.example.proyecto_last_shot.R;

import java.util.Random;

public class CoinGameActivity extends AppCompatActivity {
    // Simulación de nombres de jugadores (esto debería venir de la lógica real del juego)
    private String[] jugadores = {"Jugador 1", "Jugador 2"};
    private TextView jugadorEmpiezaTextView;
    private Button chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_game_activity);

        jugadorEmpiezaTextView = findViewById(R.id.jugadorEmpiezaTextView);
        chatButton = findViewById(R.id.chatButton);

        // Elegir aleatoriamente quién empieza
        String jugadorEmpieza = jugadores[new Random().nextInt(jugadores.length)];
        jugadorEmpiezaTextView.setText("Empieza: " + jugadorEmpieza);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de chat (ajusta el nombre si tu actividad de chat tiene otro nombre)
                Intent intent = new Intent(CoinGameActivity.this, ActivityChat.class);
                startActivity(intent);
            }
        });
    }
}

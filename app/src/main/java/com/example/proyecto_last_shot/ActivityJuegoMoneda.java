package com.example.proyecto_last_shot;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ActivityJuegoMoneda extends AppCompatActivity {
    private ImageView monedaImage;
    private ImageView btnBack, btnChat;
    private ImageButton btnGirarMoneda, btnJugadores;
    private boolean mostrandoCara = true;
    private final Random random = new Random();
    private boolean mostrarCaraFinal; // Resultado aleatorio

    private String nombreJugadorActual; // <-- NUEVO: nombre del jugador actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_moneda);

        // Simular recibir nombreJugadorActual (puedes recibirlo del Intent si viene de otra actividad)
        nombreJugadorActual = getIntent().getStringExtra("nombreJugadorActual");
        if (nombreJugadorActual == null) {
            nombreJugadorActual = "JugadorPrueba";  // valor por defecto para pruebas
        }

        // Inicializar vistas
        btnBack = findViewById(R.id.btnBack);
        monedaImage = findViewById(R.id.imgMonedaCara);
        btnChat = findViewById(R.id.btnChat);
        btnGirarMoneda = findViewById(R.id.imgGirarMoneda);
        btnJugadores = findViewById(R.id.imgJugadores);

        monedaImage.setOnClickListener(v -> lanzarMoneda());
        btnGirarMoneda.setOnClickListener(v -> lanzarMoneda());

        btnBack.setOnClickListener(v -> finish());

        btnChat.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
            String json = prefs.getString("listaJugadores", null);
            ArrayList<String> listaJugadores = new ArrayList<>();

            if (json != null) {
                Gson gson = new Gson();
                String[] array = gson.fromJson(json, String[].class);
                listaJugadores = new ArrayList<>(Arrays.asList(array));
            }

            Intent intent = new Intent(ActivityJuegoMoneda.this, ActivityChatSeleccion.class);
            intent.putStringArrayListExtra("listaJugadores", listaJugadores);

            // PASAR EL NOMBRE DEL JUGADOR ACTUAL
            intent.putExtra("nombreJugadorActual", nombreJugadorActual);

            startActivity(intent);
        });

        btnJugadores.setOnClickListener(v -> mostrarDialogoJugadores());
    }

    private void lanzarMoneda() {
        mostrarCaraFinal = random.nextBoolean(); // true = cara, false = cruz

        ObjectAnimator animator = ObjectAnimator.ofFloat(monedaImage, "rotationY", 0f, 1800f);
        animator.setDuration(2000);

        animator.addUpdateListener(animation -> {
            float angle = (float) animation.getAnimatedValue();
            float angleMod = angle % 360;

            if (angleMod > 90f && angleMod < 270f && mostrandoCara) {
                monedaImage.setImageResource(R.drawable.img_coin_cruz);
                mostrandoCara = false;
            } else if ((angleMod < 90f || angleMod > 270f) && !mostrandoCara) {
                monedaImage.setImageResource(R.drawable.coin_cara);
                mostrandoCara = true;
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) {}
            @Override public void onAnimationCancel(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                monedaImage.setRotationY(0f);

                // Mostrar resultado final aleatorio
                if (mostrarCaraFinal) {
                    monedaImage.setImageResource(R.drawable.coin_cara);
                    mostrandoCara = true;
                } else {
                    monedaImage.setImageResource(R.drawable.img_coin_cruz);
                    mostrandoCara = false;
                }
            }
        });

        animator.start();
    }

    private void mostrarDialogoJugadores() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        String json = prefs.getString("listaJugadores", null);
        ArrayList<String> listaJugadores = new ArrayList<>();

        if (json != null) {
            Gson gson = new Gson();
            String[] array = gson.fromJson(json, String[].class);
            listaJugadores = new ArrayList<>(Arrays.asList(array));
        }

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_jugadores, null);
        ListView listaView = dialogView.findViewById(R.id.listJugadores);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaJugadores);
        listaView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Jugadores en la sala")
                .setView(dialogView)
                .setPositiveButton("Cerrar", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        dialog.show();
    }

}

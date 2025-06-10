package com.example.proyecto_last_shot;

import static android.content.Intent.getIntent;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
  private boolean mostrarCaraFinal;

  private String nombreJugadorActual;

  /**
   * Método llamado al crear la actividad. Inicializa las vistas, recupera el
   * nombre del jugador actual
   * y configura los listeners para los botones y la imagen de la moneda.
   * 
   * @param savedInstanceState Estado previamente guardado de la actividad, si
   *                           existe.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_juego_moneda);

    // Simular recibir nombreJugadorActual (puedes recibirlo del Intent si viene de
    // otra actividad)
    nombreJugadorActual = getIntent().getStringExtra("nombreJugadorActual");
    if (nombreJugadorActual == null) {
      nombreJugadorActual = "JugadorPrueba"; // valor por defecto para pruebas
    }

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

      intent.putExtra("nombreJugadorActual", nombreJugadorActual);

      startActivity(intent);
    });

    btnJugadores.setOnClickListener(v -> mostrarDialogoJugadores());
  }

  /**
   * Realiza la animación de lanzamiento de la moneda, mostrando un giro y
   * eligiendo aleatoriamente
   * si termina en cara o cruz. Cambia la imagen de la moneda durante la animación
   * y al finalizar muestra el resultado.
   */
  private void lanzarMoneda() {

    mostrarCaraFinal = Math.random() < 0.5;
    long duracion = 1200; // 1.2 segundos

    // Animación que gira 6 vueltas completas (360 * 6 = 2160 grados)
    ObjectAnimator animator = ObjectAnimator.ofFloat(monedaImage, "rotationY", 0f, 2160f);
    animator.setDuration(duracion);
    animator.setInterpolator(new AccelerateDecelerateInterpolator());

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
      @Override
      public void onAnimationStart(Animator animator) {
      }

      @Override
      public void onAnimationCancel(Animator animator) {
      }

      @Override
      public void onAnimationRepeat(Animator animator) {
      }

      @Override
      public void onAnimationEnd(Animator animator) {
        monedaImage.setRotationY(0f);

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

  /**
   * Muestra un diálogo con la lista de jugadores almacenados en las preferencias
   * compartidas.
   * Recupera la lista, la muestra en un ListView dentro de un AlertDialog y
   * permite cerrarlo.
   */
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

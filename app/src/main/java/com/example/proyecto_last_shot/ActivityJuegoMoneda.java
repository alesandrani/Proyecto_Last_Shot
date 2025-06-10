package com.example.proyecto_last_shot;

import static android.content.Intent.getIntent;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ActivityJuegoMoneda extends AppCompatActivity {


    // Componentes de la interfaz
    private ImageView monedaImage, btnBack, btnChat;
    private ImageButton btnGirarMoneda, btnJugadores;

    // Estados del giro de la moneda
    private boolean mostrandoCara = true;
    private boolean mostrarCaraFinal;
    private final Random random = new Random();

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

    // Datos del jugador y sala
    private String nombreJugadorActual;
    private String idSala;

    // URL de la base de datos con región específica (Europa)
    private static final String DATABASE_URL = "https://lastshot-proyecto-default-rtdb.europe-west1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_moneda);

        // Recuperar datos desde el Intent
        nombreJugadorActual = getIntent().getStringExtra("nombreJugadorActual");
        idSala = getIntent().getStringExtra("idSala");

        // Si faltan datos, mostrar error y cerrar la actividad
        if (nombreJugadorActual == null || idSala == null) {
            Toast.makeText(this, "Error: faltan datos de usuario o sala", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Referencias del layout
        btnBack = findViewById(R.id.btnBack);
        monedaImage = findViewById(R.id.imgMonedaCara);
        btnChat = findViewById(R.id.btnChat);
        btnGirarMoneda = findViewById(R.id.imgGirarMoneda);
        btnJugadores = findViewById(R.id.imgJugadores);

        // Configurar listeners
        btnBack.setOnClickListener(v -> finish());

        btnGirarMoneda.setOnClickListener(v -> lanzarMoneda());
        monedaImage.setOnClickListener(v -> lanzarMoneda());

        btnChat.setOnClickListener(v -> {
            // Lanzar ChatGrupalActivity directamente
            Intent intent = new Intent(ActivityJuegoMoneda.this, ChatGrupalActivity.class);
            intent.putExtra("nombreJugador", nombreJugadorActual);
            intent.putExtra("idSala", idSala);
            startActivity(intent);
        });

        btnJugadores.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityJuegoMoneda.this, SeleccionarJugadorChatActivity.class);
            intent.putExtra("idSala", idSala);
            intent.putExtra("nombreJugadorActual", nombreJugadorActual);
            startActivity(intent);
        });
    }

    /**
     * Lanza la moneda con animación y muestra cara o cruz aleatoriamente.
     */
    private void lanzarMoneda() {
        mostrarCaraFinal = random.nextBoolean(); // Determinar aleatoriamente el resultado

        ObjectAnimator animator = ObjectAnimator.ofFloat(monedaImage, "rotationY", 0f, 1800f);
        animator.setDuration(2000);

        animator.addUpdateListener(animation -> {
            float angle = (float) animation.getAnimatedValue();
            float angleMod = angle % 360;

            // Cambiar la imagen mientras gira para simular el efecto
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
                monedaImage.setRotationY(0f); // Reset de rotación
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
     * Muestra un diálogo con la lista de jugadores conectados en la sala actual.
     */
    private void mostrarDialogoJugadores() {
        DatabaseReference ref = FirebaseDatabase
                .getInstance(DATABASE_URL) // Asegurarse de usar URL con región
                .getReference("salas")
                .child(idSala)
                .child("jugadores");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> listaJugadores = new ArrayList<>();

                // Recorremos cada nodo hijo (nombre del jugador)
                for (DataSnapshot jugadorSnap : snapshot.getChildren()) {
                    String nombreJugador = jugadorSnap.getKey();
                    if (nombreJugador != null) {
                        listaJugadores.add(nombreJugador);
                    }
                }

                // Inflar layout personalizado del diálogo
                View dialogView = LayoutInflater.from(ActivityJuegoMoneda.this)
                        .inflate(R.layout.dialog_jugadores, null);

                ListView listaView = dialogView.findViewById(R.id.listJugadores);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        ActivityJuegoMoneda.this,
                        android.R.layout.simple_list_item_1,
                        listaJugadores
                );

                listaView.setAdapter(adapter);

                AlertDialog dialog = new AlertDialog.Builder(ActivityJuegoMoneda.this)
                        .setTitle("Jugadores en la sala")
                        .setView(dialogView)
                        .setPositiveButton("Cerrar", (dialogInterface, i) -> dialogInterface.dismiss())
                        .create();

                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityJuegoMoneda.this,
                        "Error al cargar los jugadores: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

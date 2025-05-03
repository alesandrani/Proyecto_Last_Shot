package com.example.proyecto_last_shot;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ActivityJuegoMoneda extends AppCompatActivity {
    private ImageView monedaImage;
    private ImageView btnBack,btnChat;
    private boolean mostrandoCara = true;
    private final Random random = new Random();
    private boolean mostrarCaraFinal; // Resultado aleatorio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_moneda);

        // Inicializar las vistas después de setContentView
        btnBack = findViewById(R.id.btnBack);
        monedaImage = findViewById(R.id.imgMonedaCara);
        btnChat = findViewById(R.id.btnChat);
        monedaImage.setOnClickListener(v -> lanzarMoneda());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityJuegoMoneda.this, ActivityChat.class);
                startActivity(intent);
            }
        });
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
}

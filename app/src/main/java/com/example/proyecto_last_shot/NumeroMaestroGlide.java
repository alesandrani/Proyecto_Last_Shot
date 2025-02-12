package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class NumeroMaestroGlide extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.numero_maestro);

            Glide.with(this)
                    .load("https://i.imgur.com/1tMFzp8.png")
                    .into(findViewById(R.id.bola));
    }
}
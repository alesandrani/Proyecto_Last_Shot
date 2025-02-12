package com.example.proyecto_last_shot;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class PaginaPrincipalGlide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial); // Asegúrate de que este archivo XML sea el correcto

        // Cargar imágenes usando Glide
        Glide.with(this)
                .load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/53e4e592-9b5a-4a0c-93df-70fdf9f35566")
                .into((ImageView) findViewById(R.id.menu));

        Glide.with(this)
                .load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5cfde65b-4c31-462d-9462-de0ddb6c19f8")
                .into((ImageView) findViewById(R.id.logo));

        Glide.with(this)
                .load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/38cabae6-f779-411d-91f7-083538423791")
                .into((ImageView) findViewById(R.id.cerveza));
    }
}
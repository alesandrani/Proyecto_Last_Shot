package com.example.proyecto_last_shot;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class PaginaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_inicial);

        Glide.with(this).load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/53e4e592-9b5a-4a0c-93df-70fdf9f35566").into((ImageView) findViewById(R.id.rumfs3dbdl2));
        Glide.with(this).load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/5cfde65b-4c31-462d-9462-de0ddb6c19f8").into((ImageView) findViewById(R.id.rrfkyk5wjuh9));
        Glide.with(this).load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/38cabae6-f779-411d-91f7-083538423791").into((ImageView) findViewById(R.id.rbaxguoevnfp));

    }
}
package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddJugadoresGlide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_add_jugadores);
        Glide.with(this)
                .load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/9763d9a4-6425-4f96-b32f-438ad6fc9290")
                .into((ImageView) findViewById(R.id.back));

        Glide.with(this)
                .load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/0729d217-bd6d-42b0-9f30-217caa96350a")
                .into((ImageView) findViewById(R.id.logo));

        Glide.with(this)
                .load("https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/e25ec7a8-064e-4a24-9d67-c60597be3645")
                .into((ImageView) findViewById(R.id.next));
    }
}
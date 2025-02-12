package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class NumeroMaestro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.numero_maestro);

            TextView numeroTextView = findViewById(R.id.numero);
            Button botonJugar = findViewById(R.id.botonJugar);

            botonJugar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(10) + 1; // Generates a number between 1 and 10 (inclusive)
                    numeroTextView.setText(String.valueOf(randomNumber));
                }
            });
    }
}
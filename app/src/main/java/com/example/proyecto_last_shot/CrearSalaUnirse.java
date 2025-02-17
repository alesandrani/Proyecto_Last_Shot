package com.example.proyecto_last_shot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CrearSalaUnirse extends AppCompatActivity {

        private EditText cuadroTexto;
        private LinearLayout container; // Contenedor donde se agregarán los TextViews dinámicamente

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.pagina_crear_unirse);

            // Referencias a los elementos del XML
            cuadroTexto = findViewById(R.id.cuadroTexto);
            Button addButton = findViewById(R.id.add);
            Button nextButton = findViewById(R.id.next);
            container = findViewById(R.id.container);  // Asegúrate de agregar un LinearLayout en el XML

            // Botón para agregar un TextView con el contenido del EditText
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String texto = cuadroTexto.getText().toString().trim();
                    if (!texto.isEmpty()) {
                        TextView nuevoTexto = new TextView(CrearSalaUnirse.this);
                        nuevoTexto.setText(texto);
                        nuevoTexto.setTextSize(18);
                        nuevoTexto.setPadding(10, 10, 10, 10);
                        container.addView(nuevoTexto); // Agrega el TextView al LinearLayout
                        cuadroTexto.setText(""); // Limpia el EditText
                    }
                }
            });

            // Botón para ir a la pantalla de juegos
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CrearSalaUnirse.this, PaginaJuegos.class);
                    startActivity(intent);
                }
            });

    }
}
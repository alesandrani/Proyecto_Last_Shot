package com.example.proyecto_last_shot;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class PaginaPrincipal extends AppCompatActivity {
    private ImageView menu, logo, cerveza;
    private LinearLayout boton;
    private TextView texto;
    private DrawerLayout drawerLayout;

    // Definir constantes para los IDs del menú lateral
    private static final int MENU_INFO = R.id.menu_info;
    private static final int MENU_TERMINOS = R.id.menu_terminos;
    private static final int MENU_PRIVACIDAD = R.id.menu_privacidad;
    private static final int MENU_CONTACTO = R.id.menu_contacto;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los elementos de la interfaz y configura los eventos.
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial); // Usamos el layout correcto

        // Referencias a los elementos del XML
        menu = findViewById(R.id.menu);
        logo = findViewById(R.id.logo);
        cerveza = findViewById(R.id.cerveza);
        boton = findViewById(R.id.boton);
        texto = findViewById(R.id.texto);
        drawerLayout = findViewById(R.id.drawerLayout);
        Typeface jerseyFont = ResourcesCompat.getFont(this, R.font.jersey_10);

        // Configuración del Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);

        // Evento para abrir el menú lateral
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START); // Abre el menú desde la izquierda
            }
        });

        // Listener para manejar los clics en el menú lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // Mostrar un mensaje según la opción seleccionada
                if (itemId == MENU_INFO) {
                    Toast.makeText(PaginaPrincipal.this, "Información sobre el juego", Toast.LENGTH_SHORT).show();
                } else if (itemId == MENU_TERMINOS) {
                    Toast.makeText(PaginaPrincipal.this, "Términos de uso", Toast.LENGTH_SHORT).show();
                } else if (itemId == MENU_PRIVACIDAD) {
                    Toast.makeText(PaginaPrincipal.this, "Política de privacidad", Toast.LENGTH_SHORT).show();
                } else if (itemId == MENU_CONTACTO) {
                    Toast.makeText(PaginaPrincipal.this, "Contacto", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START); // Cerrar el menú
                return true;
            }
        });

        // Evento al presionar el botón principal
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.setText("¡Salud! 🍻"); // Cambia el texto al hacer clic
                texto.setTypeface(jerseyFont);

                // Abrir la nueva actividad PaginaAddJugadores
                Intent intent = new Intent(PaginaPrincipal.this, PaginaAddJugadores.class);
                startActivity(intent);
            }
        });
    }
}

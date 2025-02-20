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
    // Definir constantes para los IDs
    private static final int MENU_INFO = R.id.menu_info;
    private static final int MENU_TERMINOS = R.id.menu_terminos;
    private static final int MENU_PRIVACIDAD = R.id.menu_privacidad;
    private static final int MENU_CONTACTO = R.id.menu_contacto;


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
        drawerLayout = findViewById(R.id.drawerLayout); // Asegúrate de que el ID sea correcto
        Typeface jerseyFont = ResourcesCompat.getFont(this, R.font.jersey_10);


        // Configuración del Navigation Drawer
        NavigationView navigationView = findViewById(R.id.navigationView);

        // Evento para abrir el menú lateral
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START); // Usamos GravityCompat.START para abrir el menú desde la izquierda
            }
        });

        // Listener para manejar los clics en el menú lateral (sin abrir actividades)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();


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

        // Evento al presionar el botón (aquí se realiza el Intent)
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                texto.setText("¡Salud! 🍻"); // Cambia el texto al hacer clic
                texto.setTypeface(jerseyFont);
                // Abrir la nueva actividad CrearSalaUnirse
                Intent intent = new Intent(PaginaPrincipal.this, CrearSalaUnirse.class);
                startActivity(intent);
            }
        });
    }
}
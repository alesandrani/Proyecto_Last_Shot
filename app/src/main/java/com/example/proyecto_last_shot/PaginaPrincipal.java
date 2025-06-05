package com.example.proyecto_last_shot;

import android.content.Intent;
import android.content.SharedPreferences;
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
                    Toast.makeText(PaginaPrincipal.this, "\uD83C\uDFAF Last Shot\n" +
                            "¡El juego de fiesta definitivo! Gira la ruleta, reta a tus colegas y descubre quién será el próximo en tomarse el último chupito. " +
                            "Minijuegos, verdad o reto, y muchas risas aseguradas. ¿Te atreves a jugar?", Toast.LENGTH_SHORT).show();
                } else if (itemId == MENU_TERMINOS) {
                    Toast.makeText(PaginaPrincipal.this, "\uD83D\uDCDC Términos de Uso de Last Shot\n" +
                            "Bienvenido/a a Last Shot, una app creada para disfrutar con amigos en un entorno divertido y responsable. Al usar esta aplicación, aceptas lo siguiente:\n" +
                            "\n" +
                            "Uso responsable: Last Shot está pensada para mayores de 18 años. Si incluye dinámicas con alcohol, tú y tus amigos sois responsables de consumir con moderación.\n" +
                            "\n" +
                            "Privacidad: No compartimos tus datos con terceros. Los nombres y partidas se almacenan solo para mejorar tu experiencia.\n" +
                            "\n" +
                            "Diversión segura: Está prohibido usar la app para retar o exponer a otros a situaciones peligrosas, ilegales o humillantes.\n" +
                            "\n" +
                            "Contenido generado: Algunas preguntas o retos pueden ser generados por los propios usuarios. Si ves algo inapropiado, avísanos.\n" +
                            "\n" +
                            "Cambios en la app: Podemos actualizar estos términos o la app en cualquier momento. Te avisaremos si hay cambios importantes.", Toast.LENGTH_SHORT).show();
                } else if (itemId == MENU_PRIVACIDAD) {
                    Toast.makeText(PaginaPrincipal.this, "\uD83D\uDD10 Política de Privacidad de Last Shot\n" +
                            "En Last Shot, nos tomamos tu privacidad tan en serio como el último chupito. Aquí te explicamos qué datos recogemos y cómo los usamos:\n" +
                            "\n" +
                            "¿Qué datos recogemos?\n" +
                            "\n" +
                            "Tu nombre de jugador y el de tus colegas para personalizar la partida.\n" +
                            "\n" +
                            "Datos de uso básicos (número de rondas, minijuegos jugados, etc.) para mejorar la app.\n" +
                            "\n" +
                            "Nada de datos sensibles, ubicación ni acceso a tu agenda.\n" +
                            "\n" +
                            "¿Para qué los usamos?\n" +
                            "\n" +
                            "Para que el juego funcione bien y sea más divertido.\n" +
                            "\n" +
                            "Para analizar qué modos gustan más y mejorar la experiencia.\n" +
                            "\n" +
                            "¿Compartimos tus datos?\n" +
                            "\n" +
                            "Jamás vendemos tus datos. Solo usamos Firebase (de Google) para guardar partidas y estadísticas.\n" +
                            "\n" +
                            "¿Dónde se guardan los datos?\n" +
                            "\n" +
                            "En servidores seguros de Firebase, con protección estándar de la industria.\n" +
                            "\n" +
                            "¿Puedes borrar tus datos?\n" +
                            "\n" +
                            "Sí. Escríbenos y eliminamos tus datos de inmediato.\n" +
                            "\n" +
                            "Actualizaciones\n" +
                            "\n" +
                            "Si cambiamos algo importante, te avisamos dentro de la app o por el canal que hayas elegido.", Toast.LENGTH_SHORT).show();
                } else if (itemId == MENU_CONTACTO) {
                    Toast.makeText(PaginaPrincipal.this, "\uD83D\uDCEC Contacto\n" +
                            "¿Tienes dudas, sugerencias o encontraste un bug tras el tercer chupito?\n" +
                            "¡Escríbenos y te leemos con resaca o sin ella!\n" +
                            "\n" +
                            "\uD83D\uDCE7 Email: lastshot.app@gmail.com\n" +
                            "\uD83D\uDC1E Soporte técnico: mismo correo, asunto: “¡Ayuda, Last Shot se ha colado!”\n" +
                            "\uD83D\uDCA1 Ideas o retos locos: ¡nos flipan! Cuéntanos lo que se te ocurra.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(PaginaPrincipal.this, CrearSalaUnirse.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isFinishing()) {
            // Borra jugadores solo si se cierra completamente
            SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("listaJugadores");
            editor.apply();
        }
    }

}

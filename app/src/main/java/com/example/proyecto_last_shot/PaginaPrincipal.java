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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * Actividad principal de bienvenida de la aplicación. Muestra el menú lateral
 * con información,
 * términos, privacidad y contacto, y permite acceder a la pantalla de creación
 * o unión de sala.
 */
public class PaginaPrincipal extends AppCompatActivity {
  private ImageView menu, logo;
  private LinearLayout boton;
  private TextView texto;
  private DrawerLayout drawerLayout;

  private static final int MENU_INFO = R.id.menu_info;
  private static final int MENU_TERMINOS = R.id.menu_terminos;
  private static final int MENU_PRIVACIDAD = R.id.menu_privacidad;
  private static final int MENU_CONTACTO = R.id.menu_contacto;

  /**
   * Método que se ejecuta al crear la actividad.
   * Inicializa las vistas, configura el menú lateral y los listeners de los
   * botones.
   * 
   * @param savedInstanceState Estado guardado de la actividad.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pagina_inicial);

    menu = findViewById(R.id.menu);
    logo = findViewById(R.id.logo);
    boton = findViewById(R.id.boton);
    texto = findViewById(R.id.texto);
    drawerLayout = findViewById(R.id.drawerLayout);

    Typeface jerseyFont = ResourcesCompat.getFont(this, R.font.jersey_10);

    NavigationView navigationView = findViewById(R.id.navigationView);

    menu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        drawerLayout.openDrawer(GravityCompat.START);
      }
    });

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        AlertDialog.Builder builder = new AlertDialog.Builder(PaginaPrincipal.this);
        builder.setPositiveButton("Cerrar", null);

        if (itemId == MENU_INFO) {
          builder.setTitle("\uD83C\uDFAF Last Shot")
              .setMessage(
                  "¡El juego de fiesta definitivo! Gira la ruleta, reta a tus colegas y descubre quién será el próximo en tomarse el último chupito. "
                      +
                      "Minijuegos, verdad o reto, y muchas risas aseguradas. ¿Te atreves a jugar?");
        } else if (itemId == MENU_TERMINOS) {
          builder.setTitle("\uD83D\uDCDC Términos de Uso de Last Shot")
              .setMessage(
                  "Bienvenido/a a Last Shot, una app creada para disfrutar con amigos en un entorno divertido y responsable. Al usar esta aplicación, aceptas lo siguiente:\n\n"
                      +
                      "Uso responsable: Last Shot está pensada para mayores de 18 años. Si incluye dinámicas con alcohol, tú y tus amigos sois responsables de consumir con moderación.\n\n"
                      +
                      "Privacidad: No compartimos tus datos con terceros. Los nombres y partidas se almacenan solo para mejorar tu experiencia.\n\n"
                      +
                      "Diversión segura: Está prohibido usar la app para retar o exponer a otros a situaciones peligrosas, ilegales o humillantes.\n\n"
                      +
                      "Contenido generado: Algunas preguntas o retos pueden ser generados por los propios usuarios. Si ves algo inapropiado, avísanos.\n\n"
                      +
                      "Cambios en la app: Podemos actualizar estos términos o la app en cualquier momento. Te avisaremos si hay cambios importantes.");
        } else if (itemId == MENU_PRIVACIDAD) {
          builder.setTitle("\uD83D\uDD10 Política de Privacidad de Last Shot")
              .setMessage(
                  "En Last Shot, nos tomamos tu privacidad tan en serio como el último chupito. Aquí te explicamos qué datos recogemos y cómo los usamos:\n\n"
                      +
                      "¿Qué datos recogemos?\n\n" +
                      "Tu nombre de jugador y el de tus colegas para personalizar la partida.\n\n" +
                      "Datos de uso básicos (número de rondas, minijuegos jugados, etc.) para mejorar la app.\n\n" +
                      "Nada de datos sensibles, ubicación ni acceso a tu agenda.\n\n" +
                      "¿Para qué los usamos?\n\n" +
                      "Para que el juego funcione bien y sea más divertido.\n\n" +
                      "Para analizar qué modos gustan más y mejorar la experiencia.\n\n" +
                      "¿Compartimos tus datos?\n\n" +
                      "Jamás vendemos tus datos. Solo usamos Firebase (de Google) para guardar partidas y estadísticas.\n\n"
                      +
                      "¿Dónde se guardan los datos?\n\n" +
                      "En servidores seguros de Firebase, con protección estándar de la industria.\n\n" +
                      "¿Puedes borrar tus datos?\n\n" +
                      "Sí. Escríbenos y eliminamos tus datos de inmediato.\n\n" +
                      "Actualizaciones\n\n" +
                      "Si cambiamos algo importante, te avisamos dentro de la app o por el canal que hayas elegido.");
        } else if (itemId == MENU_CONTACTO) {
          builder.setTitle("\uD83D\uDCEC Contacto")
              .setMessage("¿Tienes dudas, sugerencias o encontraste un bug tras el tercer chupito?\n" +
                  "¡Escríbenos y te leemos con resaca o sin ella!\n\n" +
                  "\uD83D\uDCE7 Email: lastshot.app@gmail.com\n" +
                  "\uD83D\uDC1E Soporte técnico: mismo correo, asunto: '¡Ayuda, Last Shot se ha colado!'\n" +
                  "\uD83D\uDCA1 Ideas o retos locos: ¡nos flipan! Cuéntanos lo que se te ocurra.");
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
      }
    });

    boton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        texto.setText("¡Salud! 🍻");
        texto.setTypeface(jerseyFont);

        Intent intent = new Intent(PaginaPrincipal.this, PaginaAddJugadores.class);
        startActivity(intent);
      }
    });
  }

  /**
   * Método que se ejecuta al volver a la actividad (por ejemplo, tras volver de
   * otra pantalla).
   * Restaura el texto y la fuente del mensaje principal.
   */
  @Override
  protected void onResume() {
    super.onResume();
    // Resetear texto y tipo de letra a estado original
    texto.setText("A BEBER");
    texto.setTypeface(ResourcesCompat.getFont(this, R.font.jersey_10));
  }

  /**
   * Método que se ejecuta al destruir la actividad.
   * Si la actividad se está cerrando, borra la lista de jugadores de las
   * preferencias compartidas.
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (isFinishing()) {
      SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
      SharedPreferences.Editor editor = prefs.edit();
      editor.remove("listaJugadores");
      editor.apply();
    }
  }
}

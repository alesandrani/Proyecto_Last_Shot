package com.example.proyecto_last_shot;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PaginaJuegos extends AppCompatActivity {
    private ImageView btnBack;
    private LinearLayout btnMechero, btnVerdadReto, btnNumeroMaestro, btnJuegoRandom;
    private ImageView infoMechero, infoVerdadReto, infoNumeroMaestro, infoJuegoRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_juegos);

        // Inicialización de vistas
        btnBack = findViewById(R.id.btn_back);
        btnMechero = findViewById(R.id.btn_mechero);
        btnVerdadReto = findViewById(R.id.btn_verdad_reto);
        btnNumeroMaestro = findViewById(R.id.btn_numero_maestro);
        btnJuegoRandom = findViewById(R.id.btn_juego_random);

        // Iconos de información asociados a cada juego
        infoMechero = btnMechero.findViewById(R.id.infoMechero);
        infoVerdadReto = btnVerdadReto.findViewById(R.id.infoVerdadReto);
        infoNumeroMaestro = btnNumeroMaestro.findViewById(R.id.infoNumero);
        infoJuegoRandom = btnJuegoRandom.findViewById(R.id.infoRandom);

        // Configurar eventos de clic para los botones de navegación
        btnBack.setOnClickListener(view -> finish()); // Cierra la actividad y regresa a la anterior

       // btnMechero.setOnClickListener(view -> abrirJuego(ActivityJuegoMoneda.class));
        btnVerdadReto.setOnClickListener(view -> abrirJuego(VerdadReto.class));
        btnNumeroMaestro.setOnClickListener(view -> abrirJuego(NumeroMaestro.class));
        btnJuegoRandom.setOnClickListener(view -> seleccionarJuegoAleatorio());

        // Configurar eventos de clic para mostrar información de cada juego
        infoMechero.setOnClickListener(view -> mostrarDialogo("Mechero", "El juego comienza con un jugador aleatorio que tiene el mechero (marcado en el chat). " +
                "Este jugador elige a otro para hacerle una pregunta en un chat privado. " +
                "El jugador responde indicando a quién pasará el mechero, y la marca cambia automáticamente. " +
                "Luego, se lanza una moneda: si sale cara, la pregunta se revela; si sale cruz, se mantiene en secreto." +
                " El turno pasa al nuevo jugador con el mechero y el ciclo se repite."));

        infoVerdadReto.setOnClickListener(view -> mostrarDialogo("Verdad o Reto", "Un juego dinámico donde los jugadores son seleccionados aleatoriamente para elegir entre verdad o reto. " +
                "Al seleccionar, recibirán una acción que deberán realizar o una verdad que confesar. " +
                "El resto de los jugadores votará a través de la aplicación si el jugador ha cumplido con la acción o si merece beber. " +
                "¡Diversión garantizada en cada turno!"));

        infoNumeroMaestro.setOnClickListener(view -> mostrarDialogo("Número Maestro", "Cada jugador recibirá un número aleatorio que saldrá en el chat general, el que tenga el número más bajo bebe."));

        infoJuegoRandom.setOnClickListener(view -> mostrarDialogo("Juego Random", "Elige un juego aleatorio de la lista."));
    }

    /**
     * Inicia una nueva actividad con la clase proporcionada como parámetro.
     * @param activity Clase de la actividad a abrir.
     */
    private void abrirJuego(Class<?> activity) {
        Intent intent = new Intent(PaginaJuegos.this, activity);
        startActivity(intent);
    }

    /**
     * Selecciona aleatoriamente uno de los juegos disponibles y abre su actividad correspondiente.
     */
    private void seleccionarJuegoAleatorio() {
        int randomGame = new Random().nextInt(3); // Generar número aleatorio entre 0 y 2

        Class<?> selectedActivity;
        switch (randomGame) {
           // case 0:
                //selectedActivity = ActivityJuegoMoneda.class;
               // break;
            case 1:
                selectedActivity = VerdadReto.class;
                break;
            case 2:
            default:
                selectedActivity = NumeroMaestro.class;
                break;
        }

        abrirJuego(selectedActivity);
    }

    /**
     * Muestra un cuadro de diálogo con información sobre el juego seleccionado.
     * @param titulo Título del diálogo.
     * @param descripcion Descripción del juego.
     */
    private void mostrarDialogo(String titulo, String descripcion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_info, null);
        builder.setView(dialogView);

        TextView tituloView = dialogView.findViewById(R.id.dialog_title);
        TextView descripcionView = dialogView.findViewById(R.id.dialog_description);
        ImageView btnClose = dialogView.findViewById(R.id.btn_close);

        tituloView.setText(titulo);
        descripcionView.setText(descripcion);

        AlertDialog dialog = builder.create();
        btnClose.setOnClickListener(v -> dialog.dismiss()); // Cierra el diálogo al hacer clic en el botón

        dialog.show();
    }
}

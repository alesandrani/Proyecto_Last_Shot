package com.example.proyecto_last_shot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Actividad que muestra preguntas de "Reto" en una interfaz.
 * Las preguntas se obtienen desde Firestore REST API y se muestran con
 * temporizador.
 * Incluye botón "Siguiente" para avanzar manualmente y animaciones para
 * suavizar los cambios.
 */
public class RetoActivity extends AppCompatActivity {

  private TextView tvPreguntaReto, tvTimer, tvPreguntaNumero;
  private ImageView btnBack;
  private ImageButton btnSiguiente;

  private static final String TAG = "RetoActivity";

  private static final long TIMER_DURATION = 30000;
  private static final long TIMER_INTERVAL = 1000;

  private static final String API_KEY = "AIzaSyD5X1wkoaIHotR9URrIHThG1jUvykZSpYE";
  private static final String PROJECT_ID = "lastshot-proyecto";
  private static final String URL_FIRESTORE = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID
      + "/databases/(default)/documents/reto_verdad?key=" + API_KEY;

  private List<String> preguntasList = new ArrayList<>();
  private CountDownTimer countDownTimer;

  // Control de índice y total de preguntas
  private int preguntaActual = 0;
  private int totalPreguntas = 0;

  private Animation fadeIn, fadeOut;

  /**
   * Método que se ejecuta al crear la actividad.
   * Inicializa las vistas, animaciones, listeners y comienza la obtención de
   * preguntas.
   * 
   * @param savedInstanceState Estado guardado de la actividad.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.reto);

    btnBack = findViewById(R.id.btn_back);
    tvPreguntaReto = findViewById(R.id.preguntaReto);
    tvTimer = findViewById(R.id.timer);
    tvPreguntaNumero = findViewById(R.id.tv_pregunta_numero);
    btnSiguiente = findViewById(R.id.btn_siguiente);

    fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
    fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

    btnBack.setOnClickListener(view -> finish());

    btnSiguiente.setOnClickListener(view -> mostrarSiguientePregunta());

    new ObtenerPreguntasFirestore().execute(URL_FIRESTORE);
  }

  /**
   * Tarea asíncrona para obtener las preguntas desde Firestore usando REST API.
   */
  private class ObtenerPreguntasFirestore extends AsyncTask<String, Void, List<String>> {
    @Override
    protected List<String> doInBackground(String... urls) {
      List<String> preguntas = new ArrayList<>();
      try {
        URL url = new URL(urls[0]);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          StringBuilder response = new StringBuilder();
          String line;
          while ((line = reader.readLine()) != null) {
            response.append(line);
          }
          reader.close();

          JSONObject jsonResponse = new JSONObject(response.toString());
          if (jsonResponse.has("documents")) {
            JSONArray documentos = jsonResponse.getJSONArray("documents");

            for (int i = 0; i < documentos.length(); i++) {
              JSONObject doc = documentos.getJSONObject(i);
              JSONObject fields = doc.getJSONObject("fields");

              if (fields.has("pregunta")) {
                String pregunta = fields.getJSONObject("pregunta").getString("stringValue");
                preguntas.add(pregunta);
              }
            }
          }
        } else {
          Log.e(TAG, "Error en la solicitud: Código " + responseCode);
        }
        conn.disconnect();
      } catch (Exception e) {
        Log.e(TAG, "Error obteniendo preguntas de Firestore", e);
      }
      return preguntas;
    }

    @Override
    protected void onPostExecute(List<String> preguntas) {
      if (!preguntas.isEmpty()) {

        Collections.shuffle(preguntas);
        preguntasList = preguntas;
        totalPreguntas = preguntasList.size();
        preguntaActual = 0;
        mostrarSiguientePregunta();
      } else {
        tvPreguntaReto.setText("No hay preguntas disponibles.");
      }
    }
  }

  /**
   * Muestra la siguiente pregunta en pantalla, reiniciando el temporizador y
   * aplicando animaciones.
   */
  private void mostrarSiguientePregunta() {

    if (countDownTimer != null) {
      countDownTimer.cancel();
    }

    if (!preguntasList.isEmpty()) {
      String pregunta = preguntasList.remove(0);
      preguntaActual++;

      tvPreguntaReto.startAnimation(fadeOut);
      tvPreguntaReto.setText(pregunta);
      tvPreguntaReto.startAnimation(fadeIn);

      tvPreguntaNumero.setText("Pregunta " + preguntaActual + " de " + totalPreguntas);

      iniciarTemporizador();

    } else {
      // Si no hay más preguntas
      tvPreguntaReto.setText("¡Ya no hay más preguntas!");
      tvPreguntaNumero.setText("");
      tvTimer.setText("");
      btnSiguiente.setEnabled(false); // Desactivamos el botón
    }
  }

  /**
   * Inicia el temporizador de cuenta atrás de 30 segundos.
   */
  private void iniciarTemporizador() {
    countDownTimer = new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {
      @Override
      public void onTick(long millisUntilFinished) {
        tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
      }

      @Override
      public void onFinish() {
        tvTimer.setText("¡Tiempo terminado!");

      }
    }.start();
  }
}
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
 * Actividad que muestra preguntas de "Verdad" en una interfaz similar a RetoActivity.
 * Las preguntas se obtienen desde Firestore REST API y se muestran con temporizador.
 * Incluye botón "Siguiente" para avanzar manualmente y animaciones para suavizar los cambios.
 */
public class VerdadActivity extends AppCompatActivity {

    private TextView tvPreguntaVerdad, tvTimer, tvPreguntaNumero;
    private ImageView btnBack;
    private ImageButton btnSiguiente;

    private static final String TAG = "VerdadActivity";

    // Duración total del temporizador en milisegundos (30 segundos)
    private static final long TIMER_DURATION = 30000;
    private static final long TIMER_INTERVAL = 1000;

    // Clave de API y URL para acceder a Firestore directamente (colección verdad_reto)
    private static final String API_KEY = "AIzaSyD5X1wkoaIHotR9URrIHThG1jUvykZSpYE";
    private static final String PROJECT_ID = "lastshot-proyecto";
    private static final String URL_FIRESTORE =
            "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/verdad_reto?key=" + API_KEY;

    private List<String> preguntasList = new ArrayList<>();
    private CountDownTimer countDownTimer;

    // Control de índice y total de preguntas
    private int preguntaActual = 0;
    private int totalPreguntas = 0;

    // Animaciones de entrada y salida
    private Animation fadeIn, fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verdad);

        // Inicializamos las vistas
        btnBack = findViewById(R.id.btn_back);
        tvPreguntaVerdad = findViewById(R.id.preguntaVerdad);
        tvTimer = findViewById(R.id.timer);
        tvPreguntaNumero = findViewById(R.id.tv_pregunta_numero);
        btnSiguiente = findViewById(R.id.btn_siguiente);

        // Cargamos las animaciones desde res/anim
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Acción para el botón de volver atrás
        btnBack.setOnClickListener(view -> finish());

        // Acción para el botón "Siguiente"
        btnSiguiente.setOnClickListener(view -> mostrarSiguientePregunta());

        // Comienza la obtención de preguntas desde Firestore
        new ObtenerPreguntasFirestore().execute(URL_FIRESTORE);
    }

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
                tvPreguntaVerdad.setText("No hay preguntas disponibles.");
            }
        }
    }

    private void mostrarSiguientePregunta() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (!preguntasList.isEmpty()) {
            String pregunta = preguntasList.remove(0);
            preguntaActual++;

            tvPreguntaVerdad.startAnimation(fadeOut);
            tvPreguntaVerdad.setText(pregunta);
            tvPreguntaVerdad.startAnimation(fadeIn);

            tvPreguntaNumero.setText("Pregunta " + preguntaActual + " de " + totalPreguntas);

            iniciarTemporizador();
        } else {
            tvPreguntaVerdad.setText("¡Ya no hay más preguntas!");
            tvPreguntaNumero.setText("");
            tvTimer.setText("");
            btnSiguiente.setEnabled(false);
        }
    }

    private void iniciarTemporizador() {
        countDownTimer = new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("¡Tiempo terminado!");
                // Aquí puedes activar mostrarSiguientePregunta() si quieres avanzar automático
            }
        }.start();
    }
}
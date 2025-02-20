package com.example.proyecto_last_shot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
import java.util.List;
import java.util.Random;

public class RetoActivity extends AppCompatActivity {
    private TextView tvPreguntaReto;
    private TextView timerText;
    private ImageView btnBack;
    private static final String TAG = "RetoActivity";

    // Constantes para el temporizador
    private static final long TIMER_DURATION = 30000; // 30 segundos
    private static final long TIMER_INTERVAL = 1000; // 1 segundo

    private static final String API_KEY = "AIzaSyD5X1wkoaIHotR9URrIHThG1jUvykZSpYE";
    private static final String PROJECT_ID = "lastshot-proyecto";
    private static final String URL_FIRESTORE =
            "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/reto_verdad?key=" + API_KEY;

    private List<String> preguntasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reto);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> finish());

        tvPreguntaReto = findViewById(R.id.preguntaReto);
        timerText = findViewById(R.id.timerText);

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
                preguntasList = preguntas;
                mostrarPreguntaAleatoria();
                iniciarTemporizador();
            } else {
                tvPreguntaReto.setText("No hay preguntas disponibles.");
            }
        }
    }

    private void mostrarPreguntaAleatoria() {
        if (!preguntasList.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(preguntasList.size());
            tvPreguntaReto.setText(preguntasList.get(index));
        }
    }

    private void iniciarTemporizador() {
        new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {
            int tiempoRestante = (int) (TIMER_DURATION / 1000); // Convertir a segundos

            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante--;
                timerText.setText(String.valueOf(tiempoRestante));
            }

            @Override
            public void onFinish() {
                timerText.setText("¡Tiempo!");
                // Aquí puedes realizar acciones adicionales al finalizar el temporizador
            }
        }.start();
    }
}
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
    private TextView tvPreguntaReto, tvTimer;
    private ImageView btnBack;
    private static final String TAG = "RetoActivity";

    private static final long TIMER_DURATION = 30000; // 30 segundos
    private static final long TIMER_INTERVAL = 1000; // 1 segundo

    private static final String API_KEY = "AIzaSyD5X1wkoaIHotR9URrIHThG1jUvykZSpYE";
    private static final String PROJECT_ID = "lastshot-proyecto";
    private static final String URL_FIRESTORE =
            "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/reto_verdad?key=" + API_KEY;

    private List<String> preguntasList = new ArrayList<>();
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reto);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> finish());

        tvPreguntaReto = findViewById(R.id.preguntaReto);
        tvTimer = findViewById(R.id.timer);

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
            iniciarTemporizador();
        }
    }
    /**
     * Inicia un temporizador que cuenta hacia atrás desde un tiempo específico.
     *
     * Este método se encarga de cancelar un temporizador existente (si lo hay) y
     * crear uno nuevo utilizando la clase CountDownTimer. El temporizador actualiza
     * un TextView con el tiempo restante en segundos y muestra un mensaje cuando
     * el tiempo se ha agotado.
     *
     * Funcionamiento:
     * - Al llamar a este método, primero se verifica si hay un temporizador activo.
     *   Si es así, se cancela para evitar múltiples temporizadores simultáneos.
     * - Se crea una nueva instancia de CountDownTimer con la duración total
     *   (TIMER_DURATION) y el intervalo de actualización (TIMER_INTERVAL).
     * - En cada tick (cada intervalo definido), se actualiza el TextView (tvTimer)
     *   con el tiempo restante en segundos.
     * - Cuando el temporizador finaliza, se actualiza el TextView para mostrar
     *   un mensaje que indica que el tiempo ha terminado.
     *
     * Dependencias:
     * - tvTimer: Un TextView que muestra el tiempo restante del temporizador.
     * - TIMER_DURATION: La duración total del temporizador en milisegundos.
     * - TIMER_INTERVAL: El intervalo de actualización del temporizador en milisegundos.
     */
    private void iniciarTemporizador() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

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

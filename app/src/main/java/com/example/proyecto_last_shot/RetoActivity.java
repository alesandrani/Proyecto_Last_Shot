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

/**
 * Clase RetoActivity que maneja la visualización de preguntas de reto obtenidas desde Firestore
 * y el funcionamiento de un temporizador para responder la pregunta.
 */
public class RetoActivity extends AppCompatActivity {
    private TextView tvPreguntaReto, tvTimer; // TextViews para mostrar la pregunta y el tiempo restante
    private ImageView btnBack; // Botón para regresar a la actividad anterior
    private static final String TAG = "RetoActivity";

    // Constantes para la duración del temporizador
    private static final long TIMER_DURATION = 30000; // 30 segundos
    private static final long TIMER_INTERVAL = 1000; // 1 segundo

    // Configuración de la URL para obtener preguntas desde Firestore
    private static final String API_KEY = "AIzaSyD5X1wkoaIHotR9URrIHThG1jUvykZSpYE";
    private static final String PROJECT_ID = "lastshot-proyecto";
    private static final String URL_FIRESTORE =
            "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/reto_verdad?key=" + API_KEY;

    private List<String> preguntasList = new ArrayList<>(); // Lista de preguntas obtenidas de Firestore
    private CountDownTimer countDownTimer; // Temporizador para responder la pregunta

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los elementos de la interfaz y obtiene las preguntas desde Firestore.
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reto);

        // Referencias a los elementos del XML
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> finish()); // Botón para regresar a la actividad anterior

        tvPreguntaReto = findViewById(R.id.preguntaReto);
        tvTimer = findViewById(R.id.timer);

        // Inicia la obtención de preguntas desde Firestore
        new ObtenerPreguntasFirestore().execute(URL_FIRESTORE);
    }

    /**
     * Clase asíncrona para obtener preguntas desde Firestore.
     *
     * Esta clase extiende AsyncTask y realiza una solicitud HTTP GET a la base de datos Firestore
     * para recuperar una lista de preguntas almacenadas en la colección "reto_verdad".
     *
     * Funcionamiento:
     * - En `doInBackground()`, se establece una conexión con Firestore, se envía la solicitud
     *   y se procesa la respuesta JSON para extraer las preguntas.
     * - Si la solicitud es exitosa, se extraen los valores de las preguntas y se almacenan en
     *   una lista de tipo `List<String>`.
     * - En `onPostExecute()`, si hay preguntas disponibles, se actualiza la lista global y
     *   se muestra una pregunta aleatoria en pantalla; de lo contrario, se muestra un mensaje
     *   indicando que no hay preguntas disponibles.
     *
     * Manejadores de errores:
     * - Si la conexión falla o hay un problema en la respuesta JSON, se registra un error en los logs.
     * - Si la solicitud no es exitosa (código de respuesta diferente a HTTP_OK), se informa en el log.
     *
     * Dependencias:
     * - Se necesita acceso a internet para comunicarse con Firestore.
     * - La actividad `RetoActivity` debe contener un `TextView` para mostrar la pregunta obtenida.
     */

    private class ObtenerPreguntasFirestore extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... urls) {
            List<String> preguntas = new ArrayList<>();
            try {
                // Establecer conexión con Firestore
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                // Verificar respuesta del servidor
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Procesar la respuesta JSON
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

        /**
         * Método ejecutado después de obtener las preguntas.
         * Si hay preguntas disponibles, muestra una pregunta aleatoria.
         */
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

    /**
     * Muestra una pregunta aleatoria en pantalla y empieza el temporizador.
     */
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

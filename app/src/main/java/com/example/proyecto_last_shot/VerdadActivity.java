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
 * Clase VerdadActivity que maneja la visualización de preguntas de "Verdad" obtenidas desde Firestore.
 *
 * Esta actividad se encarga de realizar una solicitud a la base de datos Firestore para obtener
 * preguntas de la categoría "Verdad", seleccionar una aleatoriamente y mostrarla en pantalla.
 *
 * Además, incluye un temporizador de 15 segundos que cuenta el tiempo restante para responder
 * la pregunta antes de que el tiempo se agote.
 */
public class VerdadActivity extends AppCompatActivity {
    private TextView tvPreguntaVerdad, tvTimer; // Elementos de la interfaz: Pregunta y Temporizador
    private ImageView btnBack; // Botón para regresar a la pantalla anterior
    private static final String TAG = "VerdadActivity"; // Identificador para el log de la clase

    // Configuración de Firestore: Clave de API y URL de la colección
    private static final String API_KEY = "AIzaSyD5X1wkoaIHotR9URrIHThG1jUvykZSpYE";
    private static final String PROJECT_ID = "lastshot-proyecto";

    // URL para obtener preguntas de la colección "verdad_reto" en Firestore
    private static final String URL_FIRESTORE =
            "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/verdad_reto?key=" + API_KEY;

    private List<String> preguntasList = new ArrayList<>(); // Lista de preguntas obtenidas de Firestore
    private CountDownTimer countDownTimer; // Temporizador de cuenta regresiva

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los elementos de la interfaz y obtiene las preguntas desde Firestore.
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verdad);

        // Configuración de elementos de la interfaz
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> finish()); // Botón para regresar a la actividad anterior

        tvPreguntaVerdad = findViewById(R.id.preguntaVerdad);
        tvTimer = findViewById(R.id.timer);

        // Inicia la obtención de preguntas desde Firestore
        new ObtenerPreguntasFirestore().execute(URL_FIRESTORE);
    }

    /**
     * Clase asíncrona para obtener preguntas desde Firestore.
     *
     * Esta clase extiende AsyncTask y realiza una solicitud HTTP GET a la base de datos Firestore
     * para recuperar una lista de preguntas almacenadas en la colección "verdad_reto".
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
     * - La actividad `VerdadActivity` debe contener un `TextView` para mostrar la pregunta obtenida.
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
                tvPreguntaVerdad.setText("No hay preguntas disponibles.");
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
            tvPreguntaVerdad.setText(preguntasList.get(index));
            iniciarTemporizador();
        }
    }
    /**
     * Inicia un temporizador que cuenta hacia atrás desde 15 segundos.
     *
     * Este método cancela cualquier temporizador existente y crea uno nuevo utilizando
     * la clase CountDownTimer. El temporizador actualiza un TextView con el tiempo
     * restante en segundos y muestra un mensaje cuando el tiempo se ha agotado.
     *
     * Funcionamiento:
     * - Al invocar este método, se verifica si hay un temporizador activo.
     *   Si existe, se cancela para evitar múltiples temporizadores en ejecución.
     * - Se crea una nueva instancia de CountDownTimer con una duración de 15 segundos
     *   (15000 milisegundos) y un intervalo de actualización de 1 segundo (1000 milisegundos).
     * - En cada tick (cada segundo), se actualiza el TextView (tvTimer) para mostrar
     *   el tiempo restante en segundos.
     * - Cuando el temporizador finaliza, se actualiza el TextView para mostrar
     *   el mensaje "¡Tiempo terminado!".
     *
     * Dependencias:
     * - tvTimer: Un TextView que muestra el tiempo restante del temporizador.
     * - 15000: La duración total del temporizador en milisegundos (15 segundos).
     * - 1000: El intervalo de actualización del temporizador en milisegundos (1 segundo).
     */
    private void iniciarTemporizador() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(15000, 1000) { // 15 segundos, con intervalos de 1 segundo
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
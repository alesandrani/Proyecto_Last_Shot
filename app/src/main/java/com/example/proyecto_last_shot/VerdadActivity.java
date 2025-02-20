package com.example.proyecto_last_shot;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class VerdadActivity extends AppCompatActivity {
    private TextView tvPreguntaVerdad;
    private ImageView btnBack;
    private static final String TAG = "VerdadActivity";
    private static final String API_KEY = "TU_CLAVE_DE_API"; // Pega tu API Key aquí
    private static final String PROJECT_ID = "tu-proyecto-id"; // Nombre de tu proyecto en Firebase
    private static final String URL_FIRESTORE =
            "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/verdad_reto?key=" + API_KEY;

    private List<String> preguntasList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verdad);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(view -> finish());
        tvPreguntaVerdad = findViewById(R.id.preguntaVerdad);

        // Llamar a la API REST de Firestore
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

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray documentos = jsonResponse.getJSONArray("documents");

                for (int i = 0; i < documentos.length(); i++) {
                    JSONObject doc = documentos.getJSONObject(i);
                    JSONObject fields = doc.getJSONObject("fields");
                    String pregunta = fields.getJSONObject("verdad").getString("stringValue");
                    preguntas.add(pregunta);
                }
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
                tvPreguntaVerdad.setText("No hay preguntas disponibles.");
            }
        }
    }

    private void mostrarPreguntaAleatoria() {
        if (!preguntasList.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(preguntasList.size());
            tvPreguntaVerdad.setText(preguntasList.get(index));
        }
    }
}

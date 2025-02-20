package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VerdadActivity extends AppCompatActivity {
    private TextView tvPreguntaVerdad;
    private FirebaseFirestore db;
    private List<String> preguntasList;
    private static final String TAG = "VerdadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verdad); // Asegúrate de que el nombre sea correcto

        tvPreguntaVerdad = findViewById(R.id.preguntaVerdad);
        db = FirebaseFirestore.getInstance();
        preguntasList = new ArrayList<>();

        // Obtener preguntas de Firebase
        db.collection("verdad_reto").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String pregunta = document.getString("verdad");
                        if (pregunta != null) {
                            preguntasList.add(pregunta);
                            Log.d(TAG, "Pregunta obtenida: " + pregunta);
                        }
                    }

                    if (!preguntasList.isEmpty()) {
                        mostrarPreguntaAleatoria();
                    } else {
                        tvPreguntaVerdad.setText("No hay preguntas disponibles.");
                        Log.d(TAG, "Lista de preguntas vacía.");
                    }
                })
                .addOnFailureListener(e -> {
                    tvPreguntaVerdad.setText("Error al cargar preguntas.");
                    Log.e(TAG, "Error en Firebase: ", e);
                });
    }

    private void mostrarPreguntaAleatoria() {
        Random random = new Random();
        int indexAleatorio = random.nextInt(preguntasList.size());
        String preguntaAleatoria = preguntasList.get(indexAleatorio);
        tvPreguntaVerdad.setText(preguntaAleatoria);
        Log.d(TAG, "Mostrando pregunta aleatoria: " + preguntaAleatoria);
    }
}

package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class VerdadReto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.verdad_reto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    final EditText et = findViewById(R.id.editTextPregunta);
    final Button bt = findViewById(R.id.button1);
    final TextView tv = findViewById(R.id.pregunta1);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        bt.setOnClickListener(v -> {
        });

        db.collection("preguntas").document("ycv2P3Ro7OwgSOMFbZEM").get()
                .addOnSuccessListener(documentSnapshot ->{
                    String valor = documentSnapshot.getString("valor");
                    tv.setText(valor);
    });
    }
}
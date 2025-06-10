package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityChatSeleccion extends AppCompatActivity {

    private ListView listJugadores;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaJugadores = new ArrayList<>();

    private DatabaseReference jugadoresRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_seleccion);

        listJugadores = findViewById(R.id.listJugadores);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaJugadores);
        listJugadores.setAdapter(adapter);

        jugadoresRef = FirebaseDatabase.getInstance().getReference("jugadores");

        jugadoresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaJugadores.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String nombre = ds.getValue(String.class);
                    if (nombre != null) {
                        listaJugadores.add(nombre);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityChatSeleccion.this, "Error al cargar jugadores", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

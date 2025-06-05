package com.example.proyecto_last_shot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class JugadorAdapter extends RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder> {

    // Interfaz para manejar clics
    public interface OnJugadorClickListener {
        void onJugadorClick(String nombreJugador);
    }

    private final List<String> listaJugadores;
    private final OnJugadorClickListener listener;
    private final Context context;

    public JugadorAdapter(Context context, List<String> listaJugadores, OnJugadorClickListener listener) {
        this.context = context;
        Collections.sort(listaJugadores); // Ordenar alfabéticamente los nombres
        this.listaJugadores = listaJugadores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JugadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_jugador, parent, false);
        return new JugadorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorViewHolder holder, int position) {
        String nombreJugador = listaJugadores.get(position);
        holder.tvJugadorNombre.setText(nombreJugador);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJugadorClick(nombreJugador);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaJugadores.size();
    }

    // ViewHolder para mostrar el nombre del jugador
    public static class JugadorViewHolder extends RecyclerView.ViewHolder {
        TextView tvJugadorNombre;

        public JugadorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJugadorNombre = itemView.findViewById(R.id.tvJugadorNombre);
        }
    }
}

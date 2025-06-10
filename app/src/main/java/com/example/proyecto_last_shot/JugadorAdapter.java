package com.example.proyecto_last_shot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * JugadorAdapter es un adaptador para mostrar la lista de jugadores en un RecyclerView.
 * Permite seleccionar un jugador para iniciar un chat privado.
 */
public class JugadorAdapter extends RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder> {
    private List<String> jugadores;
    private OnJugadorClickListener listener;

    /**
     * Interfaz para manejar el clic sobre un jugador.
     */
    public interface OnJugadorClickListener {
        void onJugadorClick(String nombre);
    }

    public JugadorAdapter(List<String> jugadores, OnJugadorClickListener listener) {
        this.jugadores = jugadores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JugadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jugador, parent, false);
        return new JugadorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorViewHolder holder, int position) {
        String nombre = jugadores.get(position);
        holder.tvJugadorNombre.setText(nombre);
        holder.itemView.setOnClickListener(v -> listener.onJugadorClick(nombre));
        holder.iconoMensaje.setOnClickListener(v -> listener.onJugadorClick(nombre));
    }

    @Override
    public int getItemCount() {
        return jugadores.size();
    }

    /**
     * ViewHolder para mostrar el nombre y el icono de mensaje de cada jugador.
     */
    static class JugadorViewHolder extends RecyclerView.ViewHolder {
        TextView tvJugadorNombre;
        ImageView iconoMensaje;
        JugadorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJugadorNombre = itemView.findViewById(R.id.tvJugadorNombre);
            iconoMensaje = itemView.findViewById(R.id.iconoMensaje);
        }
    }
}

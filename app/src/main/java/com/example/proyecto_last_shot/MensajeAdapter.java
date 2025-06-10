package com.example.proyecto_last_shot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter para mostrar mensajes en un RecyclerView (chat privado o grupal).
 */
public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder> {

    private final List<Mensaje> mensajes;
    private final String nombreJugadorActual;

    public MensajeAdapter(List<Mensaje> mensajes, String nombreJugadorActual) {
        this.mensajes = mensajes;
        this.nombreJugadorActual = nombreJugadorActual;
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        return new MensajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        holder.tvContenido.setText(mensaje.getTexto());

        // Diferenciar entre mensaje propio y mensaje recibido
        boolean esMio = mensaje.getEmisor().equals(nombreJugadorActual);
        if (esMio) {
            holder.tvRemitente.setVisibility(View.GONE);
            holder.tvContenido.setBackgroundResource(R.drawable.bg_mensaje_enviado);
            holder.mensajeLayout.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            holder.tvRemitente.setVisibility(View.VISIBLE);
            holder.tvRemitente.setText(mensaje.getEmisor());
            holder.tvContenido.setBackgroundResource(R.drawable.bg_mensaje_recibido);
            holder.mensajeLayout.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class MensajeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRemitente, tvContenido;
        LinearLayout mensajeLayout;

        public MensajeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRemitente = itemView.findViewById(R.id.tvRemitente);
            tvContenido = itemView.findViewById(R.id.tvContenido);
            mensajeLayout = itemView.findViewById(R.id.mensajeLayout);
        }
    }
}

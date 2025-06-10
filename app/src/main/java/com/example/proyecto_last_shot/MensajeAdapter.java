package com.example.proyecto_last_shot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * MensajeAdapter es un adaptador para mostrar mensajes en un RecyclerView.
 * Distingue entre mensajes enviados y recibidos para mostrar diferentes layouts.
 */
public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder> {

    private ArrayList<Mensaje> mensajes;
    private String nombreUsuarioActual;
    private static final int TIPO_MENSAJE_ENVIADO = 1;
    private static final int TIPO_MENSAJE_RECIBIDO = 2;

    public MensajeAdapter(ArrayList<Mensaje> mensajes, String nombreUsuarioActual) {
        this.mensajes = mensajes;
        this.nombreUsuarioActual = nombreUsuarioActual;
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = mensajes.get(position);
        if (mensaje.getEmisor() == null || nombreUsuarioActual == null) {
            return TIPO_MENSAJE_RECIBIDO;
        }
        // Comparar ignorando mayúsculas/minúsculas y espacios
        if (mensaje.getEmisor().trim().equalsIgnoreCase(nombreUsuarioActual.trim())) {
            return TIPO_MENSAJE_ENVIADO;
        } else {
            return TIPO_MENSAJE_RECIBIDO;
        }
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == TIPO_MENSAJE_ENVIADO ? 
                     R.layout.item_mensaje_enviado : R.layout.item_mensaje_recibido;
        
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new MensajeViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        
        // Usar el campo correcto para el texto del mensaje
        holder.tvMensaje.setText(mensaje.getTexto());
        
        // Configurar la hora del mensaje
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String hora = sdf.format(new Date(mensaje.getTimestamp()));
        holder.tvHora.setText(hora);
        
        // Configurar el nombre del emisor solo para mensajes recibidos
        if (holder.tipoVista == TIPO_MENSAJE_RECIBIDO && holder.tvEmisor != null) {
            holder.tvEmisor.setText(mensaje.getEmisor());
            holder.tvEmisor.setVisibility(View.VISIBLE);
        }
        
        // Marcar como leído si es necesario
        if (!mensaje.isLeido() && mensaje.getEmisor() != null && nombreUsuarioActual != null && !mensaje.getEmisor().trim().equalsIgnoreCase(nombreUsuarioActual.trim())) {
            mensaje.setLeido(true);
            // Aquí podrías actualizar el estado en Firebase
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public void actualizarMensajes(ArrayList<Mensaje> nuevosMensajes) {
        this.mensajes = nuevosMensajes;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para mostrar los datos de cada mensaje.
     */
    static class MensajeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMensaje, tvEmisor, tvHora;
        int tipoVista;

        MensajeViewHolder(View itemView, int tipoVista) {
            super(itemView);
            this.tipoVista = tipoVista;
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvHora = itemView.findViewById(R.id.tvHora);
            
            // Solo buscar tvEmisor si es un mensaje recibido
            if (tipoVista == TIPO_MENSAJE_RECIBIDO) {
                tvEmisor = itemView.findViewById(R.id.tvEmisor);
            }
        }
    }
}

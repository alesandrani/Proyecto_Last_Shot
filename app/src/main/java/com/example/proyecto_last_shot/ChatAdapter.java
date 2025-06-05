package com.example.proyecto_last_shot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adaptador para mostrar mensajes en un ListView de chat.
 * Usa el layout que contiene TextViews: tvRemitente y tvContenido.
 */
public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<Mensaje> listaMensajes;
    private String nombreActual;

    /**
     * Constructor del adaptador.
     * @param context Contexto de la actividad o fragmento
     * @param listaMensajes Lista de mensajes a mostrar
     * @param nombreActual Nombre del usuario actual para diferenciar mensajes enviados y recibidos
     */
    public ChatAdapter(Context context, List<Mensaje> listaMensajes, String nombreActual) {
        this.context = context;
        this.listaMensajes = listaMensajes;
        this.nombreActual = nombreActual;
    }

    @Override
    public int getCount() {
        return listaMensajes.size();
    }

    @Override
    public Mensaje getItem(int position) {
        return listaMensajes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Configura la vista para cada mensaje en el ListView.
     * @param position Posición del mensaje en la lista
     * @param convertView Vista reutilizable
     * @param parent Vista padre
     * @return Vista configurada para el mensaje
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflar el layout solo si no existe la vista reutilizable
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mensaje, parent, false);
            // Cambia "item_mensaje" por el nombre correcto de tu layout XML
        }

        TextView tvRemitente = convertView.findViewById(R.id.tvRemitente);
        TextView tvContenido = convertView.findViewById(R.id.tvContenido);

        Mensaje mensaje = getItem(position);

        if (mensaje != null) {
            // Mostrar el remitente solo si no es el usuario actual
            if (mensaje.getEmisor().equals(nombreActual)) {
                tvRemitente.setVisibility(View.GONE); // No mostrar remitente si es el propio usuario
                // Aquí podrías personalizar el estilo del mensaje enviado
            } else {
                tvRemitente.setVisibility(View.VISIBLE);
                tvRemitente.setText(mensaje.getEmisor());
                // Aquí podrías personalizar el estilo del mensaje recibido
            }

            // Mostrar el texto del mensaje
            tvContenido.setText(mensaje.getTexto());
        }

        return convertView;
    }
}

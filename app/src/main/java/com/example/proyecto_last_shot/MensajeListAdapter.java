package com.example.proyecto_last_shot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Adaptador para mostrar mensajes en ListView.
 * Distingue entre mensajes del usuario y de otros.
 */
public class MensajeListAdapter extends ArrayAdapter<String> {

    private String nombreJugador;

    public MensajeListAdapter(@NonNull Context context, List<String> mensajes, String nombreJugador) {
        super(context, 0, mensajes);
        this.nombreJugador = nombreJugador;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String mensaje = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView tvMensaje = convertView.findViewById(android.R.id.text1);

        // Colorea mensajes del usuario de azul, otros de negro
        if (mensaje != null && mensaje.startsWith(nombreJugador + ":")) {
            tvMensaje.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
        } else {
            tvMensaje.setTextColor(getContext().getResources().getColor(android.R.color.black));
        }

        tvMensaje.setText(mensaje);

        return convertView;
    }
}

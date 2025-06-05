package com.example.proyecto_last_shot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ActivityChatSocket extends AppCompatActivity {
    private TextView mensajesChat;
    private EditText editMensaje;
    private Button btnEnviar;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private String SERVER_IP = "192.168.0.34";
    private int SERVER_PORT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_socket);

        mensajesChat = findViewById(R.id.mensajesChat);
        editMensaje = findViewById(R.id.editMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        new Thread(this::iniciarConexion).start();

        btnEnviar.setOnClickListener(v -> {
            String mensaje = editMensaje.getText().toString().trim();
            if (!mensaje.isEmpty()) {
                new Thread(() -> writer.println(mensaje)).start();
                editMensaje.setText("");
            }
        });
    }

    private void iniciarConexion() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            // Enviar nombre del jugador (puedes personalizarlo o pasarlo por Intent)
            writer.println("JugadorAndroid");

            runOnUiThread(() -> mensajesChat.append("Conectado al servidor\n"));

            String linea;
            while ((linea = reader.readLine()) != null) {
                String finalLinea = linea;
                runOnUiThread(() -> mensajesChat.append(finalLinea + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> mensajesChat.append("Error de conexión: " + e.getMessage() + "\n"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

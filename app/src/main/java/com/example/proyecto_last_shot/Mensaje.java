package com.example.proyecto_last_shot;

/**
 * Clase que representa un mensaje en el chat, ya sea grupal o privado.
 *
 * Un mensaje contiene:
 * - id: ID único del mensaje
 * - texto: el contenido del mensaje
 * - emisor: el nombre del jugador que envía el mensaje
 * - receptor: el nombre del jugador receptor (null si es chat grupal)
 * - timestamp: marca temporal de cuándo se envió el mensaje (en milisegundos)
 * - salaId: ID de la sala (para chat grupal)
 * - leido: estado de lectura del mensaje
 * - tipo: "privado" o "grupal"
 */
public class Mensaje {
    private String id;           // ID único del mensaje
    private String texto;        // Contenido del mensaje
    private String emisor;       // Nombre del jugador que envía el mensaje
    private String receptor;     // Nombre del jugador receptor (null si es chat grupal)
    private long timestamp;      // Marca temporal del mensaje
    private String salaId;       // ID de la sala (para chat grupal)
    private boolean leido;       // Estado de lectura del mensaje
    private String tipo;         // "privado" o "grupal"

    // Constructor vacío requerido para Firebase.
    public Mensaje() {}

    public Mensaje(String texto, String emisor, String receptor, long timestamp, String salaId, String tipo) {
        this.texto = texto;
        this.emisor = emisor;
        this.receptor = receptor;
        this.timestamp = timestamp;
        this.salaId = salaId;
        this.tipo = tipo;
        this.leido = false;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getEmisor() { return emisor; }
    public void setEmisor(String emisor) { this.emisor = emisor; }

    public String getReceptor() { return receptor; }
    public void setReceptor(String receptor) { this.receptor = receptor; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getSalaId() { return salaId; }
    public void setSalaId(String salaId) { this.salaId = salaId; }

    public boolean isLeido() { return leido; }
    public void setLeido(boolean leido) { this.leido = leido; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}

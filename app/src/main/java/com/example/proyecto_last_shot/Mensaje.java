package com.example.proyecto_last_shot;

/**
 * Clase que representa un mensaje en el chat, ya sea grupal o privado.
 *
 * Un mensaje contiene:
 * - texto: el contenido del mensaje
 * - emisor: el nombre del jugador que envía el mensaje
 * - receptor: el nombre del jugador receptor (null si es chat grupal)
 * - timestamp: marca temporal de cuándo se envió el mensaje (en milisegundos)
 */
public class Mensaje {
    private String texto;
    private String emisor;
    private String receptor; // null para mensajes de chat grupal
    private long timestamp;

    /**
     * Constructor vacío requerido para Firebase.
     */
    public Mensaje() {}

    /**
     * Constructor con todos los parámetros.
     *
     * @param texto     Contenido del mensaje.
     * @param emisor    Nombre del usuario que envía el mensaje.
     * @param receptor  Nombre del usuario receptor. Null si chat grupal.
     * @param timestamp Tiempo en milisegundos en que se envió el mensaje.
     */
    public Mensaje(String texto, String emisor, String receptor, long timestamp) {
        this.texto = texto;
        this.emisor = emisor;
        this.receptor = receptor;
        this.timestamp = timestamp;
    }

    /**
     * Obtiene el texto del mensaje.
     * @return texto del mensaje.
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Modifica el texto del mensaje.
     * @param texto nuevo texto del mensaje.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Obtiene el nombre del emisor (quien envía el mensaje).
     * @return nombre del emisor.
     */
    public String getEmisor() {
        return emisor;
    }

    /**
     * Modifica el nombre del emisor.
     * @param emisor nuevo nombre del emisor.
     */
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    /**
     * Obtiene el nombre del receptor (a quién va dirigido el mensaje).
     * Null si es mensaje grupal.
     * @return nombre del receptor o null.
     */
    public String getReceptor() {
        return receptor;
    }

    /**
     * Modifica el nombre del receptor.
     * @param receptor nuevo nombre del receptor.
     */
    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    /**
     * Obtiene la marca temporal del mensaje.
     * @return timestamp en milisegundos.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Modifica la marca temporal del mensaje.
     * @param timestamp nuevo timestamp en milisegundos.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

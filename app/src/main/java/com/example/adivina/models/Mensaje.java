package com.example.adivina.models;

public class Mensaje {
    private String id_usuario_remitente;
    private String id_usuario_receptor;
    private String mensaje;

    public Mensaje(){}

    public Mensaje(String id_usuario_remitente, String id_usuario_receptor, String mensaje) {
        this.id_usuario_remitente = id_usuario_remitente;
        this.id_usuario_receptor = id_usuario_receptor;
        this.mensaje = mensaje;
    }

    public void setId_usuario_remitente(String id_usuario_remitente) {
        this.id_usuario_remitente = id_usuario_remitente;
    }

    public void setId_usuario_receptor(String id_usuario_receptor) {
        this.id_usuario_receptor = id_usuario_receptor;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getId_usuario_remitente() {
        return id_usuario_remitente;
    }

    public String getId_usuario_receptor() {
        return id_usuario_receptor;
    }

    public String getMensaje() {
        return mensaje;
    }
}

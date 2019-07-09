package com.example.adivina.models;

public class Usuario {
    private String id;
    private String imagenUrl;
    private String UserName;
    private String correo;
    private String password;

    public Usuario(){}

    public Usuario(String id, String imagenUrl, String userName) {
        this.id = id;
        this.imagenUrl = imagenUrl;
        UserName = userName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getId() {
        return id;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

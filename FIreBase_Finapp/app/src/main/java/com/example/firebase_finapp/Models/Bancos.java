package com.example.firebase_finapp.Models;

public class Bancos {
    private String idbanco;
    private String nombres;
    private String telefono;
    private String fecharegistro;
    private long timestamp;
    private String Direccion;
    private String Horario;

    public String getIdbanco() {
        return idbanco;
    }

    public void setIdbanco(String idpersona) {
        this.idbanco = idpersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(String fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getHorario() {
        return Horario;
    }

    public void setHorario(String horario) {
        Horario = horario;
    }

    @Override
    public String toString() {
        return nombres;
    }
}

package com.movil.safep.Foro;

public class Usuario {
    private String usuario;

    public Usuario(){
    }

    public Usuario(String usuario){
        this.usuario=usuario;

    }

    public String getUsuario() {return usuario;}


    public void setUsuario(String usuario) {this.usuario = usuario;}


    public String toString() {
        return usuario;
    }
}

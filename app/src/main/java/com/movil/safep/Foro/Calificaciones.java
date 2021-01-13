package com.movil.safep.Foro;

public class Calificaciones {
    private String calificacion;
    public Calificaciones(){}
    public Calificaciones(String calificacion){
        this.calificacion=calificacion;
    }
    public String getCalificacion(){return calificacion;}
    public void setCalificacion(String calificacion) {this.calificacion = calificacion;}
    public String toString() {
        return calificacion;
    }
}

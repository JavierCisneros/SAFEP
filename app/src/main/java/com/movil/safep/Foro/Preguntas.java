package com.movil.safep.Foro;

public class Preguntas {
    private String pregunta;

    public Preguntas(){
    }

    public Preguntas(String pregunta){
        this.pregunta=pregunta;

    }

    public String getPregunta() {return pregunta;}


    public void setPregunta(String pregunta) {this.pregunta = pregunta;}


    public String toString() {
        return pregunta;
    }



}

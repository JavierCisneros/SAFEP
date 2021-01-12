package com.movil.safep.ui.home;

public class Gastos {
    private String Cantidad;

    public Gastos(){

    }
    public Gastos(String Cantidad){
        this.Cantidad=Cantidad;

    }
    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String Cantidad) {
        this.Cantidad = Cantidad;
    }
    @Override
    public String toString() {
        return Cantidad;
    }


}

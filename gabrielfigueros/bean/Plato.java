package org.gabrielfigueros.bean;


public class Plato {
    
    private int codigoPlatos;
    private int cantidad;
    private String nombrePlato;
    private String descripcionPlato;
    private double precioPlato;
    private int codigoTipoplato;
    
    public  Plato() {
    
    }

    public Plato(int codigoPlatos, int cantidad, String nombrePlato, String descripcionPlato, double precioPlato, int codigoTipoplato) {
        this.codigoPlatos = codigoPlatos;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.codigoTipoplato = codigoTipoplato;
    }

    public int getCodigoPlatos() {
        return codigoPlatos;
    }

    public void setCodigoPlatos(int codigoPlatos) {
        this.codigoPlatos = codigoPlatos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getCodigoTipoplato() {
        return codigoTipoplato;
    }

    public void setCodigoTipoplato(int codigoTipoplato) {
        this.codigoTipoplato = codigoTipoplato;
    }


    public String toString() {
        return getCodigoPlatos()+"| "+getNombrePlato();
    }
    
    
    
}

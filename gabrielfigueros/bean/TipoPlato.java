package org.gabrielfigueros.bean;

public class TipoPlato {
    private int codigoTipoplato;
    private String descripcion;

    public TipoPlato(){
        
    }
    
    public TipoPlato(int codigoTipoplato, String descripcion) {
        this.codigoTipoplato = codigoTipoplato;
        this.descripcion = descripcion;
    }

    public int getCodigoTipoplato() {
        return codigoTipoplato;
    }

    public void setCodigoTipoplato(int codigoTipoplato) {
        this.codigoTipoplato = codigoTipoplato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    public String toString() {
        return getCodigoTipoplato()+"| "+getDescripcion();
    }
    
    
}

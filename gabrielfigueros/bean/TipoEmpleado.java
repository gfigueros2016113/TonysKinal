package org.gabrielfigueros.bean;

public class TipoEmpleado {
    private int codigoTipoempleado;
    private String descripcion;


    public TipoEmpleado(){

    }

    public TipoEmpleado(int codigoTipoempleado, String descripcion) {
        this.codigoTipoempleado = codigoTipoempleado;
        this.descripcion = descripcion;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoempleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoempleado = codigoTipoEmpleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String toString() {
        return getCodigoTipoEmpleado()+"| "+getDescripcion();
    }
    
}
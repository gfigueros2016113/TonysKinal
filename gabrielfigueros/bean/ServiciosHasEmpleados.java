package org.gabrielfigueros.bean;

import java.util.Date;

public class ServiciosHasEmpleados {
    private int codigoServicioempleados;
    private Date fechaEvento;
    private String horaEvento;
    private String lugarEvento;
    private int codigoServicios;
    private int codigoEmpleados;

    public ServiciosHasEmpleados() {
    }
    
    public ServiciosHasEmpleados(int codigoServicioempleados, Date fechaEvento, String horaEvento, String lugarEvento, int codigoServicios, int codigoEmpleados) {
        this.codigoServicioempleados = codigoServicioempleados;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
        this.codigoServicios = codigoServicios;
        this.codigoEmpleados = codigoEmpleados;
    }

    public int getCodigoServicioempleados() {
        return codigoServicioempleados;
    }

    public void setCodigoServicioempleados(int codigoServicioempleados) {
        this.codigoServicioempleados = codigoServicioempleados;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

    public int getCodigoServicios() {
        return codigoServicios;
    }

    public void setCodigoServicios(int codigoServicios) {
        this.codigoServicios = codigoServicios;
    }

    public int getCodigoEmpleados() {
        return codigoEmpleados;
    }

    public void setCodigoEmpleados(int codigoEmpleados) {
        this.codigoEmpleados = codigoEmpleados;
    }

    @Override
    public String toString() {
        return "ServiciosHasEmpleados{" + "codigoServicioempleados=" + codigoServicioempleados + '}';
    }
    
    
}

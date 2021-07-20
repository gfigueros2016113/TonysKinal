package org.gabrielfigueros.bean;


public class Empleados {
   private int codigoEmpleados;
   private String apellidosEmpleado ;
   private String nombreEmpleado;
   private String direccionEmpleado;
   private String telefonoContacto;
   private String gradoCocinero;
   private int codigoTipoempleado;

   
   public Empleados(){
    
}
   
    public Empleados(int codigoEmpleados, String apellidosEmpleado, String nombreEmpleado, String direccionEmpleado, String telefonoContacto, String gradoCocinero, int codigoTipoempleado) {
        this.codigoEmpleados = codigoEmpleados;
        this.apellidosEmpleado = apellidosEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.direccionEmpleado = direccionEmpleado;
        this.telefonoContacto = telefonoContacto;
        this.gradoCocinero = gradoCocinero;
        this.codigoTipoempleado = codigoTipoempleado;
    }

   
   
   
    public int getCodigoEmpleados() {
        return codigoEmpleados;
    }

    public void setCodigoEmpleados(int codigoEmpleados) {
        this.codigoEmpleados = codigoEmpleados;
    }

    public String getApellidosEmpleado() {
        return apellidosEmpleado;
    }

    public void setApellidosEmpleado(String apellidosEmpleado) {
        this.apellidosEmpleado = apellidosEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getDireccionEmpleado() {
        return direccionEmpleado;
    }

    public void setDireccionEmpleado(String direccionEmpleado) {
        this.direccionEmpleado = direccionEmpleado;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getGradoCocinero() {
        return gradoCocinero;
    }

    public void setGradoCocinero(String gradoCocinero) {
        this.gradoCocinero = gradoCocinero;
    }

    public int getCodigoTipoempleado() {
        return codigoTipoempleado;
    }

    public void setCodigoTipoempleado(int codigoTipoempleado) {
        this.codigoTipoempleado = codigoTipoempleado;
    }

 
    public String toString() {
        return getCodigoEmpleados()+"| "+getNombreEmpleado();
    }
    
   
    

}



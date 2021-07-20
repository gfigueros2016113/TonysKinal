package org.gabrielfigueros.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.gabrielfigueros.system.MainApp;


public class ModuloController  implements Initializable{
    
    private MainApp escenarioPrincipal;
  
    public MainApp getEscenarioPrincipal() {
       return escenarioPrincipal;
    }

    public void setEscenarioMainApp(MainApp escenarioPrincipal) {
    this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void Return(){
        escenarioPrincipal.MainApp();
    }
    
     public void ModuloEmpresa(){
        escenarioPrincipal.ModuloEmpresa();
    }
     
    public void Tipo_plato(){
         escenarioPrincipal.Tipo_plato();
     }
     

    public void TipoEmpleado(){
        escenarioPrincipal.TipoEmpleado();
    }
    
       public void Productos(){
         escenarioPrincipal.Productos();
     }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  

   
}

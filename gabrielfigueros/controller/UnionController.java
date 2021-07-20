package org.gabrielfigueros.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.gabrielfigueros.system.MainApp;


public class UnionController  implements Initializable{
    
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
    
    public void ProductosHasPlatos(){
         escenarioPrincipal.ProductosHasPlatos();
     }
       
    public void ServiciosHasPlatos(){
         escenarioPrincipal.ServiciosHasPlatos();
     }
    
    public void ServiciosHasEmpleados(){
         escenarioPrincipal.ServiciosHasEmpleados();
     }  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  

}

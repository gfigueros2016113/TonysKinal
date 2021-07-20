package org.gabrielfigueros.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.gabrielfigueros.system.MainApp;


public class AcercaDeController implements Initializable{
    
        @FXML private Button btnReturn;

    
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
    
    public void menuPrincipal(){
    escenarioPrincipal.MainApp();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  

   
}

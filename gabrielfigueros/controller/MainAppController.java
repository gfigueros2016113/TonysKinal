package org.gabrielfigueros.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.gabrielfigueros.system.MainApp;

public class MainAppController implements Initializable{
    private MainApp escenarioMainApp;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public MainApp getEscenarioMainApp() {
        return escenarioMainApp;
    }

    public void setEscenarioMainApp(MainApp escenarioMainApp) {
        this.escenarioMainApp = escenarioMainApp;
    }
    
      public void AcercaDe(){
        escenarioMainApp.AcercaDe();
    }
    
    public void Modulo(){
        escenarioMainApp.Modulo();
    }
    
    public void Union(){
        escenarioMainApp.Union();
    }
}


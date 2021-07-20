package org.gabrielfigueros.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.gabrielfigueros.controller.AcercaDeController;
import org.gabrielfigueros.controller.MainAppController;
import org.gabrielfigueros.controller.EmpresaController;
import org.gabrielfigueros.controller.TipoEmpleadoController;
import org.gabrielfigueros.controller.EmpleadoController;
import org.gabrielfigueros.controller.ModuloController;
import org.gabrielfigueros.controller.PlatoController;
import org.gabrielfigueros.controller.PresupuestoController;
import org.gabrielfigueros.controller.ProductosController;
import org.gabrielfigueros.controller.ProductosHasPlatosController;
import org.gabrielfigueros.controller.ServiciosController;
import org.gabrielfigueros.controller.ServiciosHasEmpleadosController;
import org.gabrielfigueros.controller.ServiciosHasPlatosController;
import org.gabrielfigueros.controller.TipoPlatoController;
import org.gabrielfigueros.controller.UnionController;


public class MainApp extends Application {
    private final String PAQUETE_VISTA = "/org/gabrielfigueros/view/";
    private Stage escenarioMainApp;
    private Scene escena;
     @Override
         public void start(Stage escenarioMainApp) throws Exception {
            escenarioMainApp.setResizable(false);
            this.escenarioMainApp = escenarioMainApp;
            this.escenarioMainApp.setTitle("Tony's Kinal App");
            escenarioMainApp.getIcons().add(new Image("/org/gabrielfigueros/img/LOGO REPORTE.jpg"));
             MainApp();
            escenarioMainApp.show();
    }
    
     
    public void MainApp(){
        try{
            MainAppController MainApp = (MainAppController)cambiarEscena("MainAppView.fxml",600,400);
            MainApp.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }  
    
    public void ModuloEmpresa(){
        try{
            EmpresaController empresa = (EmpresaController)cambiarEscena("ModuloEmpresaView.fxml",600,407);
            empresa.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void TipoEmpleado(){
        try{
            TipoEmpleadoController TipoEmpleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",600,400);
            TipoEmpleado.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void Empleados(){
        try{
            EmpleadoController Empleados = (EmpleadoController)cambiarEscena("EmpleadosView.fxml",600,400);
            Empleados.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void Modulo(){
        try{
            ModuloController Modulo = (ModuloController)cambiarEscena("ModulosView.fxml",600,400);
            Modulo.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void Union(){
        try{
            UnionController Union = (UnionController)cambiarEscena("UnionView.fxml",630,350);
            Union.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ProductosHasPlatos(){
        try{
            ProductosHasPlatosController ProductosHasPlatos = (ProductosHasPlatosController)cambiarEscena("ProductosHasPlatos.fxml",600,400);
            ProductosHasPlatos.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ServiciosHasPlatos(){
        try{
            ServiciosHasPlatosController ServiciosHasPlatos = (ServiciosHasPlatosController)cambiarEscena("ServiciosHasPlatosView.fxml",600,400);
            ServiciosHasPlatos.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void ServiciosHasEmpleados(){
        try{
            ServiciosHasEmpleadosController ServiciosHasEmpleados = (ServiciosHasEmpleadosController)cambiarEscena("ServiciosHasEmpleadosView.fxml",600,400);
            ServiciosHasEmpleados.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
   public void Servicios(){
       try{
           ServiciosController Servicios = (ServiciosController)cambiarEscena("ServiciosView.fxml",600,400);
           Servicios.setEscenarioMainApp(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   
   public void Presupuesto(){
       try{
          PresupuestoController Presupuesto = (PresupuestoController)cambiarEscena("PresupuestoView.fxml",600,400);
           Presupuesto.setEscenarioMainApp(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   
      public void Tipo_plato(){
       try{
          TipoPlatoController Tipo_plato = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml",600,400);
           Tipo_plato.setEscenarioMainApp(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
      
    public void Productos(){
       try{
          ProductosController Productos = (ProductosController)cambiarEscena("ProductosView.fxml",600,400);
           Productos.setEscenarioMainApp(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }  
    
    public void Plato(){
       try{
          PlatoController Plato = (PlatoController)cambiarEscena("PlatosView.fxml",600,400);
           Plato.setEscenarioMainApp(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
    
     public void AcercaDe(){
        try{
            AcercaDeController AcercaDe = (AcercaDeController)cambiarEscena("AcercaDeView.fxml",600,400);
            AcercaDe.setEscenarioMainApp(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
  
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena (String fxml,int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = MainApp.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(MainApp.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioMainApp.setScene(escena);
        escenarioMainApp.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();        
        return resultado;
    }

    
}
  
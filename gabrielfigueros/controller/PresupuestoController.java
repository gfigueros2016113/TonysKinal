package org.gabrielfigueros.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.gabrielfigueros.bd.Conexion;
import org.gabrielfigueros.bean.Empresa;
import org.gabrielfigueros.bean.Presupuesto;
import org.gabrielfigueros.report.GenerarReporte;

import org.gabrielfigueros.system.MainApp;

public class PresupuestoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Presupuesto>listaPresupuesto;
    private ObservableList<Empresa>listaEmpresa;
    private DatePicker fechaSolicitud;
            
    private MainApp escenarioPrincipal;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private TableView tblPresupuesto;    
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;    
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private ComboBox  cmbCodigoEmpresas;
    @FXML private GridPane grpFechaSolicitud;
    
    public void cargarDatos(){
        
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto,Date >("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresas"));
        cmbCodigoEmpresas.setItems(getEmpresa());
        desactivarControles();
        
    }
    
   public void Nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
           activarControles();
           limpiarControles();
           btnNuevo.setText("Guardar");
           btnEliminar.setText("Cancelar");
           btnEditar.setDisable(true);
           btnReporte.setDisable(true);
           tipoDeOperacion = operaciones.GUARDAR;
           break;
            case  GUARDAR:  
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }     
       
    }     
    
       public void guardar(){
        Presupuesto registro= new Presupuesto();
        registro.setFechaSolicitud( fechaSolicitud.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresas(((Empresa)cmbCodigoEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresas());
        try{
            PreparedStatement procedimiento=Conexion.getInstance().getConexion().prepareCall("{call Sp_Agregar_Presupuesto(?,?,?)}");
             procedimiento.setDate(1,new java.sql.Date(registro.getFechaSolicitud().getTime()));
             procedimiento.setDouble(2, registro.getCantidadPresupuesto());
             procedimiento.setInt(3, registro.getCodigoEmpresas());
             procedimiento.execute();
             listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
       
    public void  Eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = PresupuestoController.operaciones.NUEVO;
                break;
            default:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null ){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?","Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                try{
                   PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Presupuesto(?)}");
                   sp.setInt(1,((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                   sp.execute(); 
                   listaEmpresa.remove(tblPresupuesto.getSelectionModel().getSelectedIndex());
                   limpiarControles();
                   JOptionPane.showMessageDialog(null,"Presupuesto eliminado correctamente");
                   cargarDatos();

                }catch(Exception e){
                    e.printStackTrace();

                }

                }
                }else{
                JOptionPane.showMessageDialog(null,"Debe Selecionar el registro a eliminar");

                }
        }
  }
    
   public void Editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null,"Debe seleccionar un presupuesto a actualizar");
                    }
                break;
                case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                break;
            }
        }
   
    public void Reporte(){
         switch(tipoDeOperacion){
             case NINGUNO:
               if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione un presupuesto");
                    }
                 break;
            case ACTUALIZAR:
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = PresupuestoController.operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                break;     
         }
     }
    
    
     public void imprimirReporte(){
            
            Map parametros = new HashMap();
            int OlaReina = ((Empresa)cmbCodigoEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresas();
            parametros.put("CodEmpresa",OlaReina);
            GenerarReporte.mostrarReporte("ReportePresupuestoGF.jasper", "Reporte de Presupuestos", parametros);
     }
    
        
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Presupuesto(?,?,?,?)}");
            Presupuesto registro = (Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud((java.util.Date) fechaSolicitud.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setCodigoEmpresas(((Empresa)cmbCodigoEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresas());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2,new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresas());
       
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Datos Actualizados");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ObservableList<Presupuesto> getPresupuesto(){
        
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Presupuesto()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaSolicitud"),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresas")));
                
            }
            
        }catch(Exception e){
            
            e.printStackTrace();
            
        }
        
        return listaPresupuesto = FXCollections.observableArrayList(lista);
        
    }
   
     public ObservableList<Empresa> getEmpresa(){
        
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Empresas()}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next()){
                
                lista.add(new Empresa(resultado.getInt("codigoEmpresas"),resultado.getString("nombreEmpresa"),resultado.getString("direccion"),resultado.getString("telefono")));
                
            }
            
        }catch(Exception e){
            
            e.printStackTrace();
            
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
        
    }
    

     public void seleccionarElemento(){
     txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
    fechaSolicitud.selectedDateProperty().set(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
    txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
    cmbCodigoEmpresas.getSelectionModel().select((((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresas()));

     }
     
     
  
     
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
      
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      cargarDatos();
         fechaSolicitud = new DatePicker(Locale.ENGLISH);
        fechaSolicitud.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
         fechaSolicitud.getCalendarView().todayButtonTextProperty().set("Today");
         fechaSolicitud.getCalendarView().todayButtonTextProperty().set("Today");
         grpFechaSolicitud.add(fechaSolicitud,0,0);    
         fechaSolicitud.getStylesheets().add("/org/gabrielfigueros/resource/DatePicker.css");
    }
    
    public void desactivarControles(){
    txtCodigoPresupuesto.setEditable(false);
    txtCantidadPresupuesto.setEditable(false);
    grpFechaSolicitud.setDisable(true);
    cmbCodigoEmpresas.setEditable(false);
    
    }
    
    public void activarControles(){
    txtCodigoPresupuesto.setEditable(false);
    txtCantidadPresupuesto.setEditable(true);
    grpFechaSolicitud.setDisable(false);
    cmbCodigoEmpresas.setEditable(false);
    
   
    }
    
    public void limpiarControles(){
    txtCodigoPresupuesto.setText("");
    txtCantidadPresupuesto.setText("");
    cmbCodigoEmpresas.getSelectionModel().clearSelection();
    
    
    }
    
    
    public void Return(){
        escenarioPrincipal.ModuloEmpresa();
    }
     
    public MainApp getEscenarioMainApp() {
        return escenarioPrincipal;
    }
    
    
    public void setEscenarioMainApp(MainApp escenarioMainApp) {
        this.escenarioPrincipal = escenarioMainApp;
    }
    
}


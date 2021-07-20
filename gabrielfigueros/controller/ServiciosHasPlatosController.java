package org.gabrielfigueros.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.gabrielfigueros.bd.Conexion;
import org.gabrielfigueros.bean.Plato;
import org.gabrielfigueros.bean.Servicios;
import org.gabrielfigueros.bean.ServiciosHasPlatos;
import org.gabrielfigueros.system.MainApp;


public class ServiciosHasPlatosController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, ELIMINAR, AGREGAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<ServiciosHasPlatos> listaSHP;
    ObservableList<Servicios> listaServicio;
    ObservableList<Plato> listaPlato;
    
    @FXML private ComboBox cmbCodServicio;
    @FXML private ComboBox cmbCodPlato;
    @FXML private TableView tblServiciosYPlatos;
    @FXML private TableColumn colCodServicio;
    @FXML private TableColumn colCodPlato;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        limpiarControles();
        cargarDatos();
        cmbCodServicio.setItems(getServicio());
        cmbCodPlato.setItems(getPlato());
    }
    
    public void seleccionarElemento(){
        cmbCodServicio.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos)tblServiciosYPlatos.getSelectionModel().getSelectedItem()).getCodigoServicios()));
        cmbCodPlato.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos)tblServiciosYPlatos.getSelectionModel().getSelectedItem()).getCodigoPlatos()));
    }
    
    public void cargarDatos(){
        tblServiciosYPlatos.setItems(getServicio_has_Plato());
        colCodServicio.setCellValueFactory(new PropertyValueFactory <ServiciosHasPlatos, Integer>("codigoServicios"));
        colCodPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoPlatos"));
    }
    
    public Servicios buscarServicio(int codigoServicio){
        Servicios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Buscar_Servicios(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicios(registro.getInt("codigoServicios"),
                                            registro.getDate("fechaServicio"),
                                            registro.getString("tipoServicio"),
                                            registro.getString("horaServicio"),
                                            registro.getString("lugarServicio"),
                                            registro.getString("telefonoContacto"),
                                            registro.getInt("codigoEmpresas") );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Buscar_Platos(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlatos"),
                                        registro.getInt("cantidad"),
                                        registro.getString("nombrePlato"),
                                        registro.getString("descripcionPlato"),
                                        registro.getDouble("precioPlato"),
                                        registro.getInt("codigoTipoplato") );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    
    
    
    public ObservableList<ServiciosHasPlatos> getServicio_has_Plato(){
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Servicios_has_platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServiciosHasPlatos(resultado.getInt("codigoServicios"),
                                                   resultado.getInt("codigoPlatos") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaSHP = FXCollections.observableArrayList(lista);
    }
    
     public ObservableList<Servicios> getServicio(){
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Servicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios(resultado.getInt("codigoServicios"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoServicio"),
                                        resultado.getString("horaServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresas")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio = FXCollections.observableArrayList(lista);
    }
     
     public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlatos"),
                                        resultado.getInt("cantidad"),
                                        resultado.getString("nombrePlato"),
                                        resultado.getString("descripcionPlato"),
                                        resultado.getDouble("precioPlato"),
                                        resultado.getInt("codigoTipoplato") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPlato = FXCollections.observableArrayList(lista);
    }
     
   
    
    public void limpiarControles(){
        cmbCodServicio.getSelectionModel().clearSelection();
        cmbCodPlato.getSelectionModel().clearSelection();
    }
    
    public MainApp getEscenarioMainApp() {
        return escenarioPrincipal;
    }
    
    
    public void setEscenarioMainApp(MainApp escenarioMainApp) {
        this.escenarioPrincipal = escenarioMainApp;
    }
    
     public void Return(){
        escenarioPrincipal.Union();
    }
     
    
   
}
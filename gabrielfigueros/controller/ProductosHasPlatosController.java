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
import org.gabrielfigueros.bean.Productos;
import org.gabrielfigueros.bean.ProductosHasPlatos;
import org.gabrielfigueros.system.MainApp;

public class ProductosHasPlatosController implements Initializable {
    private MainApp escenarioPrincipal;
    private enum operaciones{NINGUNO, AGREGAR, GUARDAR, ELIMINAR, CANCELAR, EDITAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<ProductosHasPlatos> listaPHP;
    ObservableList<Productos> listaProducto;
    ObservableList<Plato> listaPlato;
    @FXML private ComboBox cmbCodProducto;
    @FXML private ComboBox cmbCodPlato;
    @FXML private TableView tblProductosYPlatos;
    @FXML private TableColumn colCodProducto;
    @FXML private TableColumn colCodPlato;
   
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        limpiarControles();
        cargarDatos();
        cmbCodProducto.setItems(getProducto());
        cmbCodPlato.setItems(getPlato());
    }
    
    public void seleccionarElemento(){
        cmbCodProducto.getSelectionModel().select(buscarProducto(((ProductosHasPlatos)tblProductosYPlatos.getSelectionModel().getSelectedItem()).getCodigoProductos()));
        cmbCodPlato.getSelectionModel().select(buscarPlato(((ProductosHasPlatos)tblProductosYPlatos.getSelectionModel().getSelectedItem()).getCodigoPlatos()));
    }  
    
    public Productos buscarProducto(int codigoProducto){
        Productos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Buscar_Productos(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Productos(registro.getInt("codigoProductos"),
                                            registro.getString("nombreProducto"),
                                            registro.getInt("cantidad") );
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
    
    public void cargarDatos(){
        tblProductosYPlatos.setItems(getProducto_has_Plato());
        colCodProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoProductos"));
        colCodPlato.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoPlatos"));
    }
    
    public ObservableList<ProductosHasPlatos> getProducto_has_Plato(){
        ArrayList<ProductosHasPlatos> lista = new ArrayList<ProductosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Productos_has_platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ProductosHasPlatos(resultado.getInt("codigoProductos"),
                                                    resultado.getInt("codigoPlatos") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPHP = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Productos> getProducto(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Productos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos(resultado.getInt("codigoProductos"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("cantidad") ));   
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return listaProducto = FXCollections.observableArrayList(lista);
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
        cmbCodProducto.getSelectionModel().clearSelection();
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

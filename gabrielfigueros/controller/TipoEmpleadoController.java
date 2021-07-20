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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.gabrielfigueros.system.MainApp;
import org.gabrielfigueros.bean.TipoEmpleado;
import org.gabrielfigueros.bd.Conexion;

public class TipoEmpleadoController implements Initializable{

    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, CANCELAR, ACTUALIZAR, NINGUNO}
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    private MainApp escenarioPrincipal;
    private ObservableList <TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtCodigoDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEmpleados;

    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private TableView tblTipoEmpleado;
    
   
public void activarControles(){
    txtDescripcion.setEditable(true);
     
}
public MainApp getEscenarioMainApp() {
    return escenarioPrincipal;
}

public void setEscenarioMainApp(MainApp escenarioPrincipal) {
    this.escenarioPrincipal = escenarioPrincipal;
}

public void desactivarControles(){
    txtDescripcion.setEditable(false);
    txtCodigoDescripcion.setEditable(false);      
}


public void limpiarControles(){
    txtDescripcion.setText(null);
    txtCodigoDescripcion.setText(null);      
}


public void seleccionarElemento(){
    TipoEmpleado TipoEmpleado = new TipoEmpleado();
    txtCodigoDescripcion.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
    txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
}


public void nuevo(){
    switch(tipoOperacion){
        case NINGUNO:
            activarControles();
            limpiarControles();
            btnNuevo.setText("Guardar");
            btnEliminar.setText("Cancelar");
            btnEditar.setVisible(false);
            btnReporte.setVisible(false);
            txtCodigoDescripcion.setVisible(false);
            tipoOperacion = Operacion.GUARDAR;
            break;
        case GUARDAR:
            guardar();
            activarControles();
            limpiarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setVisible(true);
            btnReporte.setVisible(true);
            txtCodigoDescripcion.setVisible(true);
            tipoOperacion = Operacion.NINGUNO;
            cargarDatos();
    }
}


public void eliminar(){
    switch(tipoOperacion){
        case GUARDAR:
            activarControles();
            limpiarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setVisible(true);
            btnReporte.setVisible(true);
            txtCodigoDescripcion.setVisible(true);
            tipoOperacion = Operacion.NINGUNO;
        break;
        default:
            if(tblTipoEmpleado.getSelectionModel().getSelectedItem()!=null){
            int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el elemento? Se eliminara de otras entidades","Eliminar Tipo Empleado" ,JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Tipo_empleado(?)}");
                    sp.setInt(1,((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                    sp.execute();
                    listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
                    limpiarControles();
                    JOptionPane.showMessageDialog(null, "Registro Eliminado");
                    }catch(Exception e){
                    e.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
            }   
    }
}

//Método para el botón Guardar
public void guardar(){
    TipoEmpleado registro = new TipoEmpleado();
    registro.setDescripcion(txtDescripcion.getText());
    try{
    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Agregar_Tipo_empleado(?)}");
    sp.setString(1,registro.getDescripcion());
    sp.execute();
    listaTipoEmpleado.add(registro);
    }catch(Exception e){
    e.printStackTrace();
    }
}

public void editar(){
            switch(tipoOperacion){
                case NINGUNO:
                    if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoOperacion = Operacion.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null,"Selecione la empresa a actualizar");
                    }
                break;
                case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = Operacion.NINGUNO;
                    cargarDatos();
                break;
            }
        }

public void reporte(){
    switch(tipoOperacion){
        case ACTUALIZAR:
            desactivarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setVisible(true);
            btnEliminar.setVisible(true);
            txtCodigoDescripcion.setVisible(true);
            tipoOperacion = Operacion.NINGUNO;
            break;
        case NINGUNO:
            JOptionPane.showMessageDialog(null, "Revisa detenidamente el código");
            break;
    }
}


public void actualizar(){
    try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Tipo_empleado(?,?)}");
        TipoEmpleado tipoActualizado = ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem());
        tipoActualizado.setDescripcion(txtDescripcion.getText());
                sp.setInt(1, tipoActualizado.getCodigoTipoEmpleado());
                sp.setString(2, tipoActualizado.getDescripcion());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
    
}

public void cargarDatos(){
  tblTipoEmpleado.setItems(getTipoEmpleado());
  colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
  colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
}

public ObservableList<TipoEmpleado> getTipoEmpleado(){
ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
    try{
        PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Tipo_empleado}");
        ResultSet resultado= procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"), 
                    resultado.getString("descripcion")));            
            }
    }catch(Exception e){
        e.printStackTrace();
    }
    return listaTipoEmpleado= FXCollections.observableArrayList(lista);
}
    
public MainApp getEscenarioPrincipal() {
    return escenarioPrincipal;
}

public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
    this.escenarioPrincipal = escenarioPrincipal;
}

public void Return(){
        escenarioPrincipal.Modulo();
    }

public void Empleados(){
        escenarioPrincipal.Empleados();
    }

public void menuPrincipal(){
    escenarioPrincipal.MainApp();
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    
    
}



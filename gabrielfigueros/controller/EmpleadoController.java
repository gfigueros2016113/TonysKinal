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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.gabrielfigueros.bean.Empleados;
import org.gabrielfigueros.bean.TipoEmpleado;
import org.gabrielfigueros.bd.Conexion;
import org.gabrielfigueros.system.MainApp;

public class EmpleadoController implements Initializable{
    
    @Override
    public void initialize(URL url,ResourceBundle rb){
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
    }
    
    
    private MainApp escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private ObservableList<Empleados> listaEmpleado;
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtGradoCocinero;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnReturn;

    
        public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Buscar_Tipo_empleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro  = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(
                                registro.getInt("codigoTipoempleado"),
                                registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Empleados> getEmpleado(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call   Sp_listar_Empleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleados (resultado.getInt("codigoEmpleados"),
                                       resultado.getString("apellidosEmpleado"),
                                       resultado.getString("nombreEmpleado"),
                                       resultado.getString("direccionEmpleado"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getString("gradoCocinero"),
                                       resultado.getInt("codigoTipoempleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
        public ObservableList<TipoEmpleado> getTipoEmpleado(){
            ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Tipo_empleado()}");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoEmpleado(resultado.getInt("codigoTipoempleado"),
                                                resultado.getString("descripcion")));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaTipoEmpleado = FXCollections.observableArrayList(lista);
        }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleados"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidosEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleados, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoTipoempleado"));
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados()));
        txtNombres.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado());
        txtApellidos.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtDireccion.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtGradoCocinero.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
        txtTelefono.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoempleado()));
    }
    
    
    public void guardar(){
        Empleados registro = new Empleados();
        //registro.setCodigoEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidos.getText());
        registro.setNombreEmpleado(txtNombres.getText());
        registro.setDireccionEmpleado(txtDireccion.getText());
        registro.setTelefonoContacto(txtTelefono.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoempleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Agregar_Empleados(?,?,?,?,?,?)}");
            procedimiento.setString(1,registro.getApellidosEmpleado());
            procedimiento.setString(2,registro.getNombreEmpleado());
            procedimiento.setString(3,registro.getDireccionEmpleado());
            procedimiento.setString(4,registro.getTelefonoContacto());
            procedimiento.setString(5,registro.getGradoCocinero());
            procedimiento.setInt(6,registro.getCodigoTipoempleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
                }
    }
    
    public void eliminarMetodo(){
        if(tblEmpleados.getSelectionModel().getSelectedItem() != null ){
            int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro que quiere eliminar el elemento? Se eliminara de otras entidades","Eliminar Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Empleados(?)}");
                        procedimiento.setInt(1,((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados());
                        procedimiento.execute();
                        listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }else{
            JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemnto");
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Empleados(?,?,?,?,?,?,?)}");
            Empleados registro = (Empleados)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setApellidosEmpleado(txtApellidos.getText());
            registro.setNombreEmpleado(txtNombres.getText());
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoempleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            
            procedimiento.setInt(1,registro.getCodigoEmpleados());
            procedimiento.setString(2,registro.getApellidosEmpleado());
            procedimiento.setString(3,registro.getNombreEmpleado());
            procedimiento.setString(4,registro.getDireccionEmpleado());
            procedimiento.setString(5,registro.getTelefonoContacto());
            procedimiento.setString(6,registro.getGradoCocinero());
            procedimiento.setInt(7,registro.getCodigoTipoempleado());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                desactivarID();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                cargarDatos();
                limpiarControles();
                normalidad();
                tipoOperacion = operaciones.NINGUNO;
            break;
        
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                normalidad();
                limpiarControles();
                tipoOperacion = operaciones.NINGUNO;
            break;
            default: 
                eliminarMetodo();
            break;
        
        }
    }
    
    public void editar(){
        switch (tipoOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    desactivarID();
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnEditar.setText("Guardar");
                    btnReporte.setText("Cancelar");
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                }
            break;
            case ACTUALIZAR:
                actualizar();
                normalidad();
                cargarDatos();
            break;
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                normalidad();
                limpiarControles();
                tipoOperacion = operaciones.NINGUNO;
            break;
            
        }
    }
    
    
    public void desactivarID(){
        txtCodigoEmpleado.setVisible(false);
    }
    

    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtDireccion.setEditable(true);
        txtGradoCocinero.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtDireccion.setEditable(false);
        txtGradoCocinero.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void normalidad(){
        txtCodigoEmpleado.setVisible(true);
        
        txtCodigoEmpleado.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtDireccion.setEditable(false);
        txtGradoCocinero.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(false);
        
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);

        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);  
        
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnReporte.setText("Reporte");
    }
    
    public void desactivarBotones(){
        btnNuevo.setVisible(false);
        btnEliminar.setVisible(false);
        btnEditar.setVisible(false);
        btnReporte.setVisible(false);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtGradoCocinero.setText("");
        txtTelefono.setText("");
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
    }
    
    
//////////////////////    GETERS Y SETTERS ESCENARIOS     //////////////////////    
    public MainApp getEscenarioMainApp() {
        return escenarioPrincipal;
    }

    public void setEscenarioMainApp(MainApp escenarioMainApp) {
        this.escenarioPrincipal = escenarioMainApp;
    }
    
    public void MainApp(){
        escenarioPrincipal.MainApp();
    }
    
    public void TipoEmpleado(){
        escenarioPrincipal.TipoEmpleado();
    }
    public void Return(){
        escenarioPrincipal.TipoEmpleado();
    }
   
    
}

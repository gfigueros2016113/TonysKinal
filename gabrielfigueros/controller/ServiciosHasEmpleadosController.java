package org.gabrielfigueros.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.gabrielfigueros.bean.Empleados;
import org.gabrielfigueros.bean.Servicios;
import org.gabrielfigueros.bean.ServiciosHasEmpleados;
import org.gabrielfigueros.system.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.gabrielfigueros.bd.Conexion;

public class ServiciosHasEmpleadosController implements Initializable{
   
    
    private MainApp escenarioPrincipal;
    private enum operaciones{NINGUNO, AGREGAR,GUARDAR, ELIMINAR, CANCELAR, EDITAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<ServiciosHasEmpleados> listaSHE;
    ObservableList<Servicios> listaServicio;
    ObservableList<Empleados> listaEmpleado;
    private DatePicker fechaE;
    boolean vacio = false;
    @FXML private GridPane grpFechaEvento;
    @FXML private Label lblcodServiciosYEmpleados;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtCodServiciosYempleados;
    @FXML private TextField txtLugarEvento;
    @FXML private ComboBox cmbCodServicio;
    @FXML private ComboBox cmbCodEmpleado;
    @FXML private TableView tblServiciosYEmpleados;
    @FXML private TableColumn colCodServiciosYEmpleados;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colCodServicio;
    @FXML private TableColumn colCodEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;

     @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fechaE = new DatePicker(Locale.ENGLISH);
        fechaE.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fechaE.getCalendarView().todayButtonTextProperty().set("Today");
        fechaE.getStylesheets().add("/org/gabrielfigueros/resource/DatePicker.css");
        grpFechaEvento.add(fechaE, 0, 0);
        cmbCodServicio.setItems(getServicio());
        cmbCodEmpleado.setItems(getEmpleado());
    }
    
    public void seleccionarElemento(){
        if(tblServiciosYEmpleados.getSelectionModel().getSelectedItem() != null){
            txtCodServiciosYempleados.setText(String.valueOf(((ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicioempleados()));   
            fechaE.selectedDateProperty().set(((ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            txtHoraEvento.setText(((ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento());
            txtLugarEvento.setText(((ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
            cmbCodServicio.getSelectionModel().select(buscarServicio(((ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicios()));
            cmbCodEmpleado.getSelectionModel().select(buscarEmpleado(((ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados()));
        }else{
            
        }
        
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
    
    public Empleados buscarEmpleado(int codigoEmpleado){
        Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Buscar_Empleados(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleados(registro.getInt("codigoEmpleados"),
                                            registro.getString("apellidosEmpleado"),
                                            registro.getString("nombreEmpleado"),
                                            registro.getString("direccionEmpleado"),
                                            registro.getString("telefonoContacto"),
                                            registro.getString("gradoCocinero"),
                                            registro.getInt("codigoTipoempleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void cargarDatos(){
        tblServiciosYEmpleados.setItems(getServicio_has_Empleado());
        colCodServiciosYEmpleados.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicioempleados"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("lugarEvento"));
        colCodServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicios"));
        colCodEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoEmpleados"));
        desactivarControles();
    }
    
    public ObservableList<ServiciosHasEmpleados> getServicio_has_Empleado(){
        ArrayList<ServiciosHasEmpleados> lista = new ArrayList<ServiciosHasEmpleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Servicios_has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServiciosHasEmpleados(resultado.getInt("codigoServicioempleados"),  
                                                    resultado.getDate("fechaEvento"),
                                                    resultado.getString("horaEvento"),
                                                    resultado.getString("lugarEvento"),
                                                    resultado.getInt("codigoServicios"),
                                                    resultado.getInt("codigoEmpleados")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaSHE = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Servicios> getServicio(){
        toString();
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
    
    public ObservableList<Empleados> getEmpleado(){
        toString();
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empleados(resultado.getInt("codigoEmpleados"), 
                                            resultado.getString("apellidosEmpleado"),
                                            resultado.getString("nombreEmpleado"),
                                            resultado.getString("direccionEmpleado"),
                                            resultado.getString("telefonoContacto"),
                                            resultado.getString("gradoCocinero"),
                                            resultado.getInt("codigoTipoempleado") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
  
public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                    Guardar();
                    limpiarControles();
                    desactivarControles();
                    activarBotones();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEliminar.setDisable(false);
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
            break;
        }
    }
    
    public void Guardar(){
        ServiciosHasEmpleados registro = new ServiciosHasEmpleados();
        registro.setCodigoServicios(((Servicios)cmbCodServicio.getSelectionModel().getSelectedItem()).getCodigoServicios());
        registro.setCodigoEmpleados(((Empleados)cmbCodEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleados());
        registro.setFechaEvento(fechaE.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Agregar_Servicios_has_empleados(?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(2, registro.getHoraEvento());
            procedimiento.setString(3, registro.getLugarEvento());
            procedimiento.setInt(4, registro.getCodigoServicios());
            procedimiento.setInt(5, registro.getCodigoEmpleados());
            procedimiento.execute();
            listaSHE.add(registro);
            cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles(); 
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                tipoDeOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblServiciosYEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Servicios_has_Empleados(?)}");
                        procedimiento.setInt(1,((ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicioempleados());
                        procedimiento.execute();
                        listaSHE.remove(tblServiciosYEmpleados.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento primero"); 
            }
            break;    
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblServiciosYEmpleados.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    cmbCodEmpleado.setDisable(true);
                    cmbCodServicio.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione el dato a editar");
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
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Servicios_has_empleados(?,?,?,?)}");
            ServiciosHasEmpleados registro = (ServiciosHasEmpleados)tblServiciosYEmpleados.getSelectionModel().getSelectedItem();
            registro.setFechaEvento(fechaE.getSelectedDate());
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            procedimiento.setInt(1, registro.getCodigoServicioempleados());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(3, registro.getHoraEvento());
            procedimiento.setString(4, registro.getLugarEvento()); 
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                activarBotones();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;

        }
    }
    
    public void activarBotones(){
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);

        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
    }

    public void desactivarBotones(){

        btnNuevo.setVisible(false);
        btnEliminar.setVisible(false);
        btnEditar.setVisible(false);
        btnReporte.setVisible(false);
    }


    public void activarControles(){
        grpFechaEvento.setDisable(false);
        lblcodServiciosYEmpleados.setDisable(true);
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
        cmbCodServicio.setDisable(false);
        cmbCodEmpleado.setDisable(false);
    }

    public void desactivarControles(){
        lblcodServiciosYEmpleados.setDisable(false);
        grpFechaEvento.setDisable(true);
        txtCodServiciosYempleados.setEditable(false);
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
        cmbCodServicio.setDisable(true);
        cmbCodEmpleado.setDisable(true);
    }

    public void limpiarControles(){
        fechaE.selectedDateProperty().set(null);
        txtCodServiciosYempleados.setText("");
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
        cmbCodServicio.getSelectionModel().clearSelection();
        cmbCodEmpleado.getSelectionModel().clearSelection();
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

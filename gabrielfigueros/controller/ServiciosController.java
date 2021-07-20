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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.gabrielfigueros.bd.Conexion;
import org.gabrielfigueros.bean.Empresa;
import org.gabrielfigueros.bean.Servicios;
import org.gabrielfigueros.report.GenerarReporte;
import org.gabrielfigueros.system.MainApp;


public class ServiciosController implements Initializable {
private MainApp escenarioPrincipal;
private enum operaciones {NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
private operaciones tipoDeOperacion = operaciones.NINGUNO;
private ObservableList<Servicios> listaServicio;
private ObservableList<Empresa> listaEmpresa;
private DatePicker fecha;
@FXML private TextField txtCodigoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private TextField txtTipoServicio;
    @FXML private GridPane grpFechaServicio;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colCodigoEmpresa;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().setShowWeeks(false);
        grpFechaServicio.add(fecha,0,0);
        fecha.getStylesheets().add("/org/gabrielfigueros/resource/DatePicker.css");
        cmbCodigoEmpresa.setItems(getEmpresa());
    }
    
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        grpFechaServicio.setDisable(true);
        cmbCodigoEmpresa.setEditable(false);
        cmbCodigoEmpresa.setDisable(true); 
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        grpFechaServicio.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
        
    }
    
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        fecha.selectedDateProperty().set(null);
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
        //cmbCodigoEmpresa.getSelectionModel().select(null);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                CargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Servicios registro = new Servicios();
        //registro.setCodigoServicios(Interger.parseInt(txtCodigoServicios.getText()));
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresas(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresas());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call Sp_Agregar_Servicios (?,?,?,?,?,?)}");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(2, registro.getTipoServicio());
                procedimiento.setString(3, registro.getHoraServicio());
                procedimiento.setString(4, registro.getLugarServicio());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setInt(6, registro.getCodigoEmpresas());
                procedimiento.execute();
                listaServicio.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblServicios.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro que quiere eliminar el elemento? Se eliminara de otras entidades", "Eliminar Servicio", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Servicios(?)}");
                            procedimiento.setInt(1, ((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicios());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServicios.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                           } 
                        }
                    }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
            actualizar();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnEditar.setDisable(false);
            btnEditar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
            CargarDatos();
            break;
        }
    }
    
    public void GenerarReporte(){
            switch(tipoDeOperacion){
                case ACTUALIZAR:
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = ServiciosController.operaciones.NINGUNO;
                    CargarDatos();
                break;
                case NINGUNO:
                    if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    imprimirReporte();
                    }else{
                        JOptionPane.showMessageDialog(null, "Seleccione un Servicio");
                    }
                 break;    
            }
        }
    
        public void imprimirReporte(){
            Map parametros = new HashMap();
            int OlaReina = (Integer.valueOf(txtCodigoServicio.getText()));
            parametros.put("CodServicio",OlaReina);
            GenerarReporte.mostrarReporte("ReporteServiciosGF.jasper", "Reporte de Servicios", parametros);
        }
      
    public void actualizar(){
        try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Servicios(?,?,?,?,?,?,?)}");
                Servicios registro = (Servicios)tblServicios.getSelectionModel().getSelectedItem();
                registro.setCodigoServicios(Integer.parseInt(txtCodigoServicio.getText()));
                registro.setFechaServicio(fecha.getSelectedDate());
                registro.setTipoServicio(txtTipoServicio.getText());
                registro.setHoraServicio(txtHoraServicio.getText());
                registro.setLugarServicio(txtLugarServicio.getText());
                registro.setTelefonoContacto(txtTelefonoContacto.getText());
                registro.setCodigoEmpresas(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresas());
                procedimiento.setInt(1, registro.getCodigoServicios());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(3, registro.getTipoServicio());
                procedimiento.setString(4, registro.getHoraServicio());
                procedimiento.setString(5, registro.getLugarServicio());
                procedimiento.setString(6, registro.getTelefonoContacto());
                procedimiento.setInt(7, registro.getCodigoEmpresas());
                procedimiento.execute();
                listaServicio.add(registro);
                CargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void CargarDatos(){
        tblServicios.setItems(getServicios());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios,Integer>("codigoServicios"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicios,Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicios,String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicios,String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicios,String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicios,String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicios,Integer>("codigoEmpresas"));
    desactivarControles();
    }
    
    public void seleccionarElemento(){
        txtCodigoServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicios()));
        fecha.selectedDateProperty().set(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtTipoServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));
        txtHoraServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
        txtLugarServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
        txtTelefonoContacto.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresas()));
    
    }
    
    public ObservableList<Servicios>getServicios(){
        ArrayList<Servicios> lista = new ArrayList <Servicios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Servicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios (resultado.getInt("codigoServicios"),
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
    
     public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Empresas}");   
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresas"),
                                      resultado.getString("nombreEmpresa"),
                                      resultado.getString("direccion"),
                                      resultado.getString("telefono")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
     
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Buscar_Empresas(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet regitro = procedimiento.executeQuery();
            while (regitro.next()){
                resultado = new Empresa(regitro.getInt("codigoEmpresas"),
                                      regitro.getString("nombreEmpresa"),
                                      regitro.getString("direccion"),
                                      regitro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
    
    public void MainApp(){
        escenarioPrincipal.MainApp();
    }
}

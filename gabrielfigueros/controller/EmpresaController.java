package org.gabrielfigueros.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gabrielfigueros.bd.Conexion;
import org.gabrielfigueros.bean.Empresa;
import org.gabrielfigueros.report.GenerarReporte;
import org.gabrielfigueros.system.MainApp;

public class EmpresaController implements Initializable{
    
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private MainApp escenarioPrincipal;
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList <Empresa> listaEmpresa;
    
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private ImageView imgReturn;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnReturn;
    @FXML private Button btnServicios;
    @FXML private Label lblCodigoEmpresa;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer> ("codigoEmpresas"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String> ("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String> ("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String> ("telefono"));
        desactivarControles();
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
    
    public void seleccionarElementos(){
        txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresas()));   
        txtNombreEmpresa.setText((((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa()));
        txtDireccionEmpresa.setText((((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTelefonoEmpresa.setText((((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono()));
    }
public void guardar(){
    Empresa empresaNueva = new Empresa();
    empresaNueva.setNombreEmpresa(txtNombreEmpresa.getText());
    empresaNueva.setDireccion(txtDireccionEmpresa.getText());
    empresaNueva.setTelefono(txtTelefonoEmpresa.getText());
        try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Agregar_Empresas(?,?,?)}");
            sp.setString(1, empresaNueva.getNombreEmpresa());
            sp.setString(2, empresaNueva.getDireccion());
            sp.setString(3, empresaNueva.getTelefono());
            sp.execute();
            //listaEmpresa.add(empresaNueva);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void nuevo(){
        switch (tipoOperacion){
            case NINGUNO:
                activarControles();
                desactivarBotones();
                limpiarControles();
                btnNuevo.setVisible(true);
                btnNuevo.setText("Guardar");
                btnEliminar.setVisible(true);
                btnEliminar.setText("Cancelar");
                desactivarID();
                tipoOperacion = operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(true);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                activarID();
                tipoOperacion = operaciones.NINGUNO;
                activarBotones();
                cargarDatos();
                break;
        }
    }
    
    
    public void Eliminar(){
            switch (tipoOperacion){
                case GUARDAR:
                activarBotones();
                activarID();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
                default:
                    if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro que quiere eliminar el elemento? Se eliminara de otras entidades","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION){
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Empresas(?)}");
                                procedimiento.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresas());
                                procedimiento.execute();
                                listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                    
               }
        }
        
        public void Editar(){
            switch(tipoOperacion){
                case NINGUNO:
                    if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null,"Selecione la empresa a actualizar");
                    }
                break;
                case ACTUALIZAR:
                    Actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                break;
            }
        }
        
        public void Actualizar(){
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Empresas(?,?,?,?)}");
                Empresa registro = ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem());
                registro.setNombreEmpresa(txtNombreEmpresa.getText());
                registro.setDireccion(txtDireccionEmpresa.getText());
                registro.setTelefono(txtTelefonoEmpresa.getText());
                sp.setInt(1, registro.getCodigoEmpresas());
                sp.setString(2, registro.getNombreEmpresa());
                sp.setString(3, registro.getDireccion());
                sp.setString(4, registro.getTelefono());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                cargarDatos();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        
        public void GenerarReporte(){
            switch(tipoOperacion){
                case ACTUALIZAR:
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                break;
                 case NINGUNO:
                       imprimirReporte();
                break;    
                    
            }
        }
    
        public void imprimirReporte(){
            Map parametros = new HashMap();
            parametros.put("codigoEmpresas",null);
            GenerarReporte.mostrarReporte("ReporteEmpresasGF.jasper", "Reporte de Empresas", parametros);
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

    public void activarID(){
        txtCodigoEmpresa.setVisible(true);
        lblCodigoEmpresa.setVisible(true);
    }

    public void desactivarID(){
        txtCodigoEmpresa.setVisible(false);
        lblCodigoEmpresa.setVisible(false);
    }
    
    public void desactivarControles(){
    txtCodigoEmpresa.setEditable(false);
    txtNombreEmpresa.setEditable(false);
    txtDireccionEmpresa.setEditable(false);
    txtTelefonoEmpresa.setEditable(false);
    }
     public void activarControles(){
    //txtCodigoEmpresa.setEditable(true);
    txtNombreEmpresa.setEditable(true);
    txtDireccionEmpresa.setEditable(true);
    txtTelefonoEmpresa.setEditable(true);
    }
     public void limpiarControles(){
    txtCodigoEmpresa.setText("");
    txtNombreEmpresa.setText("");
    txtDireccionEmpresa.setText("");
    txtTelefonoEmpresa.setText("");
    }
    
     public void Return(){
        escenarioPrincipal.Modulo();
    }
     
    public void Servicios(){
         escenarioPrincipal.Servicios();
     }
     
     public void Presupuesto(){
         escenarioPrincipal.Presupuesto();
     }
      
     
    public MainApp getEscenarioMainApp() {
        return escenarioPrincipal;
    }
    
    
    public void setEscenarioMainApp(MainApp escenarioMainApp) {
        this.escenarioPrincipal = escenarioMainApp;
    }
    
    
    
}

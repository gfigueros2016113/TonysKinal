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
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gabrielfigueros.bd.Conexion;
import org.gabrielfigueros.bean.Plato;
import org.gabrielfigueros.bean.TipoPlato;
import org.gabrielfigueros.system.MainApp;


public class PlatoController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private MainApp escenarioPrincipal;
    private ObservableList <TipoPlato> listaTipoPlato;
    private ObservableList <Plato> listaPlatos;
    @FXML private Label lblCodigo;
    @FXML private ComboBox cmbTplato;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colPrecio;
    @FXML private ImageView imgReturn;
    @FXML private TableColumn colTipo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    public void desactivarControles(){
        lblCodigo.setDisable(false);
        txtCodigo.setEditable(false);
        txtNombre.setEditable(false);
        txtCantidad.setEditable(false);
        txtDescripcion.setEditable(false);
        txtPrecio.setEditable(false);
        cmbTplato.setDisable(true);
    }
     
    public void activarControles(){
        lblCodigo.setDisable(true);
        txtCodigo.setEditable(false);
        txtNombre.setEditable(true);
        txtCantidad.setEditable(true);
        txtDescripcion.setEditable(true);
        txtPrecio.setEditable(true);
        cmbTplato.setDisable(false);
    }
        
    public void limpiarControles(){
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCantidad.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        cmbTplato.getSelectionModel().clearSelection();
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
        
        public void Nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                desactivarBotones();
                limpiarControles();
                btnNuevo.setVisible(true);
                btnNuevo.setText("Guardar");
                btnEliminar.setVisible(true);
                btnEliminar.setText("Cancelar");
                tipoDeOperacion = operaciones.GUARDAR;
                break;

            case GUARDAR:
                Guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(true);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                activarBotones();
                cargarDatos();
                break;
        }
    }
        

        public void Guardar(){
           Plato registro = new Plato();
           registro.setNombrePlato(txtNombre.getText());
           registro.setCantidad(Integer.valueOf(txtCantidad.getText()));
           registro.setDescripcionPlato(txtDescripcion.getText());
           registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
           registro.setCodigoTipoplato(((TipoPlato)cmbTplato.getSelectionModel().getSelectedItem()).getCodigoTipoplato());
           try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Agregar_Platos(?,?,?,?,?)}");
                sp.setInt(1,registro.getCantidad());
                sp.setString(2, registro.getNombrePlato());
                sp.setString(3, registro.getDescripcionPlato());
                sp.setDouble(4, registro.getPrecioPlato());
                sp.setInt(5, registro. getCodigoTipoplato());
                
                sp.execute();
                listaPlatos.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
       
        public void Eliminar(){
            switch (tipoDeOperacion){
                case GUARDAR:
                activarBotones();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                default:
                    if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el elemento? Se eliminara de otras entidades", "Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if(respuesta == JOptionPane.YES_OPTION){
                               try{
                                   PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Platos(?)}");
                                   sp.setInt(1, ((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlatos());
                                   sp.execute();
                                   listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                                   limpiarControles();
                                   JOptionPane.showMessageDialog(null, "Plato eliminado exitosamente");
                               }catch(Exception e){
                                   e.printStackTrace();
                               }
                            }
                }else{
                        JOptionPane.showMessageDialog(null, "Seleccione el producto a eliminar");

                    }

               }
        }
        
        public void Editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null,"Selecione el plato a actualizar");
                    }
                break;
                case ACTUALIZAR:
                    Actualizar();
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
                case ACTUALIZAR:
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = PlatoController.operaciones.NINGUNO;
                    cargarDatos();
                break;
                    
                    
            }
        }
        
        public void Actualizar(){
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Platos(?,?,?,?,?,?)}");
                Plato registro = ((Plato)tblPlatos.getSelectionModel().getSelectedItem());
                registro.setNombrePlato(txtNombre.getText());
                registro.setCantidad(Integer.valueOf(txtCantidad.getText()));
                registro.setDescripcionPlato(txtDescripcion.getText());
                registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText()));
                registro.setCodigoTipoplato(((TipoPlato)cmbTplato.getSelectionModel().getSelectedItem()).getCodigoTipoplato());
                sp.setInt(1,registro.getCodigoPlatos());
                sp.setInt(2,registro.getCantidad());
                sp.setString(3, registro.getNombrePlato());
                sp.setString(4, registro.getDescripcionPlato());
                sp.setDouble(5, registro.getPrecioPlato());
                sp.setInt(6, registro. getCodigoTipoplato());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                cargarDatos();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        public ObservableList<TipoPlato> getTipoPlato(){
            ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Tipo_plato()}");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoPlato(resultado.getInt("codigoTipoplato"),
                                                resultado.getString("descripcion")));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaTipoPlato = FXCollections.observableArrayList(lista);
        }
        
        public TipoPlato buscarTplato (int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Buscar_Tipo_plato(?)}");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
            resultado = new TipoPlato(registro.getInt("codigoTipoplato"),
                                    registro.getString("descripcion"));
        }
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultado;
    }
        
    public void seleccionarElementos(){
        txtCodigo.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoplato()));
        txtNombre.setText((((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
        txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        txtDescripcion.setText((((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato()));
        txtPrecio.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cmbTplato.getSelectionModel().select(buscarTplato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoplato()));
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbTplato.setItems(getTipoPlato());
    }
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlatos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Plato, Integer> ("codigoPlatos"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer> ("cantidad"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Plato, String> ("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato, String> ("descripcionPlato"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Plato, Double> ("precioPlato"));
        colTipo.setCellValueFactory(new PropertyValueFactory<Plato, Integer> ("codigoTipoplato"));
        desactivarControles();
    }
    
    
     public ObservableList<Plato> getPlatos(){
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
                                      resultado.getInt("codigoTipoplato")));
                                                            
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPlatos = FXCollections.observableArrayList(lista);
    }
    
    public void Return(){
        escenarioPrincipal.Tipo_plato();
    }
     
    public MainApp getEscenarioMainApp() {
        return escenarioPrincipal;
    }
    
    
    public void setEscenarioMainApp(MainApp escenarioMainApp) {
        this.escenarioPrincipal = escenarioMainApp;
    }  
    
}

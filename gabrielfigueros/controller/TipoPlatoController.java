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
import org.gabrielfigueros.bd.Conexion;
import org.gabrielfigueros.bean.TipoPlato;
import org.gabrielfigueros.system.MainApp;


public class TipoPlatoController implements Initializable{
    
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private MainApp escenarioPrincipal;
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList <TipoPlato> listaTipoPlato;
    
    
    @FXML private Button btnReturn;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Label lblCodigoTipoplato;
    @FXML private TableColumn colCodigoTipoplato;
    @FXML private TableColumn colDescripcion;
    @FXML private TextField txtCodigoTipoplato;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoPlato;

   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoplato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer> ("CodigoTipoplato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String> ("descripcion"));
        desactivarControles();
    }
    
     public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call Sp_Listar_Tipo_plato}");   
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoplato"),
                                      resultado.getString("descripcion")));                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        txtCodigoTipoplato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoplato()));   
        txtDescripcion.setText((((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcion()));
    }
    
  public void Nuevo(){
        switch (tipoOperacion){
            case NINGUNO:
                activarControles();
                desactivarBotones();
                limpiarControles();
                btnNuevo.setVisible(true);
                btnNuevo.setText("Guardar");
                btnEliminar.setVisible(true);
                btnEliminar.setText("Cancelar");
                tipoOperacion = TipoPlatoController.operaciones.GUARDAR;
                break;

            case GUARDAR:
                Guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                tipoOperacion = TipoPlatoController.operaciones.NINGUNO;
                activarBotones();
                cargarDatos();
                break;
        }
    }
    
    public void Guardar(){
           TipoPlato registro = new TipoPlato();
           registro.setDescripcion(txtDescripcion.getText());
           try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Agregar_Tipo_plato(?)}");
                sp.setString(1,registro.getDescripcion());
                sp.execute();
                listaTipoPlato.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    
    
    public void Eliminar(){
        switch (tipoOperacion){
            case GUARDAR:
                activarBotones();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                tipoOperacion = TipoPlatoController.operaciones.NINGUNO;
                break;
                default:
                    if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro que quiere eliminar el elemento? Se eliminara de otras entidades", "Eliminar Tipo De Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if(respuesta == JOptionPane.YES_OPTION){
                               try{
                                   PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Eliminar_Tipo_plato(?)}");
                                   sp.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoplato());
                                   sp.execute();
                                   listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
                                   limpiarControles();
                                   JOptionPane.showMessageDialog(null, "Tipo de plato eliminado exitosamente");
                               }catch(Exception e){
                                   e.printStackTrace();
                               }
                            }
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione el dato a eliminar");
                }
        }
    }
    
public void Editar(){
    switch(tipoOperacion){
        case NINGUNO:
            if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                activarControles();
                tipoOperacion = TipoPlatoController.operaciones.ACTUALIZAR;
            }else{
                JOptionPane.showMessageDialog(null,"Selecione el tipo de plato a actualizar");
            }
            break;
        case ACTUALIZAR:
            Actualizar();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setDisable(false);
            btnEliminar.setDisable(false);
            tipoOperacion = TipoPlatoController.operaciones.NINGUNO;
            cargarDatos();
            break;
            }
}
        
        public void Actualizar(){
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call Sp_Actualizar_Tipo_plato(?,?)}");
                TipoPlato registro = ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem());
                registro.setDescripcion(txtDescripcion.getText());
                sp.setInt(1, registro.getCodigoTipoplato());
                sp.setString(2, registro.getDescripcion());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                cargarDatos();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
         public void Reporte(){
            switch(tipoOperacion){
                case ACTUALIZAR:
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = TipoPlatoController.operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                break;
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

    public void activarID(){
        txtCodigoTipoplato.setVisible(true);
        lblCodigoTipoplato.setVisible(true);
    }

    public void desactivarID(){
        txtCodigoTipoplato.setVisible(false);
        lblCodigoTipoplato.setVisible(false);
    }
    
    public void desactivarControles(){
   txtCodigoTipoplato.setEditable(false);
    txtDescripcion.setEditable(false);
   
    }
     public void activarControles(){
    //txtCodigoTipoplato.setEditable(true);
   txtDescripcion.setEditable(true);
    
    }
     public void limpiarControles(){
    txtCodigoTipoplato.setText("");
    txtDescripcion.setText("");
    
    }
    
     public void Return(){
        escenarioPrincipal.Modulo();
    }
     
     public void Plato(){
        escenarioPrincipal.Plato();
    }
     
    public MainApp getEscenarioMainApp() {
        return escenarioPrincipal;
    }
    
    
    public void setEscenarioMainApp(MainApp escenarioMainApp) {
        this.escenarioPrincipal = escenarioMainApp;
    }
}

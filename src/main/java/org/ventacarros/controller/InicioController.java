package org.ventacarros.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.ventacarros.database.Conexion;
import org.ventacarros.model.Cliente;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class InicioController implements Initializable {
    
    private Main principal;
    private ObservableList<Cliente> listaClientes;
    @FXML
    private Button btnIngresar, btnRegistrarse;
    @FXML
    private TextField txtCorreo, txtContraseña;
    @FXML
    private Label lblFelicidades;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    @FXML
    private void Ingresar(ActionEvent evento){
        if (evento.getSource() == btnRegistrarse){
            principal.Registrarse();
        } 
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public ArrayList<Cliente> listarClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        try {
            String sql = "call sp_listarClientes();";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(sql);
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                //verdadero
                clientes.add(new Cliente(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }
            return clientes;
        } catch (SQLException ex) {
            System.out.println("Error al listar los clientes" + ex.getSQLState());
            ex.printStackTrace();
        }
        return clientes;
    }
    
    @FXML
    private void Login(){
        listaClientes = FXCollections.observableArrayList(listarClientes());
        boolean encontrado = false;
        String correo = txtCorreo.getText();
        String contraseña = txtContraseña.getText();
        for (Cliente c : listaClientes){
            if (c.getCorreo().equals(correo) &&
                    c.getContraseña().equals(contraseña)){
                encontrado = true;
                break;
            }
        }
        
        if (encontrado){
            principal.MenuPrincipal();
        } else {
            lblFelicidades.setVisible(true);
            txtCorreo.clear();
            txtContraseña.clear();
        }
    }
}

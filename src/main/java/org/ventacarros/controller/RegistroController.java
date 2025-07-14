/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ventacarros.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.ventacarros.database.Conexion;
import org.ventacarros.model.Cliente;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author Zacarias
 */
public class RegistroController implements Initializable {

    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    private Cliente obtenerModelo;

    @FXML
    private TextField txtNombre, txtApellido, txtTelefono, txtCorreo, txtNit, txtContraseña;
    @FXML
    private Label lblFelicidades;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void regresar(){
        principal.Inicio();
    }

    private Cliente obtenerModeloClientes() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String nit = txtNit.getText();
        String cont = txtContraseña.getText();
        Cliente cliente = new Cliente(nombre, apellido, telefono, correo, nit, cont);
        return cliente;
    }

    private void agregarClientes() {
        obtenerModelo = obtenerModeloClientes();
        try {
            String SQL = "call sp_agregarClientes(?,?,?,?,?,?)";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(SQL);
            enunciado.setString(1, obtenerModelo.getNombre());
            enunciado.setString(2, obtenerModelo.getApellido());
            enunciado.setString(3, obtenerModelo.getTelefono());
            enunciado.setString(4, obtenerModelo.getCorreo());
            enunciado.setString(5, obtenerModelo.getNit());
            enunciado.setString(6, obtenerModelo.getContraseña());
            enunciado.execute();
        } catch (SQLException e) {
            System.out.println("Error al agregar");
            e.printStackTrace();
        }
    }

    private void limpiarTf() {
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtNit.clear();
        txtContraseña.clear();
    }

    private boolean camposVacios() {
        return  txtNombre.getText().trim().isEmpty() ||
                txtApellido.getText().trim().isEmpty() ||
                txtTelefono.getText().trim().isEmpty() ||
                txtCorreo.getText().trim().isEmpty() ||
                txtNit.getText().trim().isEmpty() ||
                txtContraseña.getText().trim().isEmpty();
    }

    @FXML
    private void Registrarse() {
        if (camposVacios()) {
            lblFelicidades.setText("Llena todos los campos");
            lblFelicidades.setTextFill(Color.RED);
            lblFelicidades.setVisible(true);
        } else {
            agregarClientes();
            lblFelicidades.setText("¡Agregado con exito!");
            lblFelicidades.setVisible(true);
        }
    }
}

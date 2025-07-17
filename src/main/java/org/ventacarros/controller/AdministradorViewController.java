/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ventacarros.database.Conexion;
import org.ventacarros.model.Administrador;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author MIGUEL
 */
public class AdministradorViewController implements Initializable {

    @FXML
    private TableView<Administrador> tablaAdmins;
    @FXML
    private TableColumn colId, colNombre, colApellido,
            colTelefono, colCorreo, colNit, colEstado, colContraseña;
    @FXML
    private TextField txtId, txtNombre, txtApellido, txtTelefono, txtCorreo, txtNit, txtContraseña, txtBuscar;
    @FXML
    private Button btnNuevo, btnEditar, btnEliminar, btnCancelar, btnGuardar, btnBuscar, btnRegresar;
    private Main principal;
    private ObservableList<Administrador> listaAdmins;
    private Administrador obtenerModelo;

    private enum acciones {
        NUEVO, EDITAR, ELIMINAR, NINGUNO
    }
    private acciones tipoAccion = acciones.NINGUNO;

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @FXML
    private void clickHandlerAction(ActionEvent evento) {
        if (evento.getSource() == btnRegresar) {
            principal.MenuPrincipalAdmins();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTabla();
        tablaAdmins.setOnMouseClicked(eventHandler -> cargarTablaEnTF());
    }

    public void configurarColumnas() {
        //Formato de columnas
        colId.setCellValueFactory(new PropertyValueFactory<Administrador, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Administrador, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Administrador, String>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administrador, String>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Administrador, String>("correo"));
        colNit.setCellValueFactory(new PropertyValueFactory<Administrador, String>("nit"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Administrador, String>("estado"));
        colContraseña.setCellValueFactory(new PropertyValueFactory<Administrador, String>("contraseña"));
    }

    public void cargarTabla() {
        listaAdmins = FXCollections.observableArrayList(listarAdmins());
        tablaAdmins.setItems(listaAdmins);
        tablaAdmins.getSelectionModel().selectFirst();
    }

    public void cargarTablaEnTF() {
        Administrador adminSeleccionado = tablaAdmins.getSelectionModel().getSelectedItem();
        if (adminSeleccionado != null) {
            txtId.setText(String.valueOf(adminSeleccionado.getId()));
            txtNombre.setText(adminSeleccionado.getNombre());
            txtApellido.setText(adminSeleccionado.getApellido());
            txtTelefono.setText(adminSeleccionado.getTelefono());
            txtCorreo.setText(adminSeleccionado.getCorreo());
            txtNit.setText(adminSeleccionado.getNit());
            txtContraseña.setText(adminSeleccionado.getContraseña());
        }
    }

    public ArrayList<Administrador> listarAdmins() {
        ArrayList<Administrador> administrador = new ArrayList<>();
        try {
            String sql = "call sp_listarAdministradores();";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(sql);
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                //verdadero
                administrador.add(new Administrador(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }
            return administrador;
        } catch (SQLException ex) {
            System.out.println("Error al listar los clientes" + ex.getSQLState());
            ex.printStackTrace();
        }
        return administrador;
    }

    private Administrador obtenerModeloAdmins() {
        int id;
        if (txtId.getText().isEmpty()) {
            id = 1;
        } else {
            id = Integer.parseInt(txtId.getText());
        }
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String nit = txtNit.getText();
        String cont = txtContraseña.getText();
        Administrador administrador = new Administrador(id, nombre, apellido, telefono, correo, nit, cont);
        return administrador;
    }

    private void agregarAdmins() {
        obtenerModelo = obtenerModeloAdmins();
        try {
            String SQL = "call sp_agregarAdministradores(?,?,?,?,?,?)";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(SQL);
            enunciado.setString(1, obtenerModelo.getNombre());
            enunciado.setString(2, obtenerModelo.getApellido());
            enunciado.setString(3, obtenerModelo.getTelefono());
            enunciado.setString(4, obtenerModelo.getCorreo());
            enunciado.setString(5, obtenerModelo.getNit());
            enunciado.setString(6, obtenerModelo.getContraseña());
            enunciado.execute();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar");
            e.printStackTrace();
        }
    }

    private void actualizarAdmins() {
        obtenerModelo = obtenerModeloAdmins();
        try {
            String SQL = "call sp_actualizarAdministradores(?,?,?,?,?,?,?)";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(SQL);
            enunciado.setInt(1, obtenerModelo.getId());
            enunciado.setString(2, obtenerModelo.getNombre());
            enunciado.setString(3, obtenerModelo.getApellido());
            enunciado.setString(4, obtenerModelo.getTelefono());
            enunciado.setString(5, obtenerModelo.getCorreo());
            enunciado.setString(6, obtenerModelo.getNit());
            enunciado.setString(7, obtenerModelo.getContraseña());
            enunciado.execute();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al actualizar");
            e.printStackTrace();
        }
    }

    private void eliminarAdmins() {
        obtenerModelo = obtenerModeloAdmins();
        try {
            String SQL = "call sp_eliminarAdministradores(?)";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(SQL);
            enunciado.setInt(1, obtenerModelo.getId());
            enunciado.execute();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al eliminar");
            e.printStackTrace();
        }
    }

    private void limpiarTF() {
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtNit.clear();
        txtContraseña.clear();
    }

    private void cambiarEstado(boolean estado) {
        txtNombre.setDisable(estado);
        txtApellido.setDisable(estado);
        txtTelefono.setDisable(estado);
        txtCorreo.setDisable(estado);
        txtNit.setDisable(estado);
        txtContraseña.setDisable(estado);
    }

    private void deshabilitarNodos() {
        boolean desactivado = txtNombre.isDisable();
        cambiarEstado(!desactivado);
        btnNuevo.setDisable(desactivado);
        btnEditar.setDisable(desactivado);
        btnEliminar.setDisable(desactivado);
        btnGuardar.setDisable(!desactivado);
        btnCancelar.setDisable(!desactivado);
    }

    @FXML
    private void btnNuevo() {
        limpiarTF();
        deshabilitarNodos();
        txtNombre.requestFocus();
        tipoAccion = acciones.NUEVO;
    }

    @FXML
    private void btnEditar() {
        deshabilitarNodos();
        txtNombre.requestFocus();
        tipoAccion = acciones.EDITAR;
    }

    @FXML
    private void btnEliminar() {
        deshabilitarNodos();
        txtNombre.requestFocus();
        tipoAccion = acciones.ELIMINAR;
    }

    @FXML
    private void btnCancelar() {
        cargarTablaEnTF();
        deshabilitarNodos();
    }

    @FXML
    private void guardar() {
        deshabilitarNodos();
        if (tipoAccion == acciones.NUEVO) {
            agregarAdmins();
            tipoAccion = acciones.NINGUNO;
        } else if (tipoAccion == acciones.EDITAR) {
            actualizarAdmins();
            tipoAccion = acciones.EDITAR;
        } else if (tipoAccion == acciones.ELIMINAR) {
            eliminarAdmins();
            tipoAccion = acciones.ELIMINAR;
        }
    }

    @FXML
    private void buscar() {
        ArrayList<Administrador> resultadoBusqueda = new ArrayList<>();
        String nombreCompleto = txtBuscar.getText();
        for (Administrador a : listaAdmins) {
            if (a.getNombre().toLowerCase().contains(nombreCompleto.toLowerCase())
                    || a.getApellido().toLowerCase().contains(nombreCompleto)) {
                resultadoBusqueda.add(a);
                tablaAdmins.setItems(FXCollections.observableArrayList(resultadoBusqueda));
                if (resultadoBusqueda.isEmpty()) {
                    tablaAdmins.getSelectionModel().selectFirst();
                }
            }
        }
    }
}

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
import org.ventacarros.model.Productos;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class ProductosController implements Initializable {

    @FXML
    private TableView<Productos> tablaProductos;
    @FXML
    private TableColumn colIdProducto, colNombre, colPrecio,
            colDescripcion, colStock, colEstado;
    @FXML
    private TextField txtId, txtNombre, txtPrecio, txtDescripcion, txtStock ,txtBuscar;
    @FXML
    private Button btnNuevo, btnEditar, btnEliminar, btnCancelar, btnGuardar, btnBuscar, btnRegresar;
    private Main principal;
    private ObservableList<Productos> listaProductos;
    private Productos obtenerModelo;

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
            principal.MenuPrincipal();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTablaProductos();
        tablaProductos.setOnMouseClicked(eventHandler -> cargarTablaEnTF());
    }

    public void configurarColumnas() {
        //Formato de columnas
        colIdProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Productos, String>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precio"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcion"));
        colStock.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("stock"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Productos, String>("estado"));
    }

    public void cargarTablaProductos() {
        listaProductos = FXCollections.observableArrayList(listarProductos());
        tablaProductos.setItems(listaProductos);
        tablaProductos.getSelectionModel().selectFirst();
    }

    public void cargarTablaEnTF() {
        Productos productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            txtId.setText(String.valueOf(productoSeleccionado.getId()));
            txtNombre.setText(productoSeleccionado.getNombre());
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
            txtDescripcion.setText(productoSeleccionado.getDescripcion());
            txtStock.setText(String.valueOf(productoSeleccionado.getStock()));
        }
    }

    public ArrayList<Productos> listarProductos() {
        ArrayList<Productos> productos = new ArrayList<>();
        try {
            String sql = "call sp_listarProductos();";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(sql);
            ResultSet rs = enunciado.executeQuery();
            while (rs.next()) {
                //verdadero
                productos.add(new Productos(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getString(6)));
            }
            return productos;
        } catch (SQLException ex) {
            System.out.println("Error al listar" + ex.getSQLState());
            ex.printStackTrace();
        }
        return productos;
    }

    private Productos obtenerModelo() {
        int id;
        if (txtId.getText().isEmpty()) {
            id = 1;
        } else {
            id = Integer.parseInt(txtId.getText());
        }
        String nombre = txtNombre.getText();
        Double precio = Double.parseDouble(txtPrecio.getText());
        String descripcion = txtDescripcion.getText();
        int stock = Integer.parseInt(txtStock.getText());
        Productos productos = new Productos(id, nombre, precio, descripcion, stock);
        return productos;
    }

    private void agregarProductos() {
        obtenerModelo = obtenerModelo();
        try {
            String SQL = "call sp_agregarProductos(?,?,?,?)";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(SQL);
            enunciado.setString(1, obtenerModelo.getNombre());
            enunciado.setDouble(2, obtenerModelo.getPrecio());
            enunciado.setString(3, obtenerModelo.getDescripcion());
            enunciado.setInt(4, obtenerModelo.getStock());
            enunciado.execute();
            cargarTablaProductos();
        } catch (SQLException e) {
            System.out.println("Error al agregar");
            e.printStackTrace();
        }
    }

    private void actualizarProductos() {
        obtenerModelo = obtenerModelo();
        try {
            String SQL = "call sp_actualizarProductos(?,?,?,?,?)";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(SQL);
            enunciado.setInt(1, obtenerModelo.getId());
            enunciado.setString(2, obtenerModelo.getNombre());
            enunciado.setDouble(3, obtenerModelo.getPrecio());
            enunciado.setString(4, obtenerModelo.getDescripcion());
            enunciado.setInt(5, obtenerModelo.getStock());
            enunciado.execute();
            cargarTablaProductos();
        } catch (SQLException e) {
            System.out.println("Error al actualizar");
            e.printStackTrace();
        }
    }

    private void eliminarProductos() {
        obtenerModelo = obtenerModelo();
        try {
            String SQL = "call sp_eliminarProductos(?)";
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall(SQL);
            enunciado.setInt(1, obtenerModelo.getId());
            enunciado.execute();
            cargarTablaProductos();
        } catch (SQLException e) {
            System.out.println("Error al eliminar");
            e.printStackTrace();
        }
    }

    private void limpiarTF() {
        txtNombre.clear();
        txtPrecio.clear();
        txtDescripcion.clear();
        txtStock.clear();
    }

    private void cambiarEstado(boolean estado) {
        txtNombre.setDisable(estado);
        txtPrecio.setDisable(estado);
        txtDescripcion.setDisable(estado);
        txtStock.setDisable(estado);
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
            agregarProductos();
            tipoAccion = acciones.NINGUNO;
        } else if (tipoAccion == acciones.EDITAR) {
            actualizarProductos();
            tipoAccion = acciones.EDITAR;
        } else if (tipoAccion == acciones.ELIMINAR) {
            eliminarProductos();
            tipoAccion = acciones.ELIMINAR;
        }
    }

    @FXML
    private void buscar() {
        ArrayList<Productos> resultadoBusqueda = new ArrayList<>();
        String nombreCompleto = txtBuscar.getText();
        for (Productos p : listaProductos) {
            if (p.getNombre().toLowerCase().contains(nombreCompleto.toLowerCase())) {
                resultadoBusqueda.add(p);
                tablaProductos.setItems(FXCollections.observableArrayList(resultadoBusqueda));
                if (resultadoBusqueda.isEmpty()) {
                    tablaProductos.getSelectionModel().selectFirst();
                }
            }
        }
    }
}

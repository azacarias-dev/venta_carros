/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ventacarros.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ventacarros.database.Conexion;
import org.ventacarros.model.Compras;
import org.ventacarros.model.Productos;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author jgome
 */
public class ComprasController implements Initializable {

    private ObservableList<Compras> listaCompras;
    private ObservableList<Productos> listaProductos;
    private Compras modelo;
    private Main principal;
    private int idCliente;

    private enum acciones {
        AGREGAR, EDITAR, ELIMINAR, NINGUNA;
    }
    private acciones tipoAccion = acciones.NINGUNA;
    @FXML
    private TableView<Compras> tablaCompras;
    @FXML
    private Button btnRegresar, btnNuevo, btnEditar, btnGuardar, btnCancelar, btnBuscar;
    @FXML
    private TextField txtId, txtBuscar, txtSubtotal;
    @FXML
    private DatePicker dpFechaCompra;
    @FXML
    private TableColumn colId, colFecha, colIdProducto, colSubtotal;
    @FXML
    private ComboBox<Productos> cbxProductos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarCasillas();
        cargarProductosAComboBox();
        tablaCompras.setOnMouseClicked(eventHandler -> cargarEnTextField());
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
        this.idCliente = principal.getClienteActivo().getId();
        cargarTabla();
    }

    @FXML
    private void clickActionHandler(ActionEvent evento) {
        if (evento.getSource() == btnRegresar) {
            System.out.println("Regresando...");
            principal.MenuPrincipal();
        }
    }

    private ArrayList<Compras> listarCompras() {
        ArrayList<Compras> compras = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_listarComprasPorCliente(?);");
            enunciado.setInt(1, idCliente);
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                compras.add(new Compras(
                        resultado.getInt(1),
                        resultado.getInt(2),
                        resultado.getDate(3).toLocalDate(),
                        resultado.getInt(4),
                        resultado.getDouble(5)));
            }
            return compras;
        } catch (SQLException e) {
            System.out.println("Error al listar las compras");
            e.printStackTrace();
        }
        return compras;
    }

    private ArrayList<Productos> obtenerModeloProductos() {
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

    private Compras obtenerModelo() {
        int id = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        Productos productoSeleccionado = cbxProductos.getSelectionModel().getSelectedItem();
        int idProducto = productoSeleccionado != null ? productoSeleccionado.getId() : 0;
        return new Compras(id, idCliente, dpFechaCompra.getValue(), idProducto, Double.parseDouble(txtSubtotal.getText()));
    }

    private void cargarProductosAComboBox() {
        listaProductos = FXCollections.observableArrayList(obtenerModeloProductos());
        cbxProductos.setItems(listaProductos);
    }

    private void configurarCasillas() {
        colId.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("id"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Compras, LocalDate>("fechaCompra"));
        colIdProducto.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("idProducto"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<Compras, Double>("subtotal"));
    }

    private void cargarTabla() {
        listaCompras = FXCollections.observableArrayList(listarCompras());
        tablaCompras.setItems(listaCompras);
        tablaCompras.getSelectionModel().selectFirst();
        cargarEnTextField();
    }

    private void cargarEnTextField() {
        Compras compraSeleecionada = tablaCompras.getSelectionModel().getSelectedItem();
        if (compraSeleecionada != null) {
            txtId.setText(String.valueOf(compraSeleecionada.getId()));
            txtSubtotal.setText((String.valueOf(compraSeleecionada.getSubtotal())));
            dpFechaCompra.setValue(compraSeleecionada.getFechaCompra());
            for (Productos p : cbxProductos.getItems()) {
                if (p.getId() == compraSeleecionada.getIdProducto()) {
                    cbxProductos.setValue(p);
                    break;
                }
            }
        }
    }

    private void agregarCompras() {
        modelo = obtenerModelo();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_agregarCompras(?,?,?,?);");
            enunciado.setDate(1, Date.valueOf(modelo.getFechaCompra()));
            enunciado.setInt(2, idCliente);
            enunciado.setInt(3, modelo.getIdProducto());
            enunciado.setDouble(4, modelo.getSubtotal());
            enunciado.executeUpdate();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar la compra");
            e.printStackTrace();
        }
    }

    private void editarCompra() {
        modelo = obtenerModelo();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().prepareCall("call sp_actualizarCompras(?,?,?,?,?);");
            enunciado.setInt(1, modelo.getId());
            enunciado.setDate(2, Date.valueOf(modelo.getFechaCompra()));
            enunciado.setInt(3, idCliente);
            enunciado.setInt(4, modelo.getIdProducto());
            enunciado.setDouble(5, modelo.getSubtotal());
            enunciado.execute();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al actualizar la compra");
            e.printStackTrace();
        }
    }

    @FXML
    private void buscar() {
        ArrayList<Compras> resultadoBusqueda = new ArrayList<>();
        int id = Integer.parseInt(txtBuscar.getText());
        for (Compras c : listaCompras) {
            if (c.getId() == id) {
                resultadoBusqueda.add(c);
                tablaCompras.setItems(FXCollections.observableArrayList(resultadoBusqueda));
            } else if (resultadoBusqueda.isEmpty()) {
                tablaCompras.getSelectionModel().selectFirst();
            }
        }
    }

    private void limpiarTextField() {
        cbxProductos.setValue(null);
        dpFechaCompra.setValue(null);
        txtSubtotal.clear();
    }

    private void cambiarEstado(boolean estado) {
        cbxProductos.setDisable(estado);
        dpFechaCompra.setDisable(estado);
        txtSubtotal.setDisable(estado);
    }

    private void deshabilitar() {
        boolean descativado = cbxProductos.isDisable();
        cambiarEstado(!descativado);
        btnNuevo.setDisable(descativado);
        btnEditar.setDisable(descativado);
        btnCancelar.setDisable(!descativado);
        btnGuardar.setDisable(!descativado);
    }

    @FXML
    private void btnNuevo() {
        limpiarTextField();
        deshabilitar();
        tipoAccion = acciones.AGREGAR;
    }

    @FXML
    private void btnEditar() {
        deshabilitar();
        tipoAccion = acciones.EDITAR;
    }

    @FXML
    private void btnGuardar() {
        if (tipoAccion == acciones.AGREGAR) {
            agregarCompras();
            tipoAccion = acciones.NINGUNA;
        } else if (tipoAccion == acciones.EDITAR) {
            editarCompra();
            tipoAccion = acciones.NINGUNA;
        }
    }

    @FXML
    private void btnCancelar() {
        cargarEnTextField();
        deshabilitar();
    }
}

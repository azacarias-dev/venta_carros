package org.ventacarros.system;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ventacarros.controller.AdministradorViewController;
import org.ventacarros.controller.ClientesVController;
import org.ventacarros.controller.ComprasController;
import org.ventacarros.controller.CrearCuentaAdminController;
import org.ventacarros.controller.InicioController;
import org.ventacarros.controller.MenuAdministradorController;
import org.ventacarros.controller.MenuPrincipalController;
import org.ventacarros.controller.ProductosController;
import org.ventacarros.controller.RegistroController;
import org.ventacarros.database.Conexion;
import org.ventacarros.model.Administrador;
import org.ventacarros.model.Cliente;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
/**
 *
 * @author informatica
 */
public class Main extends Application {

    private final String URL = "/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    private Cliente clienteActivo;
    private ObservableList<Administrador> listaAdmins;

    public Stage getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public Cliente getClienteActivo() {
        return clienteActivo;
    }

    public void setClienteActivo(Cliente clienteActivo) {
        this.clienteActivo = clienteActivo;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        listaAdmins = FXCollections.observableArrayList(listarAdmins());
        if (listaAdmins.isEmpty()) {
            crearCuentaAdmin();
            escenarioPrincipal.setTitle("Admin");
            escenarioPrincipal.show();
        } else {
            Inicio();
            escenarioPrincipal.setTitle("Inicio");
            escenarioPrincipal.show();
        }
    }

    public Initializable cambiarEscena(String fxml, double ancho, double alto) throws IOException {
        Initializable interfazCargada = null;
        FXMLLoader cargadorFXML = new FXMLLoader();

        InputStream archivoFXML = Main.class.getResourceAsStream(URL + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Main.class.getResource(URL + fxml));

        escena = new Scene(cargadorFXML.load(archivoFXML), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        escenarioPrincipal.setFullScreen(true);
        escenarioPrincipal.setFullScreenExitHint("");
        interfazCargada = cargadorFXML.getController();

        return interfazCargada;
    }

    public void Inicio() {
        try {
            InicioController ic = (InicioController) cambiarEscena("InicioView.fxml", 414, 473);
            ic.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cambiar escena");
            e.printStackTrace();
        }
    }

    public void Productos() {
        try {
            ProductosController pc = (ProductosController) cambiarEscena("ProductosView.fxml", 900, 527);
            pc.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cambiar escena");
            e.printStackTrace();
        }
    }

    public void Clientes() {
        try {
            ClientesVController cc = (ClientesVController) cambiarEscena("ClientesView.fxml", 900, 527);
            cc.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cambiar de escena");
            e.printStackTrace();
        }
    }

    public void Compras() {
        try {
            ComprasController coc = (ComprasController) cambiarEscena("ComprasView.fxml", 900, 527);
            coc.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cambiar de escena");
            e.printStackTrace();
        }
    }

    public void MenuPrincipal() {
        try {
            MenuPrincipalController mcp = (MenuPrincipalController) cambiarEscena("MenuPrincipal.fxml", 819, 527);
            mcp.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cargar escena");
            e.printStackTrace();
        }
    }

    public void Registrarse() {
        try {
            RegistroController rc = (RegistroController) cambiarEscena("RegistroView.fxml", 414, 643);
            rc.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cambiar escena");
            e.printStackTrace();
        }
    }

    public void Admins() {
        try {
            AdministradorViewController avc = (AdministradorViewController) cambiarEscena("AdministradorView.fxml", 900, 527);
            avc.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cargar la escena");
            e.printStackTrace();
        }
    }

    public void MenuPrincipalAdmins() {
        try {
            MenuAdministradorController mac = (MenuAdministradorController) cambiarEscena("MenuAdministradorView.fxml", 819, 527);
            mac.setPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error al cargar escena");
            e.printStackTrace();
        }
    }

    public void crearCuentaAdmin() {
        try {
            CrearCuentaAdminController ccac = (CrearCuentaAdminController) cambiarEscena("CrearCuentaAdmin.fxml", 414, 643);
            ccac.setPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al cargar vista");
            e.printStackTrace();
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
}

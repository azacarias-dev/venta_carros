package org.ventacarros.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ventacarros.controller.InicioController;

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
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        Inicio();
        escenarioPrincipal.setTitle("Inicio");
        escenarioPrincipal.show();
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

        interfazCargada = cargadorFXML.getController();

        return interfazCargada;
    }
    
    public void Inicio(){
        try {
            InicioController ic = (InicioController) cambiarEscena("InicioView.fxml", 400, 600);
            ic.setPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al cambiar escena");
            e.printStackTrace();
        }
    }
}

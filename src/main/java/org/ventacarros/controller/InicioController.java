package org.ventacarros.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class InicioController implements Initializable {
    
    private Main principal;
    
    @FXML
    private Button btnIngresar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    @FXML
    private void Ingresar(ActionEvent evento){
        if (evento.getSource() == btnIngresar) {
            principal.Productos();
        }
    }
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

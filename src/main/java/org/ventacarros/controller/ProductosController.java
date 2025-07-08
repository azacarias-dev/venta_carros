/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ventacarros.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class ProductosController implements Initializable {

    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
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
    
}

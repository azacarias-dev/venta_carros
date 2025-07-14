/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ventacarros.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author jgome
 */
public class MenuPrincipalController implements Initializable {

    private Main principal;
    @FXML
    private Button btnRegresar, btnClientes, btnCompras, btnProductos;
    
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
    private void clickHandlerAction (ActionEvent evento){
        if (evento.getSource() == btnRegresar){
            principal.Inicio();
        } else if (evento.getSource() == btnClientes){
            principal.Clientes();
        } else if (evento.getSource() == btnProductos){
            principal.Productos();
        } else if (evento.getSource() == btnCompras){
            principal.Compras();
        }
    }
    
}

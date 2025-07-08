package org.ventacarros.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.ventacarros.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class InicioController implements Initializable {
    
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
    
}

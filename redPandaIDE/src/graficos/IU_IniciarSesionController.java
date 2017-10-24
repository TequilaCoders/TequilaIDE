package graficos;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_IniciarSesionController implements Initializable {

    @FXML
    private TextField txUsuario;
    @FXML
    private PasswordField txClave;
    @FXML
    private Label lbUsuario;
    @FXML
    private Label lbClave;
    @FXML
    private JFXButton botonIngresar;
    @FXML
    private Hyperlink botonCrearCuenta;
    @FXML
    private Hyperlink botonAleman;
    @FXML
    private Hyperlink botonIngles;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void eventoCrearCuenta(ActionEvent event) {
        
    }
    
}

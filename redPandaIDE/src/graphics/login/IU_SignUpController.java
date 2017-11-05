package graphics.login;

import graphics.login.IU_LogInController;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_SignUpController implements Initializable {
    private ResourceBundle rb; 
    @FXML
    private Hyperlink botonIniciarSesion;
    private IU_LogInController parentController; 
    @FXML
    private JFXTextField tfName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb; 
    }    

    @FXML
    private void eventLogIn(ActionEvent event) throws InterruptedException {
        parentController.closeDrawer();
    }
    
    public void setParentController(IU_LogInController parentController) {
        this.parentController = parentController;
    }
}

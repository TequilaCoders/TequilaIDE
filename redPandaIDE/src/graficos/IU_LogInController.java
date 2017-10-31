/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graficos;


import com.jfoenix.controls.JFXDrawer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García Cruz
 */
public class IU_LogInController implements Initializable {
    @FXML
    private JFXDrawer drawerRegistrar;
    private ResourceBundle rb;

    /**
     * Inicia la clase controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }

    @FXML
    private void openDrawer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/graficos/IU_SignUp.fxml"), rb);
            Pane pane = loader.load();
            IU_SignUpController controller = loader.getController();
            drawerRegistrar.setSidePane(pane);
            controller.setParentController(this);
        } catch (Exception ex) {
            Logger.getLogger(IU_LogInController.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawerRegistrar.toFront();
        drawerRegistrar.open();
    }

    public void closeDrawer() {
        drawerRegistrar.close();
        drawerRegistrar.toBack();
    }

    @FXML
    void changeLanguage(ActionEvent event) throws IOException {
        String language = ((Hyperlink) event.getSource()).getText();
        switch (language) {
            case "Español":
                rb = ResourceBundle.getBundle("recursos/idiomas.idioma");
                break;
            case "English":
                rb = ResourceBundle.getBundle("recursos/idiomas.idioma_en_US");
                break;
            case "Deutsche":
                rb = ResourceBundle.getBundle("recursos/idiomas.idioma_de_DE");
                break;
        }
        Stage mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        reload(mainWindow);
    }

    public void reload(Stage mainWindow) throws IOException {
        Parent reloadWindow;
        reloadWindow = (AnchorPane) FXMLLoader.load(getClass().getResource("/graficos/IU_LogIn.fxml"), rb);
        Scene newScene;
        newScene = new Scene(reloadWindow);
        mainWindow.setScene(newScene);
    }

}

package main;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class RedPandaIDE extends Application {

  private Stage stagePrincipal;
  private AnchorPane rootPane;

  @Override
  public void start(Stage stagePrincipal) throws Exception {
    this.stagePrincipal = stagePrincipal;
    mostrarVentanaPrincipal();
  }

  public void mostrarVentanaPrincipal() {
    ResourceBundle bundle = ResourceBundle.getBundle("recursos/idiomas.idioma");
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/graficos/IU_LogIn.fxml"),bundle);
    try {
      rootPane = (AnchorPane) loader.load();
    } catch (IOException ex) {
      Logger.getLogger(RedPandaIDE.class.getName()).log(Level.SEVERE, null, ex);
    }
    Scene scene = new Scene(rootPane);
    stagePrincipal.setScene(scene);
    //stagePrincipal.setMaximized(true);
    stagePrincipal.show();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}

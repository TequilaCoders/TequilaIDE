package main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

  /*
  Metodo que carga el escenario IU_Ingreso o IU_Ingreso_BajaResolucion, dependiendo de la resolución
  de la pantalla
  */
  public void mostrarVentanaPrincipal() {

    //EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");

    //EntityManager entitymanager = emfactory.createEntityManager();
    //se obtiene la resolución de la pantalla
    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

    double ancho = visualBounds.getWidth();
    double alto = visualBounds.getHeight();

    FXMLLoader loader = null;
    
    //dependiendo del alto y ancho de la pantalla se cargara la interfaz de baja o alta resolución 
    if (alto < 1400 || ancho < 800) {
      loader = new FXMLLoader(getClass().getResource("/graficos/explorador/IU_Explorador.fxml"));
    } else {
      loader = new FXMLLoader(getClass().getResource("/graficos/IU_IniciarSesion.fxml"));
    }

    try {
      rootPane = (AnchorPane) loader.load();
    } catch (IOException ex) {
      Logger.getLogger(RedPandaIDE.class.getName()).log(Level.SEVERE, null, ex);
    }
    Scene scene = new Scene(rootPane);
 
    stagePrincipal.initStyle(StageStyle.UNDECORATED);
    stagePrincipal.setScene(scene);
    stagePrincipal.show();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}

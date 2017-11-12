package main;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        ResourceBundle bundle = ResourceBundle.getBundle("resources/languages.language");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/fileExplorer/IU_FileExplorer.fxml"), bundle);
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Editor.fxml"), bundle);

        //se obtiene la resolución de la pantalla
        /*Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        double ancho = visualBounds.getWidth();
        double alto = visualBounds.getHeight();

        FXMLLoader loader = null;

        //dependiendo del alto y ancho de la pantalla se cargara la interfaz de baja o alta resolución 
        if (alto < 1400 || ancho < 800) {
          loader = new FXMLLoader(getClass().getResource("/graficos/explorador/IU_Explorador.fxml"));
        } else {
          loader = new FXMLLoader(getClass().getResource("/graficos/IU_IniciarSesion.fxml"));
        }*/
        try {
            rootPane = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(RedPandaIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(rootPane);
        stagePrincipal.initStyle(StageStyle.UNIFIED);
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

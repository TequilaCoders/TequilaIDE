package tequilaide;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class TequilaIDE extends Application {
  public static Socket socket;
  private Stage stagePrincipal;
  private AnchorPane rootPane;

  @Override
  public void start(Stage stagePrincipal) throws Exception {
	this.stagePrincipal = stagePrincipal;
	//openConnection();
	showMainWindows();
  }

  /**
   * Muestra la ventana principal del sistema
   */
  public void showMainWindows() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources/languages.language");
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/login/IU_LogIn.fxml"), bundle);

	try {
	  rootPane = loader.load();
	} catch (IOException ex) {
	  Logger.getLogger(TequilaIDE.class.getName()).log(Level.SEVERE, null, ex);
	}
	Scene scene = new Scene(rootPane);

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

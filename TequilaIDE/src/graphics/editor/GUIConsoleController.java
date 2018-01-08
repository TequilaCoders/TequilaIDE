package graphics.editor;

import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class GUIConsoleController implements Initializable {
  private ResourceBundle rb;
  @FXML
  private TextArea taConsole;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	this.rb = rb;
	listenServer();
  }  
  
  /**
   * Abre la ventana GUIConsole
   * @param fileExplorerStage
   * @param rb 
   */
  public void openConsole(Stage fileExplorerStage, ResourceBundle rb){
	Stage stagePrincipal = new Stage();
	try {
	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/GUIConsole.fxml"), rb);
	  Parent root = (Parent) loader.load();
	  Scene scene = new Scene(root);
	  stagePrincipal.setScene(scene);
	  stagePrincipal.show();
	} catch (IOException ex) {
	  Logger.getLogger(GUIConsoleController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }
  
  /**
   * Se mantiene a la escucha de eventos emitidos por el servidor. 
   */
  public void listenServer() {
	socket.on("compilationFinish", new Emitter.Listener() {
	  @Override
	  public void call(Object... os) {
		Platform.runLater(() -> {
		  String intStringCompilationResult;
		  if ((int) os[0] == 0) {
			intStringCompilationResult = rb.getString("successfulCompilation");
			taConsole.setText(taConsole.getText() + "\n\n" + intStringCompilationResult);
		  } else {
			if (os.length > 1) {
			  taConsole.setText(taConsole.getText() + "\n\n" + os[1]);
			}
		  }
		});
	  }
	});
	
	socket.on("operationFinish", new Emitter.Listener() {
	  @Override
	  public void call(Object... os) {
		Platform.runLater(() -> taConsole.setText(taConsole.getText() + "\n\n" + (String) os[0]));
	  }
	});
  }
}

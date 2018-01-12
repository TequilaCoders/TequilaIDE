package graphics.login;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import graphics.explorer.GUIFileExplorerController;
import graphics.tools.Tools;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.domain.Project;
import logic.domain.User;
import logic.sockets.SocketUser;
import org.json.JSONObject;
import tequilaide.TequilaIDE;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García Cruz
 */
public class GUILogInController implements Initializable {

  @FXML
  private JFXDrawer drawerRegistrar;
  @FXML
  private JFXTextField tfUser;
  @FXML
  private JFXPasswordField pfPassword;
  @FXML
  private ImageView aliasRedCross;
  @FXML
  private ImageView passwordRedCross;
  @FXML
  private Hyperlink hpGithub;
  
  List<Project> projectList = new ArrayList<>();
  private ResourceBundle rb;
  private boolean changingLanguage;
  
  /**
   * Inicia la clase controller
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    changingLanguage = false;
    this.rb = rb;
	
	hpGithub.setOnAction(event -> goToURL("https://github.com/TequilaCoders/TequilaIDE"));
    
    drawerRegistrar.setOnDrawerClosed(event -> {
      drawerRegistrar.toBack();
    });
	
	tfUser.setOnKeyPressed(event ->{ 
	  GUISignUpController signController = new GUISignUpController();
	  String intStringPromtAlias = rb.getString("promptAlias");
	  signController.resetTextFieldMessage(intStringPromtAlias, tfUser, aliasRedCross);
	});
	
	pfPassword.setOnKeyPressed(event ->{
	  GUISignUpController signController = new GUISignUpController();
	  String intStringPromtPassword = rb.getString("promptPassword");
	  signController.resetPasswordMessage(intStringPromtPassword, pfPassword, passwordRedCross);
	});
	
    openConnection();
	listenServer();
  }

  /**
   * Carga la ventana GUISignUp en un JFXDrawer
   * @param event 
   */
  @FXML
  private void openDrawer(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/login/GUISignUp.fxml"), rb);
      Pane pane = loader.load();
      GUISignUpController controller = loader.getController();

      drawerRegistrar.setSidePane(pane);
      controller.setParentController(this);
    } catch (IOException ex) {
      Logger.getLogger(GUILogInController.class.getName()).log(Level.SEVERE, null, ex);
    }
    drawerRegistrar.toFront();
    drawerRegistrar.open();
  }

  public void closeDrawer() {
    drawerRegistrar.close();
  }

  /**
   * Actualiza el idioma de la interfaz 
   * @param event
   * @throws IOException 
   */
  @FXML
  void changeLanguage(ActionEvent event) throws IOException {
    String language = ((Hyperlink) event.getSource()).getText();
    switch (language) {
      case "Español":
        rb = ResourceBundle.getBundle("resources/languages.language");
        break;
      case "English":
        rb = ResourceBundle.getBundle("resources/languages.language_en_US");
        break;
      default:
        rb = ResourceBundle.getBundle("resources/languages.language_de_DE");
        break;
    }
    Stage mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
    reload(mainWindow);
  }

  /**
   * Recarga la interfaz gráfica 
   * @param mainWindow
   * @throws IOException 
   */
  public void reload(Stage mainWindow) throws IOException {
    changingLanguage = true;
    socket.close();
    Parent reloadWindow;
    reloadWindow = (AnchorPane) FXMLLoader.load(getClass().getResource("/graphics/login/GUILogIn.fxml"), rb);
    Scene newScene;
    newScene = new Scene(reloadWindow);
    mainWindow.setScene(newScene);
  }

  /**
   * Emite un método para ingresar al sistema y se mantiene a la escucha de una respuesta por parte
   * del servidor.
   * @param event 
   */
  @FXML
  public void eventLogIn(ActionEvent event) {
    String alias = tfUser.getText();
    String password = Tools.getHashedPassword(pfPassword.getText());
    
    SocketUser socketUser = new SocketUser();
    socketUser.accesUser(alias, password);
  }

  /**
   * Recibe un JSONObjet y lo convierte en un objeto de tipo User
   * @param receivedObject
   * @return User 
   */
  public User createUser(Object receivedObject) {
    JSONObject objectRecovered;
    String jsonString;
    User receivedUser;
    objectRecovered = (JSONObject) receivedObject;
    jsonString = objectRecovered.toString();
    Gson gson = new Gson();
    receivedUser = gson.fromJson(jsonString, User.class);
  
    return receivedUser;
  }
  
  /**
   * Carga la ventana de login
   * @param mainStage
   * @param rb 
   */
  public void openLogIn(Stage mainStage, ResourceBundle rb){
    AnchorPane rootPane;
    Stage stagePrincipal = new Stage();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/login/GUILogIn.fxml"), rb);

    try {
      rootPane = loader.load();
      Scene scene = new Scene(rootPane);
      stagePrincipal.setScene(scene);
      stagePrincipal.show();
      mainStage.close();
    } catch (IOException ex) {
      Logger.getLogger(TequilaIDE.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * Abre la conexión entre el cliente y el servidor.
   */
  public void openConnection() {
    try {
      socket = IO.socket("http://localhost:7000");
      socket.on(Socket.EVENT_DISCONNECT, (Object... os) -> {
        Platform.runLater(() -> {
          if (!changingLanguage) { //Esto para que? 
            String intStringLostConnectionMessage = rb.getString("lostConnectionMessage");
            Tools.displayWarningAlert(intStringLostConnectionMessage, rb);
          }
        });
      }).on(Socket.EVENT_RECONNECT, (Object... os) -> {
        Platform.runLater(() -> {
          if (!changingLanguage) {
            String intStringReestablishedConnectionMessage = rb.getString("reestablishedConnectionMessage");
            Tools.displayWarningAlert(intStringReestablishedConnectionMessage, rb);
          }
        });
      });
      socket.connect();
    } catch (URISyntaxException ex) {
      Logger.getLogger(TequilaIDE.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * Permite abrir una página web desde el navegador por defecto
   * @param url
   */
  public void goToURL(String url) {
	if (java.awt.Desktop.isDesktopSupported()) {
	  java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
	  if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
		try {
		  java.net.URI uri = new java.net.URI(url);
		  desktop.browse(uri);
		} catch (URISyntaxException | IOException ex) {
		  Logger.getLogger(GUILogInController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	  }
	}
  }
  
  public void listenServer(){
	socket.on("approved", (Object... os) -> {
     Platform.runLater(() -> {
		if ((boolean) os[0]) {
		  User userReceived = createUser(os[1]);
		  Stage stage = (Stage) tfUser.getScene().getWindow();
		  GUIFileExplorerController newScene = new GUIFileExplorerController();
		  newScene.openFileExplorer(stage, rb, userReceived);
		  
		} else {
		  GUISignUpController signController = new GUISignUpController();
		  if ((int) os[1] == 1) {
			String promptPassword = rb.getString("promptWrongPassword");
			signController.showPasswordMessage(promptPassword, pfPassword, passwordRedCross);
		  } else {
			String promptUser = rb.getString("promptWrongUser");
			signController.showTextFieldMessage(promptUser, tfUser, aliasRedCross);
		  }
		}
	  }
	  );
	});
  }


}

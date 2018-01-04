package graphics.login;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import graphics.explorer.IU_FileExplorerController;
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
import org.json.JSONException;
import org.json.JSONObject;
import tequilaide.TequilaIDE;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García Cruz
 */
public class IU_LogInController implements Initializable {

  @FXML
  private JFXDrawer drawerRegistrar;
  @FXML
  private JFXTextField tfUser;
  @FXML
  private JFXPasswordField pfPassword;
  
  List<Project> projectList = new ArrayList<>();

  private ResourceBundle rb;

  @FXML
  private ImageView aliasRedCross;

  @FXML
  private ImageView passwordRedCross;
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
    
    drawerRegistrar.setOnDrawerClosed(event -> {
      drawerRegistrar.toBack();
    });
	
	tfUser.setOnKeyPressed(event ->{ 
	  IU_SignUpController signController = new IU_SignUpController();
	  String intStringPromtAlias = rb.getString("promptAlias");
	  signController.resetTextFieldMessage(intStringPromtAlias, tfUser, aliasRedCross);
	});
	
	pfPassword.setOnKeyPressed(event ->{
	  IU_SignUpController signController = new IU_SignUpController();
	  String intStringPromtPassword = rb.getString("promptPassword");
	  signController.resetPasswordMessage(intStringPromtPassword, pfPassword, passwordRedCross);
	});
	
    openConnection();
  }

  @FXML
  private void openDrawer(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/login/IU_SignUp.fxml"), rb);
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
  }

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
      case "Deutsche":
        rb = ResourceBundle.getBundle("resources/languages.language_de_DE");
        break;
    }
    Stage mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
    reload(mainWindow);
  }

  public void reload(Stage mainWindow) throws IOException {
    changingLanguage = true;
    socket.close();
    Parent reloadWindow;
    reloadWindow = (AnchorPane) FXMLLoader.load(getClass().getResource("/graphics/login/IU_LogIn.fxml"), rb);
    Scene newScene;
    newScene = new Scene(reloadWindow);
    mainWindow.setScene(newScene);
  }

  @FXML
  public void eventLogIn(ActionEvent event) {
    String alias = tfUser.getText();
    String password = Tools.getHashedPassword(pfPassword.getText());
    
    SocketUser socketUser = new SocketUser();
    socketUser.accesUser(alias, password);

    socket.on("approved", (Object... os) -> {
     Platform.runLater(() -> {
		if ((boolean) os[0]) {
		  User userReceived = createUser(os[1]);
		  System.out.println("usuario " + userReceived.getAlias());
		  Stage stage = (Stage) tfUser.getScene().getWindow();
		  IU_FileExplorerController newScene = new IU_FileExplorerController();
		  newScene.openFileExplorer(stage, rb, userReceived);
		  
		} else {
		  IU_SignUpController signController = new IU_SignUpController();
		  if ((int) os[1] == 1) {
			signController.showPasswordMessage("Clave incorrecta", pfPassword, passwordRedCross);
		  } else {
			signController.showTextFieldMessage("El usuario no existe", tfUser, aliasRedCross);
		  }
		}
	  }
	  );
	});

  }

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
  
  public void openLogIn(Stage logInStage, ResourceBundle rb){
    AnchorPane rootPane;
    Stage stagePrincipal = new Stage();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/login/IU_LogIn.fxml"), rb);

    try {
      rootPane = loader.load();

      Scene scene = new Scene(rootPane);

      stagePrincipal.setScene(scene);
      stagePrincipal.show();
      
      logInStage.close();
    } catch (IOException ex) {
      Logger.getLogger(TequilaIDE.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * Abre la conexión entre el cliente y el servidor
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

}

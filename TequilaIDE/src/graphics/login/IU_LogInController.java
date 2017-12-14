package graphics.login;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import graphics.explorer.IU_FileExplorerController;
import graphics.tools.Tools;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.domain.Project;
import logic.domain.User;
import org.json.JSONException;
import org.json.JSONObject;

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
  public static Socket socket;

  /**
   * Inicia la clase controller
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
    drawerRegistrar.setOnDrawerClosed(event -> {
      drawerRegistrar.toBack();
    });

    try {
      socket = IO.socket("http://localhost:7000");
	  socket.connect();
    } catch (URISyntaxException ex) {
      Logger.getLogger(IU_LogInController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    socket.on("connectToRoom", new Emitter.Listener(){
            @Override                        
            public void call(Object... os) {
              System.out.println("usuario " + tfUser.getText());
                System.out.println("conectado a cuarto");
                System.out.println("objeto recibido " + Arrays.toString(os));
            }                                            
        });
    
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
    Parent reloadWindow;
    reloadWindow = (AnchorPane) FXMLLoader.load(getClass().getResource("/graphics/login/IU_LogIn.fxml"), rb);
    Scene newScene;
    newScene = new Scene(reloadWindow);
    mainWindow.setScene(newScene);
  }

  @FXML
  void eventLogIn(ActionEvent event) {
    JSONObject user = new JSONObject();

    String alias = tfUser.getText();
    String password = Tools.getHashedPassword(pfPassword.getText());

    try {
      user.accumulate("alias", alias);
      user.accumulate("clave", password);
    } catch (JSONException ex) {
      Logger.getLogger(IU_LogInController.class.getName()).log(Level.SEVERE, null, ex);
    }

    socket.connect();
    System.err.println("logrado");

    socket.emit("access", user);
    socket.on("approved", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        Platform.runLater(
            () -> {
              if ((boolean) os[0]) {
                
                User userReceived = createUser(os[1]);
                System.out.println("usuario " + userReceived.getAlias());
                Stage stage = (Stage) tfUser.getScene().getWindow();
                IU_FileExplorerController newScene = new IU_FileExplorerController();
                newScene.open_FileExplorer(stage, rb, userReceived);
            
                //socket.disconnect();
              } else {
                System.out.println((String) os[1]);
                //socket.disconnect();
              }
            }
        );
      }
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
}

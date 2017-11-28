package graphics.login;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
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
import javax.xml.bind.DatatypeConverter;
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
      System.err.println("logrado");
    } catch (URISyntaxException ex) {
      Logger.getLogger(IU_LogInController.class.getName()).log(Level.SEVERE, null, ex);
    }
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
    String password = getHashedPassword(pfPassword.getText());

    try {
      user.accumulate("alias", alias);
      user.accumulate("clave", password);
    } catch (JSONException ex) {
      Logger.getLogger(IU_LogInController.class.getName()).log(Level.SEVERE, null, ex);
    }

    socket.emit("access", user);
    socket.on("approved", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        Platform.runLater(
            () -> {
              if ((boolean) os[0]) {
                System.out.println("Si paso");
                loadExplorer();
              } else {
                System.out.println((String) os[1]);
              }
            }
        );
      }
    });

  }

  public void loadExplorer() {
    AnchorPane pane = null;
    try {
      pane = FXMLLoader.load(getClass().getResource("/graphics/fileExplorer/IU_FileExplorer.fxml"), rb);
    } catch (IOException ex) {
      Logger.getLogger(IU_LogInController.class.getName()).log(Level.SEVERE, null, ex);
    }
    Scene scenePartida = new Scene(pane);
    Stage stage = (Stage) drawerRegistrar.getScene().getWindow();
    stage.setScene(scenePartida);
    stage.show();
  }

  private String getHashedPassword(String password) {
    String result = null;

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes("UTF-8"));
      return bytesToHex(hash); // make it printable
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  private String bytesToHex(byte[] hash) {
    return DatatypeConverter.printHexBinary(hash);
  }
}

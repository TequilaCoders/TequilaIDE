package graphics.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import static graphics.login.IU_LogInController.socket;
import com.jfoenix.controls.JFXTextField;
import graphics.tools.Tools;
import io.socket.emitter.Emitter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_SignUpController implements Initializable {

  private ResourceBundle rb;

  @FXML
  private Hyperlink botonIniciarSesion;

  private IU_LogInController parentController;

  @FXML
  private JFXTextField tfName;

  @FXML
  private JFXButton buttonSignUp;

  @FXML
  private JFXTextField tfAlias;

  @FXML
  private JFXTextField tfEmail;

  @FXML
  private JFXPasswordField pfPassword;

  @FXML
  private JFXPasswordField pfConfirmPassword;

  @FXML
  private ImageView imAliasRedCross;

  @FXML
  private ImageView imEmailRedCross;

  @FXML
  private ImageView imPasswordRedCross;

  boolean emailStatus = false;
  boolean aliasStatus = false;

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
  }

  @FXML
  private void eventLogIn(ActionEvent event) throws InterruptedException {
    parentController.closeDrawer();
  }
  
  private void eventLogIn() throws InterruptedException {
    parentController.closeDrawer();
  }

  public void setParentController(IU_LogInController parentController) {
    this.parentController = parentController;
  }

  @FXML
  void registerUser(ActionEvent event) {
    String name;
    String alias;
    String email;
    String password;
    String hashedPassword;

    name = tfName.getText();
    alias = tfAlias.getText().toLowerCase();
    email = tfEmail.getText().toLowerCase();
    password = pfPassword.getText();

    hashedPassword = Tools.getHashedPassword(password);
    createUser(name, alias, email, hashedPassword);

  }

  @FXML
  void verifyFieldsListener(KeyEvent event) {
    String name;
    String alias;
    String email;
    String password;
    String confirmedPassword;

    name = tfName.getText();
    alias = tfAlias.getText().toLowerCase();
    email = tfEmail.getText().toLowerCase();
    password = pfPassword.getText();
    confirmedPassword = pfConfirmPassword.getText();

    boolean emptyStatus = areThereEmptyFields(name, alias, email, password, confirmedPassword);
    isAliasDuplicate();
    isEmailDuplicate();

    boolean passwordStatus = isPasswordConfirmationCorrect(password, confirmedPassword);

    //BLOQUE A ELIMINAR!!------------------------
    /*System.out.println("empty: " + emptyStatus);
    System.out.println("alias: " + aliasStatus);
    System.out.println("email: " + emailStatus);
    System.out.println("password: " + passwordStatus);*/
    //---------------------------------------------
    if (!emptyStatus && !aliasStatus && !emailStatus && passwordStatus) {
      buttonSignUp.setDisable(false);
    } else {
      buttonSignUp.setDisable(true);
    }
  }

  public void createUser(String name, String alias, String email, String password) {

    JSONObject userToSend = new JSONObject();

    userToSend.accumulate("name", name);
    userToSend.accumulate("alias", alias);
    userToSend.accumulate("email", email);
    userToSend.accumulate("password", password);

    socket.connect();
    System.err.println("logrado");

    socket.emit("saveUser", userToSend);
    socket.on("registrationSuccesful", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        Platform.runLater(
            () -> {
              if ((boolean) os[0]) {
           
                Tools.displayConfirmationAlert(null, "Usuario registrado exitosamente");
                try {
                  eventLogIn();
                } catch (InterruptedException ex) {
                  Logger.getLogger(IU_SignUpController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
              } else {
                System.out.println((String) os[1]);
              }
            }
        );
      }

    });
 
  }

  public boolean areThereEmptyFields(String name, String alias, String email, String password, String confirmedPassword) {
    boolean isEmpty = true;
    if (!name.trim().isEmpty() && !alias.trim().isEmpty() && !email.trim().isEmpty()
        && !password.trim().isEmpty() && !confirmedPassword.trim().isEmpty()) {
      isEmpty = false;
    }
    return isEmpty;
  }

  public void isAliasDuplicate() {
    tfAlias.setOnKeyTyped((KeyEvent event) -> {
      JSONObject aliasToSend = new JSONObject();

      String alias = tfAlias.getText().toLowerCase() + event.getCharacter().toLowerCase();

      aliasToSend.accumulate("alias", alias);
      
      socket.connect();
      System.err.println("logrado");
      
      socket.emit("aliasChanged", aliasToSend);

      socket.on("aliasDuplicated", new Emitter.Listener() {
        @Override
        public void call(Object... os) {
          Platform.runLater(
              () -> {
                if ((boolean) os[0]) {

                  System.out.println("esta duplicado");
                  aliasStatus = true;
                  tfAlias.setFocusColor(Paint.valueOf("orange"));
                  tfAlias.setUnFocusColor(Paint.valueOf("orange"));
                  tfAlias.setPromptText("Alias (ya existe el Alias)");
                  tfAlias.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
                  imAliasRedCross.setVisible(true);
                } else {
                  //System.out.println((String) os[1]);

                  System.out.println("no esta duplicado");
                  aliasStatus = false;
                  tfAlias.setFocusColor(Paint.valueOf("#77d2ff"));
                  tfAlias.setUnFocusColor(Paint.valueOf("#17a589"));

                  tfAlias.setPromptText("Alias");
                  tfAlias.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
                  imAliasRedCross.setVisible(false);
                }
              }
          );
        }
      });
    });
  }

  public void isEmailDuplicate() {
    tfEmail.setOnKeyTyped((KeyEvent event) -> {
      JSONObject emailToSend = new JSONObject();

      String email = tfEmail.getText().toLowerCase() + event.getCharacter().toLowerCase();

      emailToSend.accumulate("email", email);
      
      socket.connect();
      System.err.println("logrado");
      
      socket.emit("emailChanged", emailToSend);

      socket.on("emailDuplicated", new Emitter.Listener() {
        @Override
        public void call(Object... os) {
          Platform.runLater(
              () -> {
                if ((boolean) os[0]) {

                  System.out.println("esta duplicado");
                  emailStatus = true;
                  tfEmail.setFocusColor(Paint.valueOf("orange"));
                  tfEmail.setUnFocusColor(Paint.valueOf("orange"));
                  tfEmail.setPromptText("Email (ya existe el Email)");
                  tfEmail.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
                  imEmailRedCross.setVisible(true);
                } else {
                  //System.out.println((String) os[1]);

                  System.out.println("no esta duplicado");
                  emailStatus = false;
                  tfEmail.setFocusColor(Paint.valueOf("#77d2ff"));
                  tfEmail.setUnFocusColor(Paint.valueOf("#17a589"));
                  tfEmail.setPromptText("Email");
                  tfEmail.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");

                  imEmailRedCross.setVisible(false);
                }
              }
          );
        }
      });
    });
  }

  public boolean isPasswordConfirmationCorrect(String password, String confirmedPassword) {
    boolean status = false;
    if (password.equals(confirmedPassword) || pfConfirmPassword.getText().isEmpty()) {
      status = true;
      pfConfirmPassword.setFocusColor(Paint.valueOf("#77d2ff"));
      pfConfirmPassword.setUnFocusColor(Paint.valueOf("#77d2ff"));
      pfConfirmPassword.setPromptText("Confirmar Contraseña");
      pfConfirmPassword.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
      imPasswordRedCross.setVisible(false);
    } else {
      pfConfirmPassword.setFocusColor(Paint.valueOf("orange"));
      pfConfirmPassword.setUnFocusColor(Paint.valueOf("orange"));
      pfConfirmPassword.setPromptText("Confirm Password (las claves no coinciden)");
      pfConfirmPassword.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
      imPasswordRedCross.setVisible(true);
    }
    return status;
  }
}

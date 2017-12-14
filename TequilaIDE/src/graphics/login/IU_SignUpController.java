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
import logic.sockets.SocketUser;

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
	listenServer();
  }

  @FXML
  public void eventLogIn(ActionEvent event) throws InterruptedException {
    parentController.closeDrawer();
  }
  
  public void eventLogIn() throws InterruptedException {
    parentController.closeDrawer();
  }

  public void setParentController(IU_LogInController parentController) {
    this.parentController = parentController;
  }

  @FXML
  public void registerUser(ActionEvent event) {
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

	SocketUser socketUser = new SocketUser();
	socketUser.createUser(name, alias, email, hashedPassword);
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
    if (!emptyStatus && !aliasStatus && !emailStatus && passwordStatus) {
      buttonSignUp.setDisable(false);
    } else {
      buttonSignUp.setDisable(true);
    }
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
	  String alias = tfAlias.getText().toLowerCase() + event.getCharacter().toLowerCase();
      SocketUser socketUser = new SocketUser();
	  socketUser.checkAlias(alias);
    });
  }

  public void isEmailDuplicate() {
    tfEmail.setOnKeyTyped((KeyEvent event) -> {
	  String email = tfEmail.getText().toLowerCase() + event.getCharacter().toLowerCase();
	  SocketUser socketUser = new SocketUser();
	  socketUser.checkEmail(email);
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
  
  public void listenServer() {
	socket.on("registrationSuccesful", new Emitter.Listener() {
	  @Override
	  public void call(Object... os) {
		Platform.runLater(() -> {
		  registrationSuccesful((boolean) os[0]);
		}
		);
	  }
	});

	socket.on("aliasDuplicated", new Emitter.Listener() {
	  @Override
	  public void call(Object... os) {
		Platform.runLater(() -> {
		  aliasDuplicated((boolean) os[0]);
		}
		);
	  }
	});
	
	socket.on("emailDuplicated", new Emitter.Listener() {
	  @Override
	  public void call(Object... os) {
		Platform.runLater(() -> {
		  emailDuplicated((boolean) os[0]);
		}
		);
	  }
	});
  }
  
  public void aliasDuplicated(boolean duplicated) {
	if (duplicated) {
	  aliasStatus = true;
	  tfAlias.setFocusColor(Paint.valueOf("orange"));
	  tfAlias.setUnFocusColor(Paint.valueOf("orange"));
	  tfAlias.setPromptText("Alias (ya existe el Alias)");
	  tfAlias.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
	  imAliasRedCross.setVisible(true);
	} else {
	  aliasStatus = false;
	  tfAlias.setFocusColor(Paint.valueOf("#77d2ff"));
	  tfAlias.setUnFocusColor(Paint.valueOf("#17a589"));
	  tfAlias.setPromptText("Alias");
	  tfAlias.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
	  imAliasRedCross.setVisible(false);
	}
  }
  
  public void emailDuplicated(boolean duplicated) {
	if (duplicated) {
	  emailStatus = true;
	  tfEmail.setFocusColor(Paint.valueOf("orange"));
	  tfEmail.setUnFocusColor(Paint.valueOf("orange"));
	  tfEmail.setPromptText("Email (ya existe el Email)");
	  tfEmail.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
	  imEmailRedCross.setVisible(true);
	} else {
	  emailStatus = false;
	  tfEmail.setFocusColor(Paint.valueOf("#77d2ff"));
	  tfEmail.setUnFocusColor(Paint.valueOf("#17a589"));
	  tfEmail.setPromptText("Email");
	  tfEmail.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
	  imEmailRedCross.setVisible(false);
	}
  }

  public void registrationSuccesful(boolean registration) {
	if (registration) {
	  Tools.displayConfirmationAlert(null, "Usuario registrado exitosamente");
	  try {
		eventLogIn();
	  } catch (InterruptedException ex) {
		Logger.getLogger(IU_SignUpController.class.getName()).log(Level.SEVERE, null, ex);
	  }
	} else {
	  //CAMBIAR
	  System.out.println("Eror al registrar");
	}
  }
}

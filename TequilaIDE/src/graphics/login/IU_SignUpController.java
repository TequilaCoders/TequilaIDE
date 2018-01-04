package graphics.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import graphics.tools.Tools;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import logic.sockets.SocketUser;
import static tequilaide.TequilaIDE.socket;

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
  private ImageView imConfirmPasswordRedCross;

  @FXML
  private ImageView imPasswordRedCross;

  @FXML
  private ImageView imNameRedCross;

  boolean emailDuplicated = false;
  boolean aliasDuplicated = false;

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

    boolean nameIsReady = checkName(name, rb);

    boolean emailIsReady = checkEmail(email, rb);

    boolean aliasIsReady = checkAlias(alias, rb, tfAlias, imAliasRedCross);

    boolean passwordIsReady = checkPassword(password, rb);

    boolean passwordStatus = isPasswordConfirmationCorrect(password, confirmedPassword);
    
    if (!emptyStatus && aliasIsReady && emailIsReady && passwordStatus && nameIsReady && passwordIsReady) {
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

  public boolean checkName(String name, ResourceBundle rb) {
    boolean nameIsReady = false;
    boolean hasNameSpecialCharacters = Tools.applyRegularExpression(name, "^[\\p{L} .'-]+$");
    if (!hasNameSpecialCharacters) {
      String intStringNameWithSimbols = rb.getString("intStringNameWithSimbols");
      showTextFieldMessage(intStringNameWithSimbols, tfName, imNameRedCross);
    }

    boolean isLenghtOk = Tools.checkLenght(name, 5, 30);
    if (!isLenghtOk) {
      String intStringNameLenghtWrong = rb.getString("intStringNameLenghtWrong");
      showTextFieldMessage(intStringNameLenghtWrong, tfName, imNameRedCross);
    }

    if (hasNameSpecialCharacters && isLenghtOk) {
      String intStringPromtName = rb.getString("promptName");
      resetTextFieldMessage(intStringPromtName, tfName, imNameRedCross);
      nameIsReady = true;
    }
    return nameIsReady;
  }

  public boolean checkAlias(String alias, ResourceBundle rb, JFXTextField tfAlias, ImageView imAliasRedCross) {
    boolean aliasIsReady = false;
    boolean hasAliasSpecialCharacters = Tools.applyRegularExpression(alias, "[^A-Za-z0-9]");
    if (hasAliasSpecialCharacters) {
      String intStringAliasWithSimbols = rb.getString("intStringAliasWithSimbols");
      showTextFieldMessage(intStringAliasWithSimbols, tfAlias, imAliasRedCross);
    }

    boolean isLenghtOk = Tools.checkLenght(alias, 2, 15);
    if (!isLenghtOk) {
      String intStringAliasLenghtWrong = rb.getString("intStringAliasLenghtWrong");
      showTextFieldMessage(intStringAliasLenghtWrong, tfAlias, imAliasRedCross);
    }

    isAliasDuplicate(tfAlias);

    if (!aliasDuplicated && !hasAliasSpecialCharacters && isLenghtOk) {
      String intStringPromtAlias = rb.getString("promptAlias");
      resetTextFieldMessage(intStringPromtAlias, tfAlias, imAliasRedCross);
      aliasIsReady = true;
    }
    return aliasIsReady;
  }

  public boolean checkEmail(String email, ResourceBundle rb) {
    boolean emailIsReady = false;

    boolean formatIsOk = validateEmailFormat(email);
    if (!formatIsOk) {
      String intStringWrongEmailFormat = rb.getString("intStringWrongEmailFormat");
      showTextFieldMessage(intStringWrongEmailFormat, tfEmail, imEmailRedCross);
    }

    boolean isLenghtOk = Tools.checkLenght(email, 7, 30);
    if (!isLenghtOk) {
      String intStringEmailLenghtWrong = rb.getString("intStringEmailLenghtWrong");
      showTextFieldMessage(intStringEmailLenghtWrong, tfEmail, imEmailRedCross);
    }

    isEmailDuplicate();

    if (!emailDuplicated && formatIsOk && isLenghtOk) {
      String intStringPromtEmail = rb.getString("promptEmail");
      resetTextFieldMessage(intStringPromtEmail, tfEmail, imEmailRedCross);
      emailIsReady = true;
    }
    return emailIsReady;
  }

  public boolean checkPassword(String password, ResourceBundle rb) {
    boolean passwordIsReady = false;

    boolean hasNumber = Tools.applyRegularExpression(password, "(?=.*[0-9])");
    if (!hasNumber) {
      String intStringPasswordWithoutNumber = rb.getString("intStringPasswordWithoutNumber");
      showPasswordMessage(intStringPasswordWithoutNumber,pfPassword,imPasswordRedCross);
    }

    boolean isLenghtOk = Tools.checkLenght(password, 8, 40);
    if (!isLenghtOk) {
      String intStringPasswordLenghtWrong = rb.getString("intStringPasswordLenghtWrong");
      showPasswordMessage(intStringPasswordLenghtWrong, pfPassword, imPasswordRedCross);
    }

    if (isLenghtOk && hasNumber) {
	  String intStringPromptPassword = rb.getString("promptPassword");
      resetPasswordMessage(intStringPromptPassword, pfPassword, imPasswordRedCross);
      passwordIsReady = true;
    }
    return passwordIsReady;
  }

  public void isAliasDuplicate(JFXTextField tfAlias) {
    tfAlias.textProperty().addListener((observable, oldValue, newValue) -> {
      SocketUser socketUser = new SocketUser();
      socketUser.checkAlias(newValue);
    });
  }

  public void isEmailDuplicate() {
    tfEmail.textProperty().addListener((observable, oldValue, newValue) -> {
      SocketUser socketUser = new SocketUser();
      socketUser.checkEmail(newValue);
    });
  }

  public boolean isPasswordConfirmationCorrect(String password, String confirmedPassword) {
    boolean status = false;
    if (password.equals(confirmedPassword) || pfConfirmPassword.getText().isEmpty()) {
      status = true;
      pfConfirmPassword.setFocusColor(Paint.valueOf("#77d2ff"));
      pfConfirmPassword.setUnFocusColor(Paint.valueOf("#77d2ff"));
      
      String intStringResetPassword = rb.getString("promptConfirmPassword");
      
      pfConfirmPassword.setPromptText(intStringResetPassword);
      pfConfirmPassword.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
      imConfirmPasswordRedCross.setVisible(false);
    } else {
      pfConfirmPassword.setFocusColor(Paint.valueOf("orange"));
      pfConfirmPassword.setUnFocusColor(Paint.valueOf("orange"));
      
      String intStringWrongPasswordConfirmation = rb.getString("intStringWrongPasswordConfirmation");
      
      pfConfirmPassword.setPromptText(intStringWrongPasswordConfirmation);
      pfConfirmPassword.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
      imConfirmPasswordRedCross.setVisible(true);
    }
    return status;
  }

  public boolean validateEmailFormat(String emailField) {
    Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    Matcher matcher = pattern.matcher(emailField);
    return matcher.find();
  }

  public void listenServer() {
    socket.on("registrationSuccesful", (Object... os) -> {
      Platform.runLater(() -> {
        registrationSuccesful((boolean) os[0]);
      }
      );
    }).on("aliasDuplicated", (Object... os) -> {
      Platform.runLater(() -> {
        if ((boolean) os[0]) {
          aliasDuplicated = true;
          String intStringAliasDuplicated = rb.getString("intStringAliasDuplicated");
          showTextFieldMessage(intStringAliasDuplicated, tfAlias, imAliasRedCross);
        } else {
          aliasDuplicated = false;
        }
      }
      );
    }).on("emailDuplicated", (Object... os) -> {
      Platform.runLater(() -> {
        if ((boolean) os[0]) {
          emailDuplicated = true;
          String intStringEmailDuplicated = rb.getString("intStringEmailDuplicated");
          
          showTextFieldMessage(intStringEmailDuplicated, tfEmail, imEmailRedCross);
        } else {
          emailDuplicated = false;
        }
      }
      );
    });
  }

  public void showTextFieldMessage(String message, JFXTextField textField, ImageView ivRedCross) {
    textField.setFocusColor(Paint.valueOf("orange"));
    textField.setUnFocusColor(Paint.valueOf("orange"));
    textField.setPromptText(message);
    textField.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
    ivRedCross.setVisible(true);
  }

  public void resetTextFieldMessage(String message, JFXTextField textField, ImageView ivRedCross) {
    textField.setFocusColor(Paint.valueOf("#77d2ff"));
    textField.setUnFocusColor(Paint.valueOf("#17a589"));
    textField.setPromptText(message);
    textField.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
    ivRedCross.setVisible(false);

  }

  public void showPasswordMessage(String message, JFXPasswordField passwordField, ImageView ivRedCross) {
    passwordField.setFocusColor(Paint.valueOf("orange"));
    passwordField.setUnFocusColor(Paint.valueOf("orange"));
    passwordField.setPromptText(message);
    passwordField.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
    ivRedCross.setVisible(true);
  }

  public void resetPasswordMessage(String message, JFXPasswordField passwordField, ImageView ivRedCross) {
    passwordField.setFocusColor(Paint.valueOf("#77d2ff"));
    passwordField.setUnFocusColor(Paint.valueOf("#17a589"));
    passwordField.setPromptText(message);
    passwordField.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
    ivRedCross.setVisible(false);
  }

  public void registrationSuccesful(boolean registration) {
    if (registration) {
      String intStringRegistrationSuccesful = rb.getString("intStringRegistrationSuccesful");
      Tools.displayConfirmationAlert(intStringRegistrationSuccesful, rb);
      try {
        eventLogIn();
      } catch (InterruptedException ex) {
        Logger.getLogger(IU_SignUpController.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      //CAMBIAR
      System.out.println("Error al registrar");
    }
  }
}

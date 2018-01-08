package graphics.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import graphics.tools.Tools;
import java.net.URL;
import java.util.ResourceBundle;
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
public class GUISignUpController implements Initializable {
  
  @FXML
  private Hyperlink botonIniciarSesion;
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

  private GUILogInController parentController;
  private ResourceBundle rb;
  boolean emailDuplicated = false;
  boolean aliasDuplicated = false;

  private static final String FOCUS_COLOR = "orange";
  private static final String WRONG_FIELD_STYLE = "-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF";
  private static final String DEFAULT_PASSWORD_STYLE = "-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF";
  private static final String SPECIAL_CHARACTERS = "hasSpecialCharacters";
  private static final String WRONG_LENGHT = "lenghtIsWrong";
  private static final String NO_ERROR = "noError";
  private static final String PASSWORD_FIELD_FOCUS = "#77d2ff";

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
  
  public void setParentController(GUILogInController parentController) {
	this.parentController = parentController;
  }

  /**
   * Cierra el drawer en el controlador padre. 
   */
  @FXML
  public void eventLogIn() {
	parentController.closeDrawer();
  }
  
  /**
   * Registra el usuario en el sistema
   * @param event
   */
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

  /**
   * Verifica que los valores de los campos cumplan con las validaciones para poder registrar un
   * usuario
   * @param event
   */
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

	boolean nameIsReady = verifyNameField(name, rb);

	boolean emailIsReady = verifyEmailField(email, rb);

	boolean aliasIsReady = verifyAliasField(alias, rb);

	boolean passwordIsReady = verifyPasswordField(password, rb);

	boolean passwordConfirmationIsReady = verifyPasswordConfirmationField(password, confirmedPassword, rb);

	if (!emptyStatus && aliasIsReady && emailIsReady && passwordConfirmationIsReady && nameIsReady && passwordIsReady) {
	  buttonSignUp.setDisable(false);
	} else {
	  buttonSignUp.setDisable(true);
	}
  }

  /**
   * Verifica que los campos no esten vacios
   *
   * @param name
   * @param alias
   * @param email
   * @param password
   * @param confirmedPassword
   * @return
   */
  public boolean areThereEmptyFields(String name, String alias, String email, String password, String confirmedPassword) {
	boolean isEmpty = true;
	if (!name.trim().isEmpty() && !alias.trim().isEmpty() && !email.trim().isEmpty()
			&& !password.trim().isEmpty() && !confirmedPassword.trim().isEmpty()) {
	  isEmpty = false;
	}
	return isEmpty;
  }

  /**
   * Valida el campo name
   *
   * @param name
   * @param rb
   * @return
   */
  public boolean verifyNameField(String name, ResourceBundle rb) {
	boolean nameIsReady = false;
	String result = checkName(name);

	switch (result) {
	  case SPECIAL_CHARACTERS:
		String intStringNameWithSimbols = rb.getString("intStringNameWithSimbols");
		showTextFieldMessage(intStringNameWithSimbols, tfName, imNameRedCross);
		break;
	  case WRONG_LENGHT:
		String intStringNameLenghtWrong = rb.getString("intStringNameLenghtWrong");
		showTextFieldMessage(intStringNameLenghtWrong, tfName, imNameRedCross);
		break;
	  default:
		String intStringPromtName = rb.getString("promptName");
		resetTextFieldMessage(intStringPromtName, tfName, imNameRedCross);
		nameIsReady = true;
		break;
	}
	return nameIsReady;
  }

  /**
   * Verifica si la cadena recibida tiene caracteres especiales y es de la longitud adecuada
   *
   * @param name
   * @return
   */
  public String checkName(String name) {
	String nameStatus = NO_ERROR;
	boolean hasNameSpecialCharacters = Tools.applyRegularExpression(name, "^[\\p{L} .'-]+$");
	if (!hasNameSpecialCharacters) {
	  nameStatus = SPECIAL_CHARACTERS;
	}
	boolean isLenghtOk = Tools.checkLenght(name, 5, 30);
	if (!isLenghtOk) {
	  nameStatus = WRONG_LENGHT;
	}
	return nameStatus;
  }

  /**
   * Valida el campo alias
   *
   * @param alias
   * @param rb
   * @return
   */
  public boolean verifyAliasField(String alias, ResourceBundle rb) {
	boolean aliasIsReady = false;

	String result = checkAlias(alias);

	switch (result) {
	  case SPECIAL_CHARACTERS:
		String intStringAliasWithSimbols = rb.getString("intStringAliasWithSimbols");
		showTextFieldMessage(intStringAliasWithSimbols, tfAlias, imAliasRedCross);
		break;
	  case WRONG_LENGHT:
		String intStringAliasLenghtWrong = rb.getString("intStringAliasLenghtWrong");
		showTextFieldMessage(intStringAliasLenghtWrong, tfAlias, imAliasRedCross);
		break;
	  default:
		isAliasDuplicate(tfAlias);
		if (!aliasDuplicated) {
		  String intStringPromtAlias = rb.getString("promptAlias");
		  resetTextFieldMessage(intStringPromtAlias, tfAlias, imAliasRedCross);
		  aliasIsReady = true;
		}
		break;
	}

	return aliasIsReady;
  }

  /**
   * Verifica si la cadena recibida tiene caracteres especiales y es de longitud adecuada
   *
   * @param alias
   * @return
   */
  public String checkAlias(String alias) {
	String aliasStatus = NO_ERROR;
	boolean hasAliasSpecialCharacters = Tools.applyRegularExpression(alias, "[^A-Za-z0-9]");
	if (hasAliasSpecialCharacters) {
	  aliasStatus = SPECIAL_CHARACTERS;
	}

	boolean isLenghtOk = Tools.checkLenght(alias, 2, 15);
	if (!isLenghtOk) {
	  aliasStatus = WRONG_LENGHT;
	}
	return aliasStatus;
  }

  /**
   * Valida el campo email
   *
   * @param email
   * @param rb
   * @return
   */
  public boolean verifyEmailField(String email, ResourceBundle rb) {
	boolean emailIsReady = false;

	String result = checkEmail(email);

	switch (result) {
	  case "invalidFormat":
		String intStringWrongEmailFormat = rb.getString("intStringWrongEmailFormat");
		showTextFieldMessage(intStringWrongEmailFormat, tfEmail, imEmailRedCross);
		break;
	  case WRONG_LENGHT:
		String intStringEmailLenghtWrong = rb.getString("intStringEmailLenghtWrong");
		showTextFieldMessage(intStringEmailLenghtWrong, tfEmail, imEmailRedCross);
		break;
	  default:
		isEmailDuplicate(tfEmail);
		if (!emailDuplicated) {
		  String intStringPromtEmail = rb.getString("promptEmail");
		  resetTextFieldMessage(intStringPromtEmail, tfEmail, imEmailRedCross);
		  emailIsReady = true;
		}
		break;
	}
	return emailIsReady;
  }

  /**
   * Verifica si la cadena recibida cumple con el formato correcto para un E-mail y tiene la
   * longitud adecuada.
   *
   * @param email
   * @return
   */
  public String checkEmail(String email) {
	String emailStatus = NO_ERROR;

	boolean formatIsOk = validateEmailFormat(email);
	if (!formatIsOk) {
	  emailStatus = "invalidFormat";
	}

	boolean isLenghtOk = Tools.checkLenght(email, 7, 30);
	if (!isLenghtOk) {
	  emailStatus = WRONG_LENGHT;
	}
	return emailStatus;
  }

  /**
   * Valida el campo password
   *
   * @param password
   * @param rb
   * @return
   */
  public boolean verifyPasswordField(String password, ResourceBundle rb) {
	boolean passwordIsReady = false;

	String result = checkPassword(password);

	switch (result) {
	  case "hasNotNumber":
		String intStringPasswordWithoutNumber = rb.getString("intStringPasswordWithoutNumber");
		showPasswordMessage(intStringPasswordWithoutNumber, pfPassword, imPasswordRedCross);
		break;
	  case WRONG_LENGHT:
		String intStringPasswordLenghtWrong = rb.getString("intStringPasswordLenghtWrong");
		showPasswordMessage(intStringPasswordLenghtWrong, pfPassword, imPasswordRedCross);
		break;
	  default:
		String intStringPromptPassword = rb.getString("promptPassword");
		resetPasswordMessage(intStringPromptPassword, pfPassword, imPasswordRedCross);
		passwordIsReady = true;

		break;
	}
	return passwordIsReady;
  }

  /**
   * Verifica si la cadena recibida cumple con el formato para una contraseña y tiene la longitud
   * adecuada.
   *
   * @param password
   * @return
   */
  public String checkPassword(String password) {
	String passwordStatus = NO_ERROR;

	boolean hasNumber = Tools.applyRegularExpression(password, "(?=.*[0-9])");
	if (!hasNumber) {
	  passwordStatus = "hasNotNumber";
	}

	boolean isLenghtOk = Tools.checkLenght(password, 8, 40);
	if (!isLenghtOk) {
	  passwordStatus = WRONG_LENGHT;
	}
	return passwordStatus;
  }

  /**
   * Valida el campo confirmedPassword
   *
   * @param password
   * @param confirmedPassword
   * @param rb
   * @return
   */
  public boolean verifyPasswordConfirmationField(String password, String confirmedPassword, ResourceBundle rb) {
	boolean passwordConfirmationIsReady = false;

	String result = checkPasswordConfirmation(password, confirmedPassword);

	if (result.equals("isNotTheSame")) {
	  pfConfirmPassword.setFocusColor(Paint.valueOf(FOCUS_COLOR));
	  pfConfirmPassword.setUnFocusColor(Paint.valueOf(FOCUS_COLOR));

	  String intStringWrongPasswordConfirmation = rb.getString("intStringWrongPasswordConfirmation");

	  pfConfirmPassword.setPromptText(intStringWrongPasswordConfirmation);
	  pfConfirmPassword.setStyle(WRONG_FIELD_STYLE);
	  imConfirmPasswordRedCross.setVisible(true);
	} else {
	  passwordConfirmationIsReady = true;
	  pfConfirmPassword.setFocusColor(Paint.valueOf(PASSWORD_FIELD_FOCUS));
	  pfConfirmPassword.setUnFocusColor(Paint.valueOf(PASSWORD_FIELD_FOCUS));

	  String intStringResetPassword = rb.getString("promptConfirmPassword");

	  pfConfirmPassword.setPromptText(intStringResetPassword);
	  pfConfirmPassword.setStyle(DEFAULT_PASSWORD_STYLE);
	  imConfirmPasswordRedCross.setVisible(false);
	}
	return passwordConfirmationIsReady;
  }

  /**
   * Verifica que las dos cadenas recibidas sean iguales
   *
   * @param password
   * @param confirmedPassword
   * @return
   */
  public String checkPasswordConfirmation(String password, String confirmedPassword) {
	String passwordConfirmationStatus = NO_ERROR;

	if (!password.equals(confirmedPassword)) {
	  passwordConfirmationStatus = "isNotTheSame";
	}

	return passwordConfirmationStatus;
  }

  /**
   * Envia una solicitud al servidor para comprobar si un alias es duplicado.
   *
   * @param tfAlias
   */
  public void isAliasDuplicate(JFXTextField tfAlias) {
	tfAlias.textProperty().addListener((observable, oldValue, newValue) -> {
	  SocketUser socketUser = new SocketUser();
	  socketUser.checkAlias(newValue);
	});
  }

  /**
   * Envia una solicitud al servidor para comprobar si un email es duplicado.
   *
   * @param tfEmail
   */
  public void isEmailDuplicate(JFXTextField tfEmail) {
	tfEmail.textProperty().addListener((observable, oldValue, newValue) -> {
	  SocketUser socketUser = new SocketUser();
	  socketUser.checkEmail(newValue);
	});
  }

  /**
   * Verifica si la cadena recibida cumple con el formato de un E-mail.
   *
   * @param emailField
   * @return
   */
  public boolean validateEmailFormat(String emailField) {
	Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	Matcher matcher = pattern.matcher(emailField);
	return matcher.find();
  }

  /**
   * Metodo que esta a la escucha de cualquier evento recibido del servidor.
   */
  public void listenServer() {
	socket.on("registrationSuccesful", (Object... os) -> {
	  Platform.runLater(() -> {
		String intStringRegistrationSuccesful = rb.getString("intStringRegistrationSuccesful");
		Tools.displayConfirmationAlert(intStringRegistrationSuccesful, rb);
        eventLogIn();
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

  /**
   * Muestra un mensaje de error en el textfield recibido y muestra una cruz roja al lado de este.
   *
   * @param message
   * @param textField
   * @param ivRedCross
   */
  public void showTextFieldMessage(String message, JFXTextField textField, ImageView ivRedCross) {
	textField.setFocusColor(Paint.valueOf(FOCUS_COLOR));
	textField.setUnFocusColor(Paint.valueOf(FOCUS_COLOR));
	textField.setPromptText(message);
	textField.setStyle(WRONG_FIELD_STYLE);
	ivRedCross.setVisible(true);
  }

  /**
   * Resetea la apariencia del textfield a su imagen original.
   *
   * @param message
   * @param textField
   * @param ivRedCross
   */
  public void resetTextFieldMessage(String message, JFXTextField textField, ImageView ivRedCross) {
	textField.setFocusColor(Paint.valueOf(PASSWORD_FIELD_FOCUS));
	textField.setUnFocusColor(Paint.valueOf("#17a589"));
	textField.setPromptText(message);
	textField.setStyle(DEFAULT_PASSWORD_STYLE);
	ivRedCross.setVisible(false);
  }

  /**
   * Muestra un mensaje de error en el passwordField recibido y muestra una cruz roja al lado de
   * este.
   *
   * @param message
   * @param passwordField
   * @param ivRedCross
   */
  public void showPasswordMessage(String message, JFXPasswordField passwordField, ImageView ivRedCross) {
	passwordField.setFocusColor(Paint.valueOf(FOCUS_COLOR));
	passwordField.setUnFocusColor(Paint.valueOf(FOCUS_COLOR));
	passwordField.setPromptText(message);
	passwordField.setStyle(WRONG_FIELD_STYLE);
	ivRedCross.setVisible(true);
  }

  /**
   * Resetea la apariencia del passwordField a su imagen original.
   *
   * @param message
   * @param passwordField
   * @param ivRedCross
   */
  public void resetPasswordMessage(String message, JFXPasswordField passwordField, ImageView ivRedCross) {
	passwordField.setFocusColor(Paint.valueOf(PASSWORD_FIELD_FOCUS));
	passwordField.setUnFocusColor(Paint.valueOf("#17a589"));
	passwordField.setPromptText(message);
	passwordField.setStyle(DEFAULT_PASSWORD_STYLE);
	ivRedCross.setVisible(false);
  }
}

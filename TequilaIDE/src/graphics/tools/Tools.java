package graphics.tools;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.xml.bind.DatatypeConverter;
import logic.domain.File;
import logic.domain.Project;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class Tools {

  private static final String BUTTONACEPT = "buttonAccept"; 
  
  private Tools() {
  
  }
  
  /**
   * Muestra una alerta de tipo advertencia
   * @param message
   * @param rb 
   */
  public static void displayWarningAlert(String message, ResourceBundle rb) {
    String intStringWarningTitle = rb.getString("intStringWarningTitle");
    String intStringAccept = rb.getString(BUTTONACEPT);
    
    ButtonType btAccept = new ButtonType(intStringAccept, ButtonBar.ButtonData.OK_DONE);

    Alert alert = new Alert(Alert.AlertType.WARNING, message, btAccept);
    alert.setTitle(intStringWarningTitle);
    alert.setHeaderText(null);
    alert.show();
  }
  
  /**
   * Muestra una alerta de tipo advertencia con los botones Aceptar y Cancelar
   * @param message
   * @param rb
   * @return 
   */
  public static boolean displayWarningAlertWithChoice(String message, ResourceBundle rb) {
	boolean choice = false;
	String intStringWarningTitle = rb.getString("intStringWarningTitle");
	String intStringAccept = rb.getString(BUTTONACEPT);
	String intStringCancel = rb.getString("buttonCancel");

	ButtonType btAccept = new ButtonType(intStringAccept, ButtonBar.ButtonData.OK_DONE);
	ButtonType btCancel = new ButtonType(intStringCancel, ButtonBar.ButtonData.CANCEL_CLOSE);

	Alert alert = new Alert(Alert.AlertType.WARNING, message, btAccept, btCancel);
	alert.setTitle(intStringWarningTitle);
	alert.setHeaderText(null);

	Optional<ButtonType> result = alert.showAndWait();
	if (result.isPresent() && result.get() == btAccept) {
	  choice = true;
	}
	return choice;
  }

  /**
   * Muestra una alerta del tipo Confirmación
   * @param message
   * @param rb 
   */
  public static void displayConfirmationAlert(String message, ResourceBundle rb) {
    String intStringConfirmTitle = rb.getString("intStringConfirmationTitle");
    String intStringAccept = rb.getString(BUTTONACEPT);
    
    ButtonType btAccept = new ButtonType(intStringAccept, ButtonBar.ButtonData.OK_DONE);
    
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, btAccept);
    alert.setTitle(intStringConfirmTitle);
    alert.setHeaderText(null);
    alert.show();
  }
  
  /**
   * Muestra una alerta personalizada 
   * @param title
   * @param message 
   */
  public static void displayInformation(String title, String message) {
    Stage primaryStage = new Stage();
    StackPane stackPane = new StackPane();
    stackPane.setStyle("-fx-background-color: #0F1F38;");
    JFXDialogLayout content = new JFXDialogLayout();
    content.setHeading(new Text(title));
    content.setBody(new Text(message));

    JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

    JFXButton button = new JFXButton("Okay");
    button.setOnAction((ActionEvent event) -> {
      dialog.close();
      primaryStage.close();
    });
    content.setActions(button);

    Scene scene = new Scene(stackPane);

    primaryStage.setScene(scene);
    primaryStage.initStyle(StageStyle.TRANSPARENT);
    dialog.show();
    primaryStage.show();
  }
  
  /**
   * Muestra una alerta con la opcion de ingresar información
   * @param title
   * @param content
   * @param pattern
   * @return 
   */
  public static String displayTextInputDialog(String title, String content, String pattern, ResourceBundle rb) {
	TextInputDialog dialog = new TextInputDialog("");
	dialog.setTitle(title);
	dialog.setContentText(content);
	dialog.setHeaderText(null);
	
	String intStringAccept = rb.getString(BUTTONACEPT);
	String intStringCancel = rb.getString("buttonCancel");

	ButtonType btAccept = new ButtonType(intStringAccept, ButtonBar.ButtonData.OK_DONE);
	ButtonType btCancel = new ButtonType(intStringCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
	dialog.getDialogPane().getButtonTypes().clear();
    dialog.getDialogPane().getButtonTypes().addAll(btAccept, btCancel);
	
	Pattern patternFileName = Pattern.compile(pattern);
	TextFormatter formatoFileName = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
	  return patternFileName.matcher(change.getControlNewText()).matches() ? change : null;
	});

	dialog.getEditor().setTextFormatter(formatoFileName);

	Optional<String> result = dialog.showAndWait();
	if (result.isPresent()) {
	  if (!result.get().trim().isEmpty()) {
		return result.get();
	  } else{
		return "";
	  }
	} else {
	  return ""; 
	}
  }
 
  /**
   * Muestra una alerta del tipo choiceDialog, con diferentes opciones para elegir.
   * @param title
   * @param content
   * @param choices
   * @return 
   */
  public static String displayChoiceDialog(String title, String content, List choices) {
	ChoiceDialog<File> dialog = new ChoiceDialog<>(choices.get(0), choices);
	dialog.setTitle(title);
	dialog.setContentText(content);

	Optional<File> result = dialog.showAndWait();
	if (result.isPresent()) {
	  return result.get().getNombre();
	} else {
	  return ""; 
	}
  }

  /**
   * Regresa un password hasheado del estilo SHA-256.
   * @param password
   * @return 
   */
  public static String getHashedPassword(String password) {
    String result = null;

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes("UTF-8"));
      return bytesToHex(hash); 
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
	  return result;
    }
  }

  private static String bytesToHex(byte[] hash) {
    return DatatypeConverter.printHexBinary(hash);
  }
  
  /**
   * Método que regresa el proyecto cuyo nombre coincida con el parametro de entrada
   * @param name
   * @param projectList
   * @return
   */
  public static Project searchProjectByName(String name, List<Project> projectList) {

    Project proyectoAuxiliar;
    Project selectedProject = null;
    for (int i = 0; i < projectList.size(); i++) {
      proyectoAuxiliar = projectList.get(i);
      if (proyectoAuxiliar.getNombre().equals(name)) {
        selectedProject = proyectoAuxiliar;
      }
    }
    return selectedProject;
  }
  
  /**
   * Aplica una expresión regular a una cadena recibida y regresa el resultado en forma de booleano.
   * @param field
   * @param regex
   * @return 
   */
  public static boolean applyRegularExpression(String field, String regex) {
	boolean flag;
	Pattern p = Pattern.compile(regex);
	Matcher m = p.matcher(field);

	flag = m.find();
	return flag;
  }
  
  /**
   * Verifica que la longitud de la cadena recibida este entre los valores minimo y maximo.
   * @param field
   * @param minimo
   * @param maximo
   * @return 
   */
  public static boolean checkLenght(String field, int minimo, int maximo){
    boolean isLenghtOk = false;
    int lenght = field.length();
    if (lenght <= maximo && lenght >= minimo) {
      isLenghtOk = true;
    } 
    return isLenghtOk;
  }
  
  public static void showNotification(String title, String text, Pos pos) {
	Notifications notification;
	notification = Notifications.create().title(title).text(text).hideAfter(Duration.seconds(1)).position(pos);
	notification.show();
  }
 
}

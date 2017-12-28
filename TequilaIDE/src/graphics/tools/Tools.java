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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.bind.DatatypeConverter;
import logic.domain.File;
import logic.domain.Project;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class Tools {

  public static void displayWarningAlert(String message, ResourceBundle rb) {
    String intStringWarningTitle = rb.getString("intStringWarningTitle");
    String intStringAccept = rb.getString("buttonAccept");
    
    ButtonType btAccept = new ButtonType(intStringAccept, ButtonBar.ButtonData.OK_DONE);

    Alert alert = new Alert(Alert.AlertType.WARNING, message, btAccept);
    alert.setTitle(intStringWarningTitle);
    alert.setHeaderText(null);
    alert.show();
  }

  public static void displayConfirmationAlert(String message, ResourceBundle rb) {
    String intStringConfirmTitle = rb.getString("intStringConfirmationTitle");
    String intStringAccept = rb.getString("buttonAccept");
    
    ButtonType btAccept = new ButtonType(intStringAccept, ButtonBar.ButtonData.OK_DONE);
    
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, btAccept);
    alert.setTitle(intStringConfirmTitle);
    alert.setHeaderText(null);
    alert.show();
  }
  
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
  
  public static String displayTextInputDialog(String title, String content) {
	TextInputDialog dialog = new TextInputDialog("");
	dialog.setTitle(title);
	dialog.setContentText(content);

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

  public static String getHashedPassword(String password) {
    String result = null;

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes("UTF-8"));
      return bytesToHex(hash); 
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
    }
    return result;
  }

  private static String bytesToHex(byte[] hash) {
    return DatatypeConverter.printHexBinary(hash);
  }
  
  /**
   * Método que regresa el proyecto cuyo nombre coincida con el parametro de entrada
   * @param name
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
  
  public static boolean applyRegularExpression(String field, String regex){
    boolean flag;
    Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(field);
      
      flag = m.find();
      return flag;
  }
  
  public static boolean checkLenght(String field, int minimo, int maximo){
    boolean isLenghtOk = false;
    int lenght = field.length();
    if (lenght <= maximo && lenght >= minimo) {
      isLenghtOk = true;
    } 
    return isLenghtOk;
  }
 
}

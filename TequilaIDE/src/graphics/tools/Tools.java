package graphics.tools;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.xml.bind.DatatypeConverter;
import logic.domain.File;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class Tools {

  public static void displayWarningAlert(String title, String message) {
    StackPane stackPane = new StackPane();
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.show();
  }

  public static void displayConfirmationAlert(String title, String message) {
    StackPane stackPane = new StackPane();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
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
    button.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        dialog.close();
        primaryStage.close();
      }
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
      return bytesToHex(hash); // make it printable
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
    }
    return result;
  }

  private static String bytesToHex(byte[] hash) {
    return DatatypeConverter.printHexBinary(hash);
  }
 
}

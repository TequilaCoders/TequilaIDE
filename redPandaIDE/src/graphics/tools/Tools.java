package graphics.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javax.xml.bind.DatatypeConverter;

/**
 * 
 * @author Miguel Alejandro Cámara Árciga
 */
public class Tools {
  
  public static void displayWarningAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.show();
  }

  public static void displayConfirmationAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.show();
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
	ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
	dialog.setTitle(title);
	dialog.setContentText(content);

	Optional<String> result = dialog.showAndWait();
	if (result.isPresent()) {
	  return result.get();
	} else {
	  return ""; 
	}
  }
}

package graphics.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.Alert;
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
}

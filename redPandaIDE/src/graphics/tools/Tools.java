package graphics.tools;

import javafx.scene.control.Alert;

/**
 * 
 * @author Miguel Alejandro Cámara Árciga
 */
public class Tools {
  
  /**Metodo que recibe un String como titulo de la alerta y un String como contenido de esta, y
   * regresa dicha alerta.
   *
   * @param title
   * @param message
   */
  public static void displayAlert(String title, String message) {
    //agregar ventana emergente---------------------------------------
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.show();
    //fin ventana emergente--------------------------------------------
  }
}

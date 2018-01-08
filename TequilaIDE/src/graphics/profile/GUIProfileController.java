package graphics.profile;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logic.domain.User;
import logic.sockets.SocketUser;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class GUIProfileController implements Initializable {

  @FXML
  private JFXButton buttonAlias;
  @FXML
  private JFXButton buttonEmail;
  @FXML
  private TextArea taBiography;
  @FXML
  private Button buttonAcept;
  @FXML
  private Button buttonCancel;
  
  private User user; 

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	buttonAlias.setText(user.getAlias());
	buttonEmail.setText(user.getCorreo());
	taBiography.setText(user.getBiografia());
	
	if (user.getBiografia() != null) {
	  taBiography.setOnKeyReleased(event -> {
		String newBio = taBiography.getText();
		if (newBio.equals(user.getBiografia())) {
		  buttonAcept.setDisable(true);
		} else {
		  buttonAcept.setDisable(false);
		}
	  });
	} else {
	  buttonAcept.setDisable(false);
	}
  }
  
  /**
   * Carga la ventana GUIProfileController.
   * @param rb
   * @param loader 
   */
  public void openBiography(ResourceBundle rb, FXMLLoader loader){
	Stage stagePrincipal = new Stage();
	try {
	  Parent root = (Parent) loader.load();
	  Scene scene = new Scene(root);
	  stagePrincipal.setScene(scene);
	  stagePrincipal.show();
	} catch (IOException ex) {
	  Logger.getLogger(GUIProfileController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  /**
   * Invoca un método de la capa lógica que permite actualizar la biografía del usuario actual. 
   * @param event 
   */
  @FXML
  void updateBiography(ActionEvent event) {
	String newBio = taBiography.getText();
	int userId = user.getIdUsuario();
	
	SocketUser socketUser = new SocketUser();
	socketUser.updateBiography(userId, newBio);

	user.setBiografia(taBiography.getText());
	Stage stage = (Stage) buttonAcept.getScene().getWindow();
	stage.close();
  }

  /**
   * Cierra la ventana
   * @param event 
   */
  @FXML
  void cancel(ActionEvent event) {
	Stage stage = (Stage) buttonAcept.getScene().getWindow();
	stage.close();
  }
}

package graphics.profile;

import com.jfoenix.controls.JFXButton;
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
import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class GUIBiographyController implements Initializable {

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
  private ResourceBundle rb; 

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	this.rb = rb; 
	buttonAlias.setText(user.getAlias());
	buttonEmail.setText(user.getCorreo());
	taBiography.setText(user.getBiografia());
	
	taBiography.setOnKeyReleased(event -> {
	  String newBio = taBiography.getText();
	  if (newBio.equals(user.getBiografia())) {
		buttonAcept.setDisable(true);
	  } else {
		buttonAcept.setDisable(false);
	  }
	});
  }  
  
  public void openBiography(ResourceBundle rb, FXMLLoader loader){
	Stage stagePrincipal = new Stage();
	try {
	  Parent root = (Parent) loader.load();
	  Scene scene = new Scene(root);
	  stagePrincipal.setScene(scene);
	  stagePrincipal.show();
	} catch (Exception ex) {
	  Logger.getLogger(GUIBiographyController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  @FXML
  void updateBiography(ActionEvent event) {
	String newBio = taBiography.getText();

	JSONObject userToSend = new JSONObject();
	userToSend.accumulate("userId", user.getIdUsuario());
	userToSend.accumulate("biography", newBio);
	socket.emit("updateBiography", userToSend);

	user.setBiografia(taBiography.getText());
	Stage stage = (Stage) buttonAcept.getScene().getWindow();
	stage.close();
  }

  @FXML
  void cancel(ActionEvent event) {
	Stage stage = (Stage) buttonAcept.getScene().getWindow();
	stage.close();
  }
}

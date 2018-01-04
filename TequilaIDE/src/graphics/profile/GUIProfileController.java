package graphics.profile;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import graphics.editor.GUIRunProjectController;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import logic.domain.User;

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
  private JFXButton buttonBiography;
  @FXML
  private JFXComboBox<?> cbTheme;
  @FXML
  private JFXSlider sliderScale;
  
  private User user; 
  boolean emailDuplicated = false;
  boolean aliasDuplicated = false;
  private ResourceBundle rb; 
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	this.rb = rb; 
	buttonAlias.setText(user.getAlias());
	buttonEmail.setText(user.getCorreo());
	buttonBiography.setText(user.getBiografia());
	
	buttonAlias.setOnAction(event -> {
	  openEditAlias();
	});
  }  
  
  public void openProfile(ResourceBundle rb, FXMLLoader loader){
	Stage stagePrincipal = new Stage();
	try {
	  Parent root = (Parent) loader.load();
	  Scene scene = new Scene(root);
	  stagePrincipal.setScene(scene);
	  stagePrincipal.show();
	} catch (Exception ex) {
	  Logger.getLogger(GUIRunProjectController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }
  
  public void openEditAlias(){
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/profile/GUIEditAlias.fxml"), rb);
	GUIEditAliasController controller = new GUIEditAliasController();
	loader.setController(controller);
	controller.setAlias(user.getAlias());
	controller.openEditAlias(rb, loader);
  }
  
  public void setUser(User user) {
    this.user = user;
  }
}

package graphics.profile;

import com.jfoenix.controls.JFXTextField;
import graphics.editor.GUIRunProjectController;
import graphics.login.IU_SignUpController;
import graphics.tools.Tools;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import logic.sockets.SocketUser;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class GUIEditAliasController implements Initializable {

  @FXML
  private JFXTextField tfAlias;
  @FXML
  private ImageView aliasRedCross;
  @FXML
  private Button buttonAcept;
  @FXML
  private Button buttonCancel;
  
  private IU_SignUpController signController; 
  private String alias; 
  private boolean aliasDuplicated;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	signController = new IU_SignUpController();
	tfAlias.setText(alias);
	tfAlias.setOnKeyPressed(event ->{
	  checkAlias(alias, rb, tfAlias, aliasRedCross);
	});
	socket.on("aliasDuplicated", (Object... os) -> {
      Platform.runLater(() -> {
        if ((boolean) os[0]) {
          aliasDuplicated = true;
          String intStringAliasDuplicated = rb.getString("intStringAliasDuplicated");
          showTextFieldMessage(intStringAliasDuplicated, tfAlias, aliasRedCross);
        } else {
          aliasDuplicated = false;
        }
      }
      );
    });
  }  

  public void setAlias(String alias) {
	this.alias = alias;
  }
  
  public void openEditAlias(ResourceBundle rb, FXMLLoader loader){
	Stage stagePrincipal = new Stage();
	try {
	  Parent root = (Parent) loader.load();
	  Scene scene = new Scene(root);
	  stagePrincipal.setScene(scene);
	  stagePrincipal.show();
	} catch (Exception ex) {
	  Logger.getLogger(GUIEditAliasController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }
  
  public boolean checkAlias(String alias, ResourceBundle rb, JFXTextField tfAlias, ImageView imAliasRedCross) {
    boolean aliasIsReady = false;
    boolean hasAliasSpecialCharacters = Tools.applyRegularExpression(alias, "[^A-Za-z0-9]");
    if (hasAliasSpecialCharacters) {
      String intStringAliasWithSimbols = rb.getString("intStringAliasWithSimbols");
      showTextFieldMessage(intStringAliasWithSimbols, tfAlias, imAliasRedCross);
    }

    boolean isLenghtOk = Tools.checkLenght(alias, 2, 15);
    if (!isLenghtOk) {
      String intStringAliasLenghtWrong = rb.getString("intStringAliasLenghtWrong");
      showTextFieldMessage(intStringAliasLenghtWrong, tfAlias, imAliasRedCross);
    }

    isAliasDuplicate(tfAlias);

    if (!aliasDuplicated && !hasAliasSpecialCharacters && isLenghtOk) {
      String intStringPromtAlias = rb.getString("promptAlias");
      resetTextFieldMessage(intStringPromtAlias, tfAlias, imAliasRedCross);
      aliasIsReady = true;
    }
    return aliasIsReady;
  }
  
  public void showTextFieldMessage(String message, JFXTextField textField, ImageView ivRedCross) {
    textField.setFocusColor(Paint.valueOf("orange"));
    textField.setUnFocusColor(Paint.valueOf("orange"));
    textField.setPromptText(message);
    textField.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
    ivRedCross.setVisible(true);
  }

  public void resetTextFieldMessage(String message, JFXTextField textField, ImageView ivRedCross) {
    textField.setFocusColor(Paint.valueOf("#77d2ff"));
    textField.setUnFocusColor(Paint.valueOf("#17a589"));
    textField.setPromptText(message);
    textField.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
    ivRedCross.setVisible(false);

  }
  
  public void isAliasDuplicate(JFXTextField tfAlias) {
    tfAlias.textProperty().addListener((observable, oldValue, newValue) -> {
      SocketUser socketUser = new SocketUser();
      socketUser.checkAlias(newValue);
    });
  }
  
  
}

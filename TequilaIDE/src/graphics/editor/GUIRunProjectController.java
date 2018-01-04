/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.editor;

import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.domain.File;
import logic.domain.Project;
import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class GUIRunProjectController implements Initializable {

  @FXML
  private ComboBox<File> cbMainClass;
  @FXML
  private JFXCheckBox checkRequiereArgs;
  @FXML
  private TextField tfArguments;
  @FXML
  private Button buttonRunProgram;
  
  private Project projectToRun; 
  private ResourceBundle rb; 
  private IU_EditorController parent; 
  private List<File> fileList;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	this.rb = rb; 
	cbMainClass.getItems().addAll(fileList);
	cbMainClass.getSelectionModel().selectFirst();
	
	checkRequiereArgs.setOnAction(event ->{
	  if (checkRequiereArgs.isSelected()) {
		tfArguments.setDisable(false);
	  } else {
		tfArguments.setDisable(true);
	  }
	});
  }  
  
  public void setProjectToRun(Project projectToRun) {
	this.projectToRun = projectToRun;
  }

  public void setParent(IU_EditorController parent) {
	this.parent = parent;
  }

  public void setFileList(List<File> fileList) {
	this.fileList = fileList;
  }
  
  public void openRunProject(Stage mainStage, ResourceBundle rb, FXMLLoader loader){
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
  
  @FXML
  public void runProject() {
	String mainClass = cbMainClass.getSelectionModel().getSelectedItem().getNombre();
	String arguments = tfArguments.getText(); 
	
	JSONObject projectToSend = new JSONObject();
	projectToSend.accumulate("projectID", projectToRun.getIdProyecto());
	projectToSend.accumulate("language", projectToRun.getLenguaje());
	projectToSend.accumulate("mainClass", mainClass);
	projectToSend.accumulate("arguments", arguments);
	socket.emit("runProgram", projectToSend);

	parent.openConsole();
	Stage stage = (Stage) cbMainClass.getScene().getWindow();
	stage.close();
  }
  
}

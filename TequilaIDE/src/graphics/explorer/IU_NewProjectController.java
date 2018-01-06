package graphics.explorer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.sockets.SocketProject;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_NewProjectController implements Initializable {

  @FXML
  private Pane paneNewProject;

  @FXML
  private JFXComboBox cbLanguages;

  @FXML
  private JFXButton buttonCancel;

  @FXML
  private JFXTextField tfName;
  
  private boolean estatusDeGuardado;
  
  private int idProject;
  private int userId; 
  private String projectName; 
  private String projectType; 

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    estatusDeGuardado = false;
    loadComboBoxProgramingLanguages();
    listenServer();
  }

  void setUserId(int idUsuario) {
    this.userId = idUsuario;
  }

  public String getProjectName() {
    return projectName;
  }

  public String getProjectType() {
	return projectType;
  }

  public boolean getEstatusDeGuardado() {
    return estatusDeGuardado;
  }
  
  public int getIdProject(){
    return idProject;
  }

  /**
   * Metodo llamado al accionar el elemento buttonCancel
   * @param event 
   */
  @FXML
  void closeWindow(ActionEvent event) {
    Stage stage = (Stage) buttonCancel.getScene().getWindow();
    stage.close();
  }

  /**
   * Método que carga el elemento cbLanguages con los lenguajes de programación del IDE.
   */
  public void loadComboBoxProgramingLanguages() {
    String[] programingLanguagesList = {"Java", "Python", "C++"};
    ObservableList<String> programingLanguages = FXCollections.observableArrayList(programingLanguagesList);
    cbLanguages.setItems(programingLanguages);
  }

  /**
   * Toma los valores de los campos tfName y cbLanguages para registrar un proyecto.
   * @param event 
   */
  @FXML
  void createProject(ActionEvent event) {
    estatusDeGuardado = true;
    String name;
    String programmingLanguage = "";
	
    name = tfName.getText();
	
	switch((String) cbLanguages.getSelectionModel().getSelectedItem()){
	  case "Java":
		programmingLanguage = "java";
		break;
	  case "C++":
		programmingLanguage = "cpp";
		break;
	  case "Python":
		programmingLanguage = "py";
		break; 	
	}
    
	projectName = name; 
	projectType = programmingLanguage;
	
    SocketProject socketProject = new SocketProject(); 
	socketProject.createNewProject(name, programmingLanguage, userId);
  }
  
  /**
   * Metodo que esta a la escucha de cualquier evento recibido del servidor.
   */
  public void listenServer(){
	socket.on("projectSaved", (Object... os) -> {
      if ((boolean) os[0]) {
        idProject = (int) os[1];
        Platform.runLater(() -> {
          Stage stage = (Stage) paneNewProject.getScene().getWindow();
          stage.close();
        });
      } else {
        System.out.println((String) os[1]);
      }
    });
  }
}

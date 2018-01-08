package graphics.explorer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import graphics.tools.Tools;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import logic.domain.Project;
import logic.sockets.SocketProject;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class GUINewProjectController implements Initializable {

  @FXML
  private Pane paneNewProject;

  @FXML
  private JFXComboBox cbLanguages;

  @FXML
  private JFXButton buttonCancel;

  @FXML
  private JFXButton buttonCreate;

  @FXML
  private JFXTextField tfName;

  private boolean estatusDeGuardado;

  private int idProject;
  private int userId;
  private String projectName;
  private String projectType;
  private Paint cbUnFocusColor;
  private List<Project> myProjectsList;
  private ResourceBundle rb;

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	this.rb = rb;
	estatusDeGuardado = false;
	cbUnFocusColor = cbLanguages.getUnFocusColor();

	tfName.setOnKeyReleased(event -> {
	  validateProjectName();
	});
	
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
   * Cierra la ventana
   * @param event 
   */
  @FXML
  void closeWindow(ActionEvent event) {
    Stage stage = (Stage) buttonCancel.getScene().getWindow();
    stage.close();
  }

  /**
   * Carga el elemento cbLanguages con los lenguajes de programación disponibles en el IDE.
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
	if (cbLanguages.getSelectionModel().getSelectedItem() == null) {
	  cbLanguages.setUnFocusColor(Paint.valueOf("orange"));
	} else {
	  estatusDeGuardado = true;
	  String name = tfName.getText();
	  String programmingLanguage;

	  switch ((String) cbLanguages.getSelectionModel().getSelectedItem()) {
		case "Java":
		  programmingLanguage = "java";
		  break;
		case "C++":
		  programmingLanguage = "cpp";
		  break;
		default:
		  programmingLanguage = "py";
		  break;
	  }

	  projectName = name;
	  projectType = programmingLanguage;

	  SocketProject socketProject = new SocketProject();
	  socketProject.createNewProject(name, programmingLanguage, userId);
	}

  }
  
  /**
   * Se mantiene a la escucha de ciertos eventos emitidos por el servidor.
   */
  private void listenServer() {
	socket.on("projectSaved", (Object... os) -> {
	  idProject = (int) os[1];
	  Platform.runLater(() -> {
		Stage stage = (Stage) paneNewProject.getScene().getWindow();
		stage.close();
	  });
	});
  }

  void setProjectList(List<Project> myProjectsList) {
	this.myProjectsList = myProjectsList;
  }
  
  /**
   * Verifica que el nombre de proyecto recibido existe en una lista de objetos tipo proyecto dada.
   * @param projectName
   * @param projectsList
   * @return 
   */
  public boolean projectExist(String projectName, List<Project> projectsList){
	int projectsSize = projectsList.size();
	for (int i = 0; i < projectsSize; i++) {
	  if (projectName.equals(projectsList.get(i).getNombre())) {
		return true; 
	  }
	}
	return false; 
  }
  
  /**
   * Valida que el nombre del proyecto cumpla con un patrón especifico.
   */
  public void validateProjectName() {
	cbLanguages.setUnFocusColor(cbUnFocusColor);
	if (tfName.getText().isEmpty()) {
	  buttonCreate.setDisable(true);
	} else {
	  if (projectExist(tfName.getText().trim(), myProjectsList)) {
		String promptName = rb.getString("intStringProjectNameExist");
		tfName.setPromptText(promptName);
		buttonCreate.setDisable(true);
	  } else {
		if (Tools.applyRegularExpression(tfName.getText(), "[^A-Za-z0-9_]")) {
		  String promptName = rb.getString("intStringProjectNameWithSimbols");
		  tfName.setPromptText(promptName);
		} else {
		  String promptName = rb.getString("promptName");
		  tfName.setPromptText(promptName);
		  buttonCreate.setDisable(false);
		}
	  }
	}
  }
 
}

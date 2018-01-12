package graphics.explorer;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import graphics.editor.GUIEditorController;
import graphics.login.GUILogInController;
import graphics.profile.GUIProfileController;
import graphics.tools.Tools;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import logic.domain.Project;
import logic.domain.User;
import logic.sockets.SocketProject;
import org.json.JSONArray;
import tequilaide.TequilaIDE;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class GUIFileExplorerController implements Initializable {

  @FXML
  private AnchorPane anchorPaneMain;

  @FXML
  private JFXNodesList nodeListProfile;

  @FXML
  private JFXButton buttonProfile;

  @FXML
  ImageView imageVUserImage;

  @FXML
  private JFXButton buttonLogOut;

  @FXML
  private JFXButton buttonProfileOptions;

  @FXML
  private JFXButton buttonProjects;

  @FXML
  private JFXButton buttonSharedProjects;

  @FXML
  private JFXButton buttonAllProjects;

  @FXML
  private FlowPane flowPaneProjects;

  @FXML
  private Pane paneNewProject;

  @FXML
  private ImageView imageVNewProject;

  @FXML
  private JFXTextField tfSearchProject;

  AnchorPane anchorPaneNewProject;

  boolean estatusDeGuardado;

  private ResourceBundle rb;

  Stage fileExplorerStage;

  FlowPane fpProjects;

  User user;

  List<Project> myProjectsList = new ArrayList<>();
  List<Project> sharedProjectsList = new ArrayList<>();
  List<Project> allProjectsList = new ArrayList<>();
  
  private static final String BACKGROUND_COLOR = "-fx-background-color:white;"; 
  private static final String BUTTONS_BACKGROUND = "-fx-background-color:#A1D6E2;";

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	listenServer();
	this.rb = rb;
	SocketProject socketProject = new SocketProject();

	socketProject.loadProjects(user.getIdUsuario());
	socketProject.loadSharedProjects(user.getIdUsuario());

    

    listeners();
    loadProfileMenuButtons();
    setProfileButton();
    
    searchProject();
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setProfileButton() {
    buttonProfile.setText(user.getAlias());
  }

  /**
   * Cierra la ventana actual y abre la ventana IU_LogIn
   * @param event 
   */
  @FXML
  void logOut(ActionEvent event) {
	Stage mainStage = (Stage) anchorPaneMain.getScene().getWindow();
	GUILogInController logIn = new GUILogInController();
	logIn.openLogIn(mainStage, rb);
  }

  /**
   * Abre la ventana IU_AddCollaborator
   * @param event 
   */
  @FXML
  void addNewProject(MouseEvent event) {
	imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyectoClic.png"));
	FXMLLoader loader = new FXMLLoader(getClass().getResource("GUINewProject.fxml"), rb);
	Stage iuCreateNewProject;
	iuCreateNewProject = windowNewProjectDataInput(loader);
	iuCreateNewProject.show();
	listenerWindowNewProjectClosed(loader, iuCreateNewProject);
  }

  /**
   * Muestra todos los proyectos recibidos del servidor
   * @param event 
   */
  @FXML
  void allProjectsSelected(ActionEvent event) {
    createIcons(allProjectsList);
  }

  /**
   * Muestra todos los proyectos que ha creado el usuario
   * @param event 
   */
  @FXML
  void myProjectsSelected(ActionEvent event) {
    createIcons(myProjectsList);
  }

  /**
   * Muestra todos los proyectos en los que el usuario es colaborador
   * @param event 
   */
  @FXML
  void sharedProjectsSelected(ActionEvent event) {
    createIcons(sharedProjectsList);
  }

  /**
   * Cambia la apariencia del boton cuando se posiciona el mouse en el boton
   * @param event
   */
  @FXML
  void buttonAllProjectsMouseEntered(MouseEvent event) {
	buttonAllProjects.setStyle(BACKGROUND_COLOR);
	buttonAllProjects.setPrefWidth(200);
  }

  /**
   * Cambia la apariencia del boton cuando el mouse sale del area del botón.
   * @param event
   */
  @FXML
  void buttonAllProjectsMouseExited(MouseEvent event) {
	buttonAllProjects.setStyle(BUTTONS_BACKGROUND);
	buttonAllProjects.setPrefWidth(185);
  }

  /**
   * Cambia la apariencia del boton cuando se posiciona el mouse en el boton
   * @param event 
   */
  @FXML
  void buttonMyProjectsMouseEntered(MouseEvent event) {
	buttonProjects.setStyle(BACKGROUND_COLOR);
	buttonProjects.setPrefWidth(200);
  }

  /**
   * Cambia la apariencia del boton cuando el mouse sale del area del botón.
   * @param event
   */
  @FXML
  void buttonMyProjectsMouseExited(MouseEvent event) {
	buttonProjects.setStyle(BUTTONS_BACKGROUND);
	buttonProjects.setPrefWidth(185);
  }

  /**
   * Cambia la apariencia del boton cuando se posiciona el mouse en el boton
   * @param event
   */
  @FXML
  void buttonSharedProjectsMouseEntered(MouseEvent event) {
	buttonSharedProjects.setStyle(BACKGROUND_COLOR);
	buttonSharedProjects.setPrefWidth(200);
  }

  /**
   * Cambia la apariencia del boton cuando el mouse sale del area del botón.
   * @param event
   */
  @FXML
  void buttonSharedProjectsMouseExited(MouseEvent event) {
	buttonSharedProjects.setStyle(BUTTONS_BACKGROUND);
	buttonSharedProjects.setPrefWidth(185);
  }

  /**
   * Cambia la apariencia del boton cuando se posiciona el mouse en el boton
   * @param event
   */
  @FXML
  void buttonLogOutMouseEntered(MouseEvent event) {
	buttonLogOut.setStyle(BACKGROUND_COLOR);
	buttonLogOut.setPrefHeight(36);
	buttonLogOut.setMaxHeight(36);
	buttonLogOut.setMinHeight(36);
  }

  /**
   * Cambia la apariencia del boton cuando el mouse sale del area del botón.
   * @param event
   */
  @FXML
  void buttonLogOutMouseExited(MouseEvent event) {
	buttonLogOut.setStyle(BUTTONS_BACKGROUND);
	buttonLogOut.setPrefHeight(32);
	buttonLogOut.setMaxHeight(32);
	buttonLogOut.setMinHeight(32);
  }

  /**
   * Cambia la apariencia del boton cuando se posiciona el mouse en el boton
   * @param event
   */
  @FXML
  void buttonProfileOptionsMouseEntered(MouseEvent event) {
	buttonProfileOptions.setStyle(BACKGROUND_COLOR);
	buttonProfileOptions.setPrefHeight(36);
	buttonProfileOptions.setMaxHeight(36);
	buttonProfileOptions.setMinHeight(36);
  }

  /**
   * Cambia la apariencia del boton cuando el mouse sale del area del botón.
   * @param event
   */
  @FXML
  void buttonProfileOptionsMouseExited(MouseEvent event) {
	buttonProfileOptions.setStyle(BUTTONS_BACKGROUND);
	buttonProfileOptions.setPrefHeight(32);
	buttonProfileOptions.setMaxHeight(32);
	buttonProfileOptions.setMinHeight(32);
  }

  /**
   * Listener en el elemento tfSearchProject que al encontrar un proyecto que coincida con el criterio
   * ingresado lo muestra en el flowPaneProjects.
   */
  public void searchProject() {
	tfSearchProject.textProperty().addListener((observable, oldValue, newValue) -> {
	  for (int i = 0; i < allProjectsList.size(); i++) {
		if (allProjectsList.get(i).getNombre().equalsIgnoreCase(newValue.toLowerCase())) {
		  List<Project> aux = new ArrayList<>();
		  Project matchingProject = allProjectsList.get(i);
		  aux.add(matchingProject);
		  createIcons(aux);
		  break;
		} else {
		  createIcons(myProjectsList);
		}
	  }
	});
  }

  /**
   * Método que agrega los elementos buttonProfile,buttonProfileOptions,buttonSetUp y buttonHelp a
   * un NodeList.
   */
  private void loadProfileMenuButtons() {
    nodeListProfile.addAnimatedNode(buttonProfile);
    nodeListProfile.addAnimatedNode(buttonProfileOptions);
	nodeListProfile.addAnimatedNode(buttonLogOut);
	nodeListProfile.setSpacing(15);
  }

  /**
   * listeners de elementos graficos de la ventana que solo utilizan expresiones lambda.
   */
  private void listeners() {
	buttonProfile.setOnMouseEntered((e -> imageVUserImage.setImage(new Image("/resources/icons/Male_User_Filled_White.png"))));
	buttonProfile.setOnMouseExited((e -> imageVUserImage.setImage(new Image("/resources/icons/Male_User_Filled.png"))));
	paneNewProject.setOnMouseEntered((e -> imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyectoSeleccionado.png"))));
	paneNewProject.setOnMouseExited((e -> imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyecto.png"))));
	buttonProjects.setOnAction((e -> createIcons(myProjectsList)));
  }

  /**
   * Metodo que crea un nuevo escenario para ingresar los datos del nuevo proyecto
   *
   * @param crearProyecto
   * @return
   */
  public Stage windowNewProjectDataInput(FXMLLoader crearProyecto) {
	GUINewProjectController controller = new GUINewProjectController();
	crearProyecto.setController(controller);
	try {
	  anchorPaneNewProject = crearProyecto.load();
	} catch (IOException ex) {
	  Logger.getLogger(GUIFileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
	}
	controller.setUserId(user.getIdUsuario());
	controller.setProjectList(myProjectsList);
	Stage iuCrearNuevoProyecto = new Stage();

	iuCrearNuevoProyecto.initOwner(anchorPaneMain.getScene().getWindow());
	Scene escena = new Scene(anchorPaneNewProject);
	iuCrearNuevoProyecto.initStyle(StageStyle.UNDECORATED);
	iuCrearNuevoProyecto.setScene(escena);

	return iuCrearNuevoProyecto;
  }

  /**
   * Metodo que esta a la escucha del evento de cierre de la ventana IU_NewProject, cuando se active
   * se recuperara el estatusGuardado para saber si un proyecto fue creado o no en dicha ventana.
   *
   * @param loader
   * @param iuCreateNewProject
   */
  public void listenerWindowNewProjectClosed(FXMLLoader loader, Stage iuCreateNewProject) {
	iuCreateNewProject.setOnHiding(new EventHandler<WindowEvent>() {
	  int idProject;

	  @Override
	  public void handle(WindowEvent we) {
		estatusDeGuardado = loader.<GUINewProjectController>getController().getEstatusDeGuardado();
		if (estatusDeGuardado) {
		  idProject = loader.<GUINewProjectController>getController().getIdProject();
		  String nameProject = loader.<GUINewProjectController>getController().getProjectName();
		  String language = loader.<GUINewProjectController>getController().getProjectType();

		  Project justCreatedProyect = new Project();
		  justCreatedProyect.setIdProyecto(idProject);
		  justCreatedProyect.setNombre(nameProject);
		  justCreatedProyect.setLenguaje(language);
		  fileExplorerStage = (Stage) anchorPaneMain.getScene().getWindow();
		  GUIEditorController controllerObject = new GUIEditorController();
		  controllerObject.openEditor(justCreatedProyect, fileExplorerStage, rb, user);
		}
	  }
	});
  }

  /**
   * Metodo que abre la ventana IU_FileExplorer.fxml
   *
   * @param fileExplorerStage
   * @param rb
   * @param user
   */
  public void openFileExplorer(Stage fileExplorerStage, ResourceBundle rb, User user) {
    AnchorPane rootPane;
    Stage stagePrincipal = new Stage();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/explorer/GUIFileExplorer.fxml"), rb);
    GUIFileExplorerController controller = new GUIFileExplorerController();
    controller.setUser(user);
    loader.setController(controller);

    try {
      rootPane = loader.load();

      Scene scene = new Scene(rootPane);

      stagePrincipal.setScene(scene);
      stagePrincipal.setMaximized(true);
      stagePrincipal.show();
      
      fileExplorerStage.close();
    } catch (IOException ex) {
      Logger.getLogger(TequilaIDE.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Metodo que esta a la escucha de cualquier evento recibido del servidor.
   */
  public void listenServer() {
	socket.on("projectsRecovered", (Object... os) -> {
	  projectsRecovered(os[1]);
	}).on("sharedProjectsRecovered", (Object... os) -> {
	  sharedProjectsRecovered(os[1]);
	}).on("biographySaved", (Object... os) -> {
	  Platform.runLater(() -> {
		Tools.displayInformation(rb.getString("confirmBiographyUpdated"), rb.getString("biographyUpdated"));
	  });
	}).on("collaborationInvitation", (Object... os) -> {
	  Platform.runLater(() -> {
		String title = rb.getString("invitation");
		Tools.showNotification(title, (String) os[0] +" "+ rb.getString("collaborationInvitation"), Pos.TOP_RIGHT);
		SocketProject socketProject = new SocketProject();
		socketProject.loadSharedProjects(user.getIdUsuario());
	  });
	});
  }

  /**
   * Recibe un booleano y un objeto, si el booleano es verdadero convierte el objeto a una lista de
   * proyectos y los asigna a la variable myProjectList
   * @param lista 
   */
  public void projectsRecovered(Object lista) {
	JSONArray receivedList = (JSONArray) lista;
	String jsonString = receivedList.toString();

	Gson gson = new Gson();

	Project[] jsonProjectList = gson.fromJson(jsonString, Project[].class);
	myProjectsList = Arrays.asList(jsonProjectList);
	allProjectsList.addAll(myProjectsList);
	Platform.runLater(() -> {
	  createIcons(myProjectsList);
	});
  }

  /**
   * Recibe un booleano y un objeto, si el booleano es verdadero convierte el objeto a una lista de
   * proyectos y los asigna a la variable sharedProjectList
   * @param receivedObject 
   */
  public void sharedProjectsRecovered(Object receivedObject) {
	JSONArray receivedList = (JSONArray) receivedObject;
	String jsonString = receivedList.toString();

	Gson gson = new Gson();

	Project[] jsonProjectList = gson.fromJson(jsonString, Project[].class);
	sharedProjectsList = Arrays.asList(jsonProjectList);
	sharedProjectsList = markProjectsAsShared(sharedProjectsList);
	allProjectsList.addAll(sharedProjectsList);
  }

  /**
   * Marca el atributo "shared" de cada elemento de la lista recibida como true.
   * @param projectList
   * @return
   */
  private List<Project> markProjectsAsShared(List<Project> projectList) {
	for (int i = 0; i < projectList.size(); i++) {
	  Project aux = projectList.get(i);
	  aux.setShared(true);
	}
	return projectList;
  }
  
  /**
   * Método que esta a la escucha de los eventos de entrada y salida del mouse sobre los pane
   * creados por cada prooyecto, es aquí donde se agrega el efecto de cambio de color sobre los
   * iconos de los pane.
   * @param projectPanes
   * @param projectList
   */
  public void hoverListeners(List<Pane> projectPanes, List<Project> projectList) {
    for (int i = 0; i < projectPanes.size(); i++) {
      Button button1 = (Button) projectPanes.get(i).getChildren().get(0);
      if (!projectList.get(i).isShared()) {
        ImageView im1 = (ImageView) button1.getGraphic();
        projectPanes.get(i).setOnMouseEntered((e -> im1.setImage(new Image("/resources/icons/proyecto_seleccionado.png"))));
        
        ImageView im2 = (ImageView) button1.getGraphic();
        projectPanes.get(i).setOnMouseExited((e -> im2.setImage(new Image("resources/icons/proyecto.png"))));
      } else {
        ImageView im1 = (ImageView) button1.getGraphic();
        projectPanes.get(i).setOnMouseEntered((e -> im1.setImage(new Image("/resources/icons/shared_project_yellow.png"))));
        ImageView im2 = (ImageView) button1.getGraphic();
        projectPanes.get(i).setOnMouseExited((e -> im2.setImage(new Image("resources/icons/shared_project.png"))));
      }
    }
  }

  /**
   * Método que esta a la escucha de eventos sobre los projectPanes.
   * @param projectPanes
   * @param projectList
   */
  public void projectSelectedAction(List<Pane> projectPanes, List<Project> projectList) {
    for (int i = 0; i < projectPanes.size(); i++) {

      Label name = (Label) projectPanes.get(i).getChildren().get(1);
      Button button = (Button) projectPanes.get(i).getChildren().get(0);
      
      button.setOnAction((ActionEvent e) -> {
        Project selectedProject = Tools.searchProjectByName(name.getText(), projectList);
        openEditorWindow(selectedProject);
      });
    }
  }

  /**
   * Crea iconos para cada elemento de la lista recibida como entrada
   * @param projectList
   */
  public void createIcons(List<Project> projectList){
    ArrayList<Pane> projectPanes = new ArrayList<>();
    flowPaneProjects.setHgap(7);
    for (int i = 0; i < projectList.size(); i++) {
      Pane pane = new Pane();
      pane.setPrefHeight(90);
      pane.setPrefWidth(90);
      Button button = new Button();

      ImageView imagev = new ImageView();
      button.setStyle("-fx-background-color: transparent;");
      button.setGraphic(imagev);
      pane.getChildren().add(button);
      
      if (!projectList.get(i).isShared()) {
        imagev.setImage(new Image("/resources/icons/proyecto.png"));
      } else {
        imagev.setImage(new Image("/resources/icons/shared_project.png"));
      }
  
      imagev.setFitWidth(58);
      imagev.setFitHeight(58);

      Label projectTitle = new Label();
      projectTitle.setText(projectList.get(i).getNombre());
      projectTitle.setTextFill(Paint.valueOf("white"));
      projectTitle.setLayoutY(72);

      pane.getChildren().add(projectTitle);

      projectPanes.add(pane);
    }
    flowPaneProjects.getChildren().clear();
    flowPaneProjects.getChildren().addAll(projectPanes);
    
    hoverListeners(projectPanes, projectList);
    projectSelectedAction(projectPanes, projectList);
  }
  
  /**
   * Abre la ventana IU_Editor.fxml
   * @param selectedProject
   */
  public void openEditorWindow(Project selectedProject) {
    fileExplorerStage = (Stage) anchorPaneMain.getScene().getWindow();
    GUIEditorController controllerObject = new GUIEditorController();
    controllerObject.openEditor(selectedProject, fileExplorerStage, rb, user);
  }  

  /**
   * Abre la ventana GUIBiography
   * @param event 
   */
  @FXML
  void editProfile(ActionEvent event) {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/profile/GUIProfile.fxml"), rb);
	GUIProfileController controller = new GUIProfileController();
	loader.setController(controller);
	controller.setUser(user);
	controller.openBiography(rb, loader);
  }
}

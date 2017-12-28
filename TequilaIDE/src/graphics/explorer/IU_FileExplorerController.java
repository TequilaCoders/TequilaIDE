package graphics.explorer;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import graphics.editor.IU_EditorController;
import graphics.login.IU_LogInController;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import logic.domain.Project;
import logic.domain.User;
import logic.sockets.SocketProject;
import org.json.JSONArray;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FileExplorerController implements Initializable {

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
  private JFXButton buttonSetUp;

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
  

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
    SocketProject socketProject = new SocketProject();
    
    socketProject.loadProjects(user.getIdUsuario());
    socketProject.loadSharedProjects(user.getIdUsuario());
    
    listenServer();
    searchProject();

    Platform.runLater(() -> {
      createFlowPaneMyProjects(myProjectsList);

      listeners();
      loadProfileMenuButtons();
      setProfileButton();
    });

  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setProfileButton() {
    buttonProfile.setText(user.getAlias());
  }

  @FXML
  void logOut(ActionEvent event) {
    Stage mainStage = (Stage) anchorPaneMain.getScene().getWindow();;
    IU_LogInController logIn = new IU_LogInController();
    logIn.openLogIn(mainStage, rb);
  }

  /**
   * Cuando le dan clic a crear nuevo proyecto, o sea, no tiene sentido el nombre de este método :L
   *
   * @param event
   */
  @FXML
  void projectSelected(MouseEvent event) {
    imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyectoClic.png"));
    FXMLLoader loader = new FXMLLoader(getClass().getResource("IU_NewProject.fxml"), rb);
    Stage iuCreateNewProject;
    iuCreateNewProject = windowNewProjectDataInput(loader);
    iuCreateNewProject.show();
    listenerWindowNewProjectClosed(loader, iuCreateNewProject);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void myProjectsSelected(ActionEvent event) {
    loadFlowPaneMyProjects();
  }

  @FXML
  void sharedProjectsSelected(ActionEvent event) {
    createFlowPaneSharedProjects(sharedProjectsList);
  }

  /**
   *
   * @param event 
   */
  @FXML
  void buttonAllProjectsMouseEntered(MouseEvent event) {
    buttonAllProjects.setStyle("-fx-background-color:white;");
    buttonAllProjects.setPrefWidth(200);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonAllProjectsMouseExited(MouseEvent event) {
    buttonAllProjects.setStyle("-fx-background-color:#A1D6E2;");
    buttonAllProjects.setPrefWidth(185);
  }
  
  @FXML
  void buttonMyProjectsMouseEntered(MouseEvent event) {
    buttonProjects.setStyle("-fx-background-color:white;");
    buttonProjects.setPrefWidth(200);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonMyProjectsMouseExited(MouseEvent event) {
    buttonProjects.setStyle("-fx-background-color:#A1D6E2;");
    buttonProjects.setPrefWidth(185);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonSharedProjectsMouseEntered(MouseEvent event) {
    buttonSharedProjects.setStyle("-fx-background-color:white;");
    buttonSharedProjects.setPrefWidth(200);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonSharedProjectsMouseExited(MouseEvent event) {
    buttonSharedProjects.setStyle("-fx-background-color:#A1D6E2;");
    buttonSharedProjects.setPrefWidth(185);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonLogOutMouseEntered(MouseEvent event) {
    buttonLogOut.setStyle("-fx-background-color:white;");
    buttonLogOut.setPrefHeight(36);
    buttonLogOut.setMaxHeight(36);
    buttonLogOut.setMinHeight(36);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonLogOutMouseExited(MouseEvent event) {
    buttonLogOut.setStyle("-fx-background-color:#A1D6E2;");
    buttonLogOut.setPrefHeight(32);
    buttonLogOut.setMaxHeight(32);
    buttonLogOut.setMinHeight(32);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonProfileOptionsMouseEntered(MouseEvent event) {
    buttonProfileOptions.setStyle("-fx-background-color:white;");
    buttonProfileOptions.setPrefHeight(36);
    buttonProfileOptions.setMaxHeight(36);
    buttonProfileOptions.setMinHeight(36);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonProfileOptionsMouseExited(MouseEvent event) {
    buttonProfileOptions.setStyle("-fx-background-color:#A1D6E2;");
    buttonProfileOptions.setPrefHeight(32);
    buttonProfileOptions.setMaxHeight(32);
    buttonProfileOptions.setMinHeight(32);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonSetUpMouseEntered(MouseEvent event) {
    buttonSetUp.setStyle("-fx-background-color:white;");
    buttonSetUp.setPrefHeight(36);
    buttonSetUp.setMaxHeight(36);
    buttonSetUp.setMinHeight(36);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonSetUpMouseExited(MouseEvent event) {
    buttonSetUp.setStyle("-fx-background-color:#A1D6E2;");
    buttonSetUp.setPrefHeight(32);
    buttonSetUp.setMaxHeight(32);
    buttonSetUp.setMinHeight(32);
  }


    public void searchProject() {
      
      tfSearchProject.textProperty().addListener((observable, oldValue, newValue) -> {
        boolean flag = false;
        for (int i = 0; i < myProjectsList.size(); i++) {
          if (myProjectsList.get(i).getNombre().toLowerCase().equals(newValue.toLowerCase())) {
            List<Project> aux = new ArrayList<>();
            aux.add(myProjectsList.get(i));
            createFlowPaneMyProjects(aux);
            flag = true;
          } else {
            createFlowPaneMyProjects(myProjectsList);
            flag = false;
          }
        }
        
        if (!flag) {
          for (int i = 0; i < sharedProjectsList.size(); i++) {
          if (sharedProjectsList.get(i).getNombre().toLowerCase().equals(newValue.toLowerCase())) {
            List<Project> aux = new ArrayList<>();
            aux.add(sharedProjectsList.get(i));
            createFlowPaneSharedProjects(aux);
          } else {
            createFlowPaneMyProjects(myProjectsList);
          }
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
    nodeListProfile.addAnimatedNode(buttonSetUp);
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
    buttonProjects.setOnAction((e-> createFlowPaneMyProjects(myProjectsList)));
  }

  /**
   * Metodo que crea un nuevo escenario para ingresar los datos del nuevo proyecto
   * @param crearProyecto
   * @return 
   */
  public Stage windowNewProjectDataInput(FXMLLoader crearProyecto) {
	IU_NewProjectController controller = new IU_NewProjectController();
	crearProyecto.setController(controller);
    try {
      anchorPaneNewProject = crearProyecto.load();
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }
	controller.setUserId(user.getIdUsuario());
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
   * @param loader
   * @param iuCreateNewProject
   */
  public void listenerWindowNewProjectClosed(FXMLLoader loader, Stage iuCreateNewProject){
    iuCreateNewProject.setOnHiding(new EventHandler<WindowEvent>() {
      int idProject;
      @Override
      public void handle(WindowEvent we) {
        estatusDeGuardado = loader.<IU_NewProjectController>getController().getEstatusDeGuardado();
        if (estatusDeGuardado) {
          idProject = loader.<IU_NewProjectController>getController().getIdProject();
		  String nameProject = loader.<IU_NewProjectController>getController().getProjectName();
          String language = loader.<IU_NewProjectController>getController().getProjectType();
		  
		  Project justCreatedProyect = new Project();
          justCreatedProyect.setIdProyecto(idProject);
		  justCreatedProyect.setNombre(nameProject);
		  justCreatedProyect.setLenguaje(language);
          fileExplorerStage = (Stage) anchorPaneMain.getScene().getWindow();
          IU_EditorController controllerObject = new IU_EditorController();
          controllerObject.open_Editor(justCreatedProyect, fileExplorerStage, rb, user);
        }
      }
    });
  }
  
  /**
   * Reemplaza el FXMLoader del FlowPane actual por IU_FlowPaneMyProjects.fxml
   * @param myProjectsList
   * @return 
   */
  public FlowPane createFlowPaneMyProjects(List<Project> myProjectsList){
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/explorer/IU_FlowPaneMyProjects.fxml"), rb);
      
      IU_FlowPaneMyProjectsController controller = new IU_FlowPaneMyProjectsController();
      controller.setUser(user);
      controller.setProjectList(myProjectsList);
      loader.setController(controller);

      FlowPane newFlowPane = loader.load();
      fpProjects = newFlowPane;
      flowPaneProjects.getChildren().setAll(fpProjects);
     
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    flowPaneProjects.setVisible(true);
    paneNewProject.setVisible(true);
    
    return flowPaneProjects;
  }
  
  
  public FlowPane createFlowPaneSharedProjects(List<Project> sharedProjectsList){
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/explorer/IU_FlowPaneSharedProjects.fxml"), rb);
      
      IU_FlowPaneSharedProjectsController controller = new IU_FlowPaneSharedProjectsController();
      controller.setUser(user);
      controller.setProjectList(sharedProjectsList);
      loader.setController(controller);

      FlowPane newFlowPane = loader.load();
      fpProjects = newFlowPane;
      flowPaneProjects.getChildren().setAll(fpProjects);
     
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    flowPaneProjects.setVisible(true);
    
    return flowPaneProjects;
  }
  
  public void loadFlowPaneMyProjects(){
    
    flowPaneProjects.getChildren().setAll(fpProjects);
    
    flowPaneProjects.getChildren().add(0, paneNewProject);
    
    flowPaneProjects.setVisible(true);
    paneNewProject.setVisible(true);
  }
  
  /**
   * Metodo que abre la ventana IU_FileExplorer.fxml
   * @param fileExplorerStage
   * @param rb 
   * @param user 
   */
  public void openFileExplorer(Stage fileExplorerStage, ResourceBundle rb, User user){
	try {
	  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/explorer/IU_FileExplorer.fxml"), rb);
	  IU_FileExplorerController controller = new IU_FileExplorerController();
	  controller.setUser(user);
	  fxmlLoader.setController(controller);
	  Parent root1 = (Parent) fxmlLoader.load();
      Scene scene = new Scene(root1);
	  
	  fileExplorerStage.setMaximized(false);
      
	  fileExplorerStage.setScene(scene);
	  
	  fileExplorerStage.show();
      fileExplorerStage.setMaximized(true);
	} catch (IOException ex) {
	  Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }
  
  public void listenServer(){
	socket.on("projectsRecovered", (Object... os) -> {
      projectsRecovered((boolean) os[0], os[1]);
    }).on("sharedProjectsRecovered", (Object... os) -> {
      sharedProjectsRecovered((boolean) os[0], os[1]);
    });
  }
  
  public void projectsRecovered(boolean projectRecovered, Object lista) {
	if (projectRecovered) {
	  JSONArray receivedList = (JSONArray) lista;
	  String jsonString = receivedList.toString();

	  Gson gson = new Gson();

	  Project[] jsonProjectList = gson.fromJson(jsonString, Project[].class);
	  myProjectsList = Arrays.asList(jsonProjectList);
	} else {
	  System.out.println((String) lista);
	}
  }
  
  public void sharedProjectsRecovered(boolean projectRecovered, Object receivedObject) {
	if (projectRecovered) {
	  JSONArray receivedList = (JSONArray) receivedObject;
      String jsonString = receivedList.toString();
      
      Gson gson = new Gson();
      
      Project[] jsonProjectList = gson.fromJson(jsonString, Project[].class);
      sharedProjectsList = Arrays.asList(jsonProjectList);
      sharedProjectsList = markProjectsAsShared(sharedProjectsList);
	} else {
	  System.out.println((String) receivedObject);
	}
  }
  
  /**
   * Marca el atributo "shared" de cada elemento de la lista como true 
   * @param projectList
   * @return 
   */
  public List<Project> markProjectsAsShared(List<Project> projectList){
    for (int i = 0; i < projectList.size(); i++) {
      Project aux = projectList.get(i);
      aux.setShared(true);
    }
    return projectList;
  }
  
}

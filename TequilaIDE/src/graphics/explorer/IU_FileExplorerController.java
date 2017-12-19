package graphics.explorer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import graphics.editor.IU_EditorController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
  private JFXButton buttonHelp;

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
  private FlowPane flowPaneProyectos;

  @FXML
  private Pane paneNewProject;

  @FXML
  private ImageView imageVNewProject;
  
  AnchorPane anchorPaneNewProject;

  boolean estatusDeGuardado;

  private ResourceBundle rb;
  
  Stage fileExplorerStage;
  
  FlowPane fpProjects;
  
  User user;
  
  List<Project> projectList = new ArrayList<>();
  

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
    create_FlowPaneMyProjects();
    
    listeners();
    loadProfileMenu_Buttons();

  }
  
  public void setProjectList(List<Project> projectList) {
    this.projectList = projectList;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  /**
   * Cuando le dan clic a crear nuevo proyecto, o sea, no tiene sentido el nombre de este método :L
   * @param event 
   */
  @FXML
  void projectSelected(MouseEvent event) {
    imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyectoClic.png"));
    FXMLLoader loader = new FXMLLoader(getClass().getResource("IU_NewProject.fxml"));
    Stage iuCreate_NewProject;
    iuCreate_NewProject = window_NewProject_DataInput(loader);
    iuCreate_NewProject.show();
    listener_WindowNewProject_Closed(loader, iuCreate_NewProject);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void myProjectsSelected(ActionEvent event) {
    load_FlowPaneMyProjects();
  }

  @FXML
  void sharedProjectsSelected(ActionEvent event) {
    create_FlowPaneSharedProjects();
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
  void buttonHelpMouseEntered(MouseEvent event) {
    buttonHelp.setStyle("-fx-background-color:white;");
    buttonHelp.setPrefHeight(36);
    buttonHelp.setMaxHeight(36);
    buttonHelp.setMinHeight(36);
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void buttonHelpMouseExited(MouseEvent event) {
    buttonHelp.setStyle("-fx-background-color:#A1D6E2;");
    buttonHelp.setPrefHeight(32);
    buttonHelp.setMaxHeight(32);
    buttonHelp.setMinHeight(32);
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

  /**
   * Método que agrega los elementos buttonProfile,buttonProfileOptions,buttonSetUp y buttonHelp
   * a un NodeList.
   */
  private void loadProfileMenu_Buttons() {
    nodeListProfile.addAnimatedNode(buttonProfile);
    nodeListProfile.addAnimatedNode(buttonProfileOptions);
    nodeListProfile.addAnimatedNode(buttonSetUp);
    nodeListProfile.addAnimatedNode(buttonHelp);
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
    buttonProjects.setOnAction((e-> create_FlowPaneMyProjects()));
  }

  /**
   * Metodo que crea un nuevo escenario para ingresar los datos del nuevo proyecto
   * @param crearProyecto
   * @return 
   */
  public Stage window_NewProject_DataInput(FXMLLoader crearProyecto) {
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
   * @param iuCreate_NewProject 
   */
  public void listener_WindowNewProject_Closed(FXMLLoader loader, Stage iuCreate_NewProject){
    iuCreate_NewProject.setOnHiding(new EventHandler<WindowEvent>() {
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
   * @return 
   */
  public FlowPane create_FlowPaneMyProjects(){
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/explorer/IU_FlowPaneMyProjects.fxml"), rb);
      
      IU_FlowPaneMyProjectsController controller = new IU_FlowPaneMyProjectsController();
      controller.setUser(user);
      loader.setController(controller);

      FlowPane newFlowPane = loader.load();
      fpProjects = newFlowPane;
      flowPaneProyectos.getChildren().setAll(fpProjects);
     
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    flowPaneProyectos.setVisible(true);
    paneNewProject.setVisible(true);
    
    return flowPaneProyectos;
  }
  
  public FlowPane create_FlowPaneSharedProjects(){
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/explorer/IU_FlowPaneSharedProjects.fxml"), rb);
      
      IU_FlowPaneSharedProjectsController controller = new IU_FlowPaneSharedProjectsController();
      controller.setUser(user);
      loader.setController(controller);

      FlowPane newFlowPane = loader.load();
      fpProjects = newFlowPane;
      flowPaneProyectos.getChildren().setAll(fpProjects);
     
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    flowPaneProyectos.setVisible(true);
    
    return flowPaneProyectos;
  }
  
  public void load_FlowPaneMyProjects(){
    
    flowPaneProyectos.getChildren().setAll(fpProjects);
    
    flowPaneProyectos.getChildren().add(0, paneNewProject);
    
    flowPaneProyectos.setVisible(true);
    paneNewProject.setVisible(true);
  }
  
  /**
   * Metodo que abre la ventana IU_FileExplorer.fxml
   * @param fileExplorerStage
   * @param rb 
   */
  public void open_FileExplorer(Stage fileExplorerStage, ResourceBundle rb, User user){
	try {
	  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/explorer/IU_FileExplorer.fxml"), rb);
	  IU_FileExplorerController controller = new IU_FileExplorerController();
	  controller.setUser(user);
	  fxmlLoader.setController(controller);
	  Parent root1 = (Parent) fxmlLoader.load();
	  
	  fileExplorerStage.setMaximized(false);
	  fileExplorerStage.setScene(new Scene(root1));
	  
	  System.out.println("cargando explorador");
	  fileExplorerStage.show();
	  fileExplorerStage.setMaximized(true);
	} catch (IOException ex) {
	  Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }
}

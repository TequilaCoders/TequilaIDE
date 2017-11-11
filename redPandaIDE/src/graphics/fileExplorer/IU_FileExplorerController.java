/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.fileExplorer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import graphics.textEditor.IU_EditorController;
import java.io.IOException;
import java.net.URL;
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
  
  Stage fileExplorerStage ;//= (Stage) anchorPaneMain.getScene().getWindow();;
  

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    listeners();
    loadProfileMenu_Buttons();
    this.rb = rb;
    create_FlowPaneMyProjects();
    //setStage();
    //getStage();
  }

  //Al seleccionar crear un nuevo proyecto------------------------------------
  @FXML
  void projectSelected(MouseEvent event) {
    imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyectoClic.png"));

    //se crea el cargador del nuevo componente
    FXMLLoader loader = new FXMLLoader(getClass().getResource("IU_NewProject.fxml"));
    //se crea un nuevo Stage con el fxml IU_NuevoProyecto y se muestra
    Stage iuCreate_NewProject;
    iuCreate_NewProject = window_NewProject_DataInput(loader);
    iuCreate_NewProject.show();

    listener_WindowNewProject_Closed(loader, iuCreate_NewProject);
  }

  @FXML
  void myProjectsSelected(ActionEvent event) {
    create_FlowPaneMyProjects();
  }

  @FXML
  void buttonAllProjectsMouseEntered(MouseEvent event) {
    buttonAllProjects.setStyle("-fx-background-color:white;");
    buttonAllProjects.setPrefWidth(200);
  }

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

  @FXML
  void buttonMyProjectsMouseExited(MouseEvent event) {
    buttonProjects.setStyle("-fx-background-color:#A1D6E2;");
    buttonProjects.setPrefWidth(185);
  }

  @FXML
  void buttonSharedProjectsMouseEntered(MouseEvent event) {
    buttonSharedProjects.setStyle("-fx-background-color:white;");
    buttonSharedProjects.setPrefWidth(200);
  }

  @FXML
  void buttonSharedProjectsMouseExited(MouseEvent event) {
    buttonSharedProjects.setStyle("-fx-background-color:#A1D6E2;");
    buttonSharedProjects.setPrefWidth(185);
  }

  @FXML
  void buttonHelpMouseEntered(MouseEvent event) {
    buttonHelp.setStyle("-fx-background-color:white;");
    buttonHelp.setPrefHeight(36);
    buttonHelp.setMaxHeight(36);
    buttonHelp.setMinHeight(36);
  }

  @FXML
  void buttonHelpMouseExited(MouseEvent event) {
    buttonHelp.setStyle("-fx-background-color:#A1D6E2;");
    buttonHelp.setPrefHeight(32);
    buttonHelp.setMaxHeight(32);
    buttonHelp.setMinHeight(32);
  }

  @FXML
  void buttonProfileOptionsMouseEntered(MouseEvent event) {
    buttonProfileOptions.setStyle("-fx-background-color:white;");
    buttonProfileOptions.setPrefHeight(36);
    buttonProfileOptions.setMaxHeight(36);
    buttonProfileOptions.setMinHeight(36);
  }

  @FXML
  void buttonProfileOptionsMouseExited(MouseEvent event) {
    buttonProfileOptions.setStyle("-fx-background-color:#A1D6E2;");
    buttonProfileOptions.setPrefHeight(32);
    buttonProfileOptions.setMaxHeight(32);
    buttonProfileOptions.setMinHeight(32);
  }

  @FXML
  void buttonSetUpMouseEntered(MouseEvent event) {
    buttonSetUp.setStyle("-fx-background-color:white;");
    buttonSetUp.setPrefHeight(36);
    buttonSetUp.setMaxHeight(36);
    buttonSetUp.setMinHeight(36);
  }

  @FXML
  void buttonSetUpMouseExited(MouseEvent event) {
    buttonSetUp.setStyle("-fx-background-color:#A1D6E2;");
    buttonSetUp.setPrefHeight(32);
    buttonSetUp.setMaxHeight(32);
    buttonSetUp.setMinHeight(32);
  }

  public void setStage(){
    fileExplorerStage = (Stage) anchorPaneMain.getScene().getWindow();
  }
  
  public FlowPane getFlowPaneProyectos() {
    return flowPaneProyectos;
  }

  public void setFlowPaneProyectos(FlowPane flowPaneProyectos) {
    this.flowPaneProyectos = flowPaneProyectos;
  }

  public void loadProfileMenu_Buttons() {
    nodeListProfile.addAnimatedNode(buttonProfile);
    nodeListProfile.addAnimatedNode(buttonProfileOptions);
    nodeListProfile.addAnimatedNode(buttonSetUp);
    nodeListProfile.addAnimatedNode(buttonHelp);

    nodeListProfile.setSpacing(15);
  }

  public void listeners() {

    buttonProfile.setOnMouseEntered((e -> imageVUserImage.setImage(new Image("/resources/icons/Male_User_Filled_White.png"))));
    buttonProfile.setOnMouseExited((e -> imageVUserImage.setImage(new Image("/resources/icons/Male_User_Filled.png"))));

    paneNewProject.setOnMouseEntered((e -> imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyectoSeleccionado.png"))));
    paneNewProject.setOnMouseExited((e -> imageVNewProject.setImage(new Image("/resources/icons/nuevo_proyecto.png"))));
    
    buttonProjects.setOnAction((e-> create_FlowPaneMyProjects()));
  }

  public Stage window_NewProject_DataInput(FXMLLoader crearProyecto) {
    try {
      anchorPaneNewProject = crearProyecto.load();
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }

    Stage iuCrearNuevoProyecto = new Stage();

    iuCrearNuevoProyecto.initOwner(anchorPaneMain.getScene().getWindow());
    Scene escena = new Scene(anchorPaneNewProject);
    iuCrearNuevoProyecto.initStyle(StageStyle.UNDECORATED);
    iuCrearNuevoProyecto.setScene(escena);
    
    return iuCrearNuevoProyecto;
  }
  
  public void listener_WindowNewProject_Closed(FXMLLoader loader, Stage iuCreate_NewProject){
    
    //en el evento de cierre de la IU_NuevoProyecto, se obtendra el atributo estatusDeGaurdar, el cual
    //dira si se guardo un proyecto o no
    iuCreate_NewProject.setOnHiding(new EventHandler<WindowEvent>() {
      int idProject;
      @Override
      public void handle(WindowEvent we) {
        estatusDeGuardado = loader.<IU_NewProjectController>getController().getEstatusDeGuardado();
        //se comprueba si se guardo o no un proyecto para mostrar los archivos de este
        if (estatusDeGuardado) {
          //obtiene el id del proyecto recien creado
          idProject = loader.<IU_NewProjectController>getController().getIdProject();
      
          System.out.println("el id es: "+idProject);
          //se muestra la ventana
          fileExplorerStage = (Stage) anchorPaneMain.getScene().getWindow();
          IU_EditorController controllerObject = new IU_EditorController();
          controllerObject.open_Editor(idProject, fileExplorerStage, rb);
        }
      }
    });
  }
  
  /* A Eliminar
  public void open_TextEditor(int idProject, Stage fileExplorerStage) {
    
    Stage stage = (Stage) anchorPaneMain.getScene().getWindow();
    try { 

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Editor.fxml"), rb);
      
      IU_EditorController controller = new IU_EditorController();
      fxmlLoader.setController(controller);
      System.out.println("abriendo otra ventana, proyecto = "+idProject);
      controller.setIdProject(idProject);
      
      Parent root1 = (Parent) fxmlLoader.load();
      
      //Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }*/
  
  public FlowPane create_FlowPaneMyProjects(){
    try {
      FlowPane crearProyecto = FXMLLoader.load(getClass().getResource("/graphics/fileExplorer/IU_FlowPaneMyProjects.fxml"), rb);
      flowPaneProyectos.getChildren().setAll(crearProyecto);
     
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //paneNuevoProyecto.setVisible(false);
    flowPaneProyectos.getChildren().add(0, paneNewProject);
    
    flowPaneProyectos.setVisible(true);
    paneNewProject.setVisible(true);
    
    return flowPaneProyectos;
  }

}

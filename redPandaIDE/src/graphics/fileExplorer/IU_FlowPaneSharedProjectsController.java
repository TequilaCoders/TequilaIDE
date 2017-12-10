/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.fileExplorer;

import com.google.gson.Gson;
import static graphics.login.IU_LogInController.socket;
import graphics.textEditor.IU_EditorController;
import io.socket.emitter.Emitter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import logic.Project;
import logic.User;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FlowPaneSharedProjectsController implements Initializable {

  @FXML
  private FlowPane flowPaneSharedProjects;
  
  Pane pane;

  ArrayList<Pane> projectPanes = new ArrayList<>();

  private List<Project> projectList = new ArrayList<>();

  User user;
  
  Stage fileExplorerStage;
  
  private ResourceBundle rb;
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
    
    Platform.runLater(new Runnable() {
            @Override
            public void run() {
               loadSharedProjects(user.getIdUsuario());
            }

          });
   
  }  
  
  public void setUser(User user) {
    this.user = user;
  }  
  
  public List<Project> loadSharedProjects(int userId) {
    
    JSONObject userIDToSend = new JSONObject();

    userIDToSend.accumulate("userID", userId);

    socket.connect();
    System.err.println("logrado");

    socket.emit("loadSharedProjects", userIDToSend);

    socket.on("sharedProjectsRecovered", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
          JSONArray receivedList = (JSONArray) os[1];
          String jsonString = receivedList.toString();

          Gson gson = new Gson();

          System.out.println("proyetos recuperados (json) " + jsonString);
          Project[] jsonProjectList = gson.fromJson(jsonString, Project[].class);
          projectList = Arrays.asList(jsonProjectList);
          projectList = markProjectsAsShared(projectList);
          System.out.println("proyetos convertidos (java) " + projectList.get(0).getIdProyecto());
          socket.disconnect();
          
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            System.out.println("lista proyectos " + projectList);
            createIcons(projectList);
            hoverListeners();
            projectSelectedAction();
          }

        });
      }

    });

    return projectList;
  }
  
  public List<Project> markProjectsAsShared(List<Project> projectList){
    for (int i = 0; i < projectList.size(); i++) {
      Project aux = projectList.get(i);
      aux.setShared(true);
    }
    
    return projectList;
  }


  /**
   * Método que crea iconos para cada elementos de la lista recibida como entrada
   *
   * @param listaProyectos
   */
  public void createIcons(List<Project> listaProyectos) {

    flowPaneSharedProjects.setHgap(7);

    for (int i = 0; i < listaProyectos.size(); i++) {

      System.out.println("creando panes");
      pane = new Pane();
      pane.setPrefHeight(90);
      pane.setPrefWidth(90);

      ImageView imagev = new ImageView();
      pane.getChildren().add(imagev);
      imagev.setImage(new Image("/resources/icons/shared_project.png"));
      imagev.setFitWidth(58);
      imagev.setFitHeight(58);

      Label projectTitle = new Label();
      projectTitle.setText(listaProyectos.get(i).getNombre());
      projectTitle.setTextFill(Paint.valueOf("white"));
      projectTitle.setLayoutY(72);

      pane.getChildren().add(projectTitle);

      projectPanes.add(pane);
    }
    flowPaneSharedProjects.getChildren().clear();
    flowPaneSharedProjects.getChildren().addAll(projectPanes);

    System.out.println("iconos creados");
  }
  
  /**
   * Método que esta a la escucha de los eventos de entrada y salida del mouse sobre los pane
   * creados por cada prooyecto, es aquí donde se agrega el efecto de cambio de color sobre los
   * iconos de los pane.
   */
  public void hoverListeners() {
    for (int i = 0; i < projectPanes.size(); i++) {
      ImageView im1 = (ImageView) projectPanes.get(i).getChildren().get(0);
      projectPanes.get(i).setOnMouseEntered((e -> im1.setImage(new Image("/resources/icons/shared_project_yellow.png"))));
      ImageView im2 = (ImageView) projectPanes.get(i).getChildren().get(0);
      projectPanes.get(i).setOnMouseExited((e -> im2.setImage(new Image("resources/icons/shared_project.png"))));
    }
  }

  /**
   * Método que esta a la escucha de eventos sobre los projectPanes.
   */
  public void projectSelectedAction() {
    for (int i = 0; i < projectPanes.size(); i++) {

      ImageView im1 = (ImageView) projectPanes.get(i).getChildren().get(0);
      Label name = (Label) projectPanes.get(i).getChildren().get(1);
      projectPanes.get(i).setOnMouseClicked((new EventHandler<MouseEvent>() {
        ;
        @Override
        public void handle(MouseEvent e) {

          im1.setImage(new Image("/resources/icons/proyecto_clic.png"));
          Project selectedProject = searchProjectByName(name.getText());

          open_EditorWindow(selectedProject);
        }
      }));
    }
  }
  
  /**
   * Método que ventana IU_Editor.fxml
   *
   * @param selectedProject
   */
  public void open_EditorWindow(Project selectedProject) {

    fileExplorerStage = (Stage) flowPaneSharedProjects.getScene().getWindow();
    IU_EditorController controllerObject = new IU_EditorController();
    controllerObject.open_Editor(selectedProject, fileExplorerStage, rb, user);
  }
  
  /**
   * Método que regresa el proyecto cuyo nombre coincida con el parametro de entrada
   *
   * @param name
   * @return
   */
  public Project searchProjectByName(String name) {

    Project proyectoAuxiliar;
    Project selectedProject = null;
    for (int i = 0; i < projectList.size(); i++) {
      proyectoAuxiliar = projectList.get(i);
      if (proyectoAuxiliar.getNombre().equals(name)) {
        selectedProject = proyectoAuxiliar;
        System.out.println("el proyecto encontrado por nombre es " + selectedProject.getIdProyecto());
      }
    }
    return selectedProject;
  }

  
}

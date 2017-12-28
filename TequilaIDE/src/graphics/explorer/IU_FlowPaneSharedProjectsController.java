package graphics.explorer;

import graphics.editor.IU_EditorController;
import graphics.tools.Tools;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import logic.domain.Project;
import logic.domain.User;

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
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;

    createIcons(projectList);
    hoverListeners();
    projectSelectedAction();
  }

  public void setUser(User user) {
    this.user = user;
  }  
  
  public void setProjectList(List<Project> projectList) {
    this.projectList = projectList;
  }
 
  /**
   * Método que crea iconos para cada elementos de la lista recibida como entrada
   *
   * @param listaProyectos
   */
  public void createIcons(List<Project> listaProyectos) {
    flowPaneSharedProjects.setHgap(7);
    for (int i = 0; i < listaProyectos.size(); i++) {

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
          Project selectedProject = Tools.searchProjectByName(name.getText(), projectList);

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
}

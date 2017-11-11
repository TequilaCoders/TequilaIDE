package graphics.fileExplorer;

import entities.Proyecto;
import graphics.textEditor.IU_EditorController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FlowPaneMyProjectsController implements Initializable {

  @FXML
  private FlowPane flowPaneMyProjects;

  Pane pane;

  ArrayList<Pane> projectPanes = new ArrayList<>();

  List<Proyecto> ProjectList = new ArrayList<>();
  
  Stage fileExplorerStage;

  //int idProject;

  private ResourceBundle rb;

  public IU_FlowPaneMyProjectsController() {

  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
    createIcons(loadProjects());
    listeners();
    buttonActions();
  }

  public void listeners() {

    for (int i = 0; i < projectPanes.size(); i++) {
      ImageView im1 = (ImageView) projectPanes.get(i).getChildren().get(0);
      projectPanes.get(i).setOnMouseEntered((e -> im1.setImage(new Image("/resources/icons/proyecto_seleccionado.png"))));
      ImageView im2 = (ImageView) projectPanes.get(i).getChildren().get(0);
      projectPanes.get(i).setOnMouseExited((e -> im2.setImage(new Image("resources/icons/proyecto.png"))));

    }
  }

  public void buttonActions() {
    for (int i = 0; i < projectPanes.size(); i++) {

      ImageView im1 = (ImageView) projectPanes.get(i).getChildren().get(0);
      Label name = (Label) projectPanes.get(i).getChildren().get(1);
      projectPanes.get(i).setOnMouseClicked((new EventHandler<MouseEvent>() {
 ;
        @Override
        public void handle(MouseEvent e) {

          im1.setImage(new Image("/resources/icons/proyecto_clic.png"));
          int idProject = searchProjectID(name.getText());
          
          open_EditorWindow(idProject);
        }
      }));
    }
  }

  public List<Proyecto> loadProjects() {
    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    TypedQuery<Proyecto> query
        = entitymanager.createNamedQuery("Proyecto.findByUsuarioidUsuario", Proyecto.class).setParameter("usuarioidUsuario", 1); //CAMBIAR EL 1 POR EL ID DE USUARIO>>>>
    return ProjectList = query.getResultList();
  }

  public void createIcons(List<Proyecto> listaProyectos) {

    flowPaneMyProjects.setHgap(7);

    for (int i = 0; i < listaProyectos.size(); i++) {

      pane = new Pane();
      pane.setPrefHeight(90);
      pane.setPrefWidth(90);

      ImageView imagev = new ImageView();
      pane.getChildren().add(imagev);
      imagev.setImage(new Image("/resources/icons/proyecto.png"));
      imagev.setFitWidth(58);
      imagev.setFitHeight(58);

      Label projectTitle = new Label();
      projectTitle.setText(listaProyectos.get(i).getNombre());
      projectTitle.setTextFill(Paint.valueOf("white"));
      projectTitle.setLayoutY(72);

      pane.getChildren().add(projectTitle);

      projectPanes.add(pane);
    }
    flowPaneMyProjects.getChildren().addAll(projectPanes);
  }

  public int searchProjectID(String name) {

    Proyecto p;
    int idProject = 0;
    for (int i = 0; i < ProjectList.size(); i++) {
      p = ProjectList.get(i);
      if (p.getNombre().equals(name)) {
        return idProject = p.getProyectoPK().getIdProyecto();
      }
    }
    return idProject;
  }

  public void open_EditorWindow(int idProject) {
    
    fileExplorerStage = (Stage) flowPaneMyProjects.getScene().getWindow();
    IU_EditorController controllerObject = new IU_EditorController();
    controllerObject.open_Editor(idProject, fileExplorerStage, rb);
  }

}

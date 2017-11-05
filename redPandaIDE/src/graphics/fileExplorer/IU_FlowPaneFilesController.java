/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.fileExplorer;

import entities.Archivo;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FlowPaneFilesController implements Initializable {

  @FXML
  private FlowPane flowPaneFiles;

  Pane pane;
  
  ArrayList<Pane> filePanes = new ArrayList<>();
  
  List<Archivo> fileList = new ArrayList<>();
  
  private int idProject = -1;
  
  private ResourceBundle rb;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    createIcons(loadFiles());

    this.rb = rb;
  }
  
  public int getIdProject() {
    return idProject;
  }

  public void setIdProject(int idProject) {
    this.idProject = idProject;
  }

  public List<Archivo> loadFiles() {

    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    TypedQuery<Archivo> query
        = entitymanager.createNamedQuery("Archivo.findByProyectoidProyecto", Archivo.class).setParameter("proyectoidProyecto", idProject); //CAMBIAR EL 1 POR EL ID DE USUARIO>>>>
    return fileList = query.getResultList();
  }

  public void createIcons(List<Archivo> fileList) {

    flowPaneFiles.setHgap(7);
    for (int i = 0; i < fileList.size(); i++) {
      pane = new Pane();
      pane.setPrefHeight(90);
      pane.setPrefWidth(90);

      ImageView imagev = new ImageView();
      pane.getChildren().add(imagev);
      imagev.setImage(new Image("/resources/icons/archivo.png"));
      imagev.setFitWidth(58);
      imagev.setFitHeight(58);

      Label fileTitle = new Label();
      fileTitle.setText(fileList.get(i).getNombre());
      fileTitle.setTextFill(Paint.valueOf("white"));
      fileTitle.setLayoutY(72);

      pane.getChildren().add(fileTitle);

      filePanes.add(pane);
    }
    flowPaneFiles.getChildren().addAll(filePanes);
  }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graficos.explorador;

import entidades.Archivo;
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
public class IU_FlowPaneArchivosController implements Initializable {

  @FXML
  private FlowPane flowPaneArchivos;

  @FXML
  private Pane paneNuevoArchivo;

  @FXML
  private ImageView imageVNuevoArchivo;

  Pane pane;
  
  ArrayList<Pane> panelesArchivos = new ArrayList<>();
  
  private int idProyecto = -1;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("id nuevo"+idProyecto);
    crearIconos(cargarProyectos());
    //listeners();
  }
  
  public int getIdProyecto() {
    return idProyecto;
  }

  public void setIdProyecto(int idProyecto) {
    this.idProyecto = idProyecto;
  }

  public List<Archivo> cargarProyectos() {
    List<Archivo> listaArchivos = new ArrayList<>();

    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    TypedQuery<Archivo> query
        = entitymanager.createNamedQuery("Archivo.findByProyectoidProyecto", Archivo.class).setParameter("proyectoidProyecto", idProyecto); //CAMBIAR EL 1 POR EL ID DE USUARIO>>>>
    return listaArchivos = query.getResultList();
  }

  public void crearIconos(List<Archivo> listaArchivos) {

    flowPaneArchivos.setHgap(7);
    for (int i = 0; i < listaArchivos.size(); i++) {
      pane = new Pane();
      pane.setPrefHeight(90);
      pane.setPrefWidth(90);

      ImageView imagev = new ImageView();
      pane.getChildren().add(imagev);
      imagev.setImage(new Image("/graficos/iconos/archivo.png"));
      imagev.setFitWidth(58);
      imagev.setFitHeight(58);

      Label tituloProyecto = new Label();
      tituloProyecto.setText(listaArchivos.get(i).getNombre());
      tituloProyecto.setTextFill(Paint.valueOf("white"));
      tituloProyecto.setLayoutY(72);

      pane.getChildren().add(tituloProyecto);

      panelesArchivos.add(pane);
    }
    flowPaneArchivos.getChildren().addAll(panelesArchivos);
  }

}

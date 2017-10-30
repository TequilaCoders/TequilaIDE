/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graficos.explorador;

import entidades.Proyecto;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FlowPaneProyectosExistentesController implements Initializable {

  @FXML
  private FlowPane flowPaneProyectosExistentes;

  Pane pane;
  
  ArrayList<Pane> panelesProyectos = new ArrayList<>();
  
  List<Proyecto> listaProyectos = new ArrayList<>();
  
  int idProyecto;
  
  public IU_FlowPaneProyectosExistentesController(){
    
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    crearIconos(cargarProyectos());
    listeners();
    accionBotones();
  }

  public void listeners() {
    
    for (int i = 0; i < panelesProyectos.size(); i++) {
      ImageView im1 = (ImageView) panelesProyectos.get(i).getChildren().get(0);
      panelesProyectos.get(i).setOnMouseEntered((e -> im1.setImage(new Image("/graficos/iconos/proyecto_seleccionado.png"))));
      ImageView im2 = (ImageView) panelesProyectos.get(i).getChildren().get(0);
      panelesProyectos.get(i).setOnMouseExited((e -> im2.setImage(new Image("graficos/iconos/proyecto.png"))));

    }

  }
  
  public void accionBotones(){
    for (int i = 0; i < panelesProyectos.size(); i++) {
      
      ImageView im1 = (ImageView) panelesProyectos.get(i).getChildren().get(0);
      Label nombre = (Label) panelesProyectos.get(i).getChildren().get(1);
      panelesProyectos.get(i).setOnMouseClicked((new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          im1.setImage(new Image("/graficos/iconos/proyecto_clic.png"));
          buscarIDProyecto(nombre.getText());
          cargarFlowPaneArchivos();
        }
      }));
    }
  }
  
  public List<Proyecto> cargarProyectos(){    
    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();
    
    TypedQuery<Proyecto> query
        = entitymanager.createNamedQuery("Proyecto.findByUsuarioidUsuario", Proyecto.class).setParameter("usuarioidUsuario", 1); //CAMBIAR EL 1 POR EL ID DE USUARIO>>>>
    return listaProyectos = query.getResultList();
  }
  
  public void crearIconos(List<Proyecto> listaProyectos){
    
        flowPaneProyectosExistentes.setHgap(7);
        
    for (int i = 0; i < listaProyectos.size(); i++) {
      
      pane = new Pane();
      pane.setPrefHeight(90);
      pane.setPrefWidth(90);
      
      ImageView imagev = new ImageView();
      pane.getChildren().add(imagev);
      imagev.setImage(new Image("/graficos/iconos/proyecto.png"));
      imagev.setFitWidth(58);
      imagev.setFitHeight(58);
      
      Label tituloProyecto = new Label();
      tituloProyecto.setText(listaProyectos.get(i).getNombre());
      tituloProyecto.setTextFill(Paint.valueOf("white"));
      tituloProyecto.setLayoutY(72);
      
      
      pane.getChildren().add(tituloProyecto);
      
      panelesProyectos.add(pane);      
    }
    flowPaneProyectosExistentes.getChildren().addAll(panelesProyectos);
  }
  
  public int buscarIDProyecto(String nombre){

    Proyecto p;
    for (int i = 0; i < listaProyectos.size(); i++) {
      p = listaProyectos.get(i);
      if (p.getNombre().equals(nombre)) {
        return idProyecto = p.getProyectoPK().getIdProyecto();
      }
    }
    return idProyecto;
  }

  public void cargarFlowPaneArchivos() {
    //IU_FlowPaneArchivosController nuevoFlowPane = new IU_FlowPaneArchivosController();

    FXMLLoader crearProyecto = new FXMLLoader(getClass().getResource("/graficos/explorador/IU_FlowPaneArchivos.fxml"));
    
    try {
      
      IU_FlowPaneArchivosController controlador = new IU_FlowPaneArchivosController();
      
      crearProyecto.setController(controlador);
      
      //se le pasa el di del proyecto seleccionado al controlador del flowpane
      controlador.setIdProyecto(idProyecto);
      
      FlowPane nuevo = crearProyecto.load();  
      
      flowPaneProyectosExistentes.getChildren().remove(flowPaneProyectosExistentes);
      flowPaneProyectosExistentes.getChildren().setAll(nuevo);
      
      //IU_FlowPaneArchivosController controlador = crearProyecto.<IU_FlowPaneArchivosController>getController();
      
    } catch (IOException ex) {
      Logger.getLogger(IU_ExploradorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

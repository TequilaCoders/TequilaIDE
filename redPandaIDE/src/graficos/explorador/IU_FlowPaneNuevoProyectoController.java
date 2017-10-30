/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graficos.explorador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FlowPaneNuevoProyectoController implements Initializable {

  @FXML
  private Pane paneNuevoArchivo;

  @FXML
  private ImageView imageVNuevoArchivo;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    listeners();
    accionBotones();
  }  
  
  public void listeners(){
    paneNuevoArchivo.setOnMouseEntered((e -> imageVNuevoArchivo.setImage(new Image("/graficos/iconos/mouse_sobreArchivo.png"))));
    paneNuevoArchivo.setOnMouseExited((e -> imageVNuevoArchivo.setImage(new Image("/graficos/iconos/nuevo_Archivo.png"))));
 
  }
  
  public void accionBotones(){
    paneNuevoArchivo.setOnMouseClicked((e -> crearArchivo()));
  }
  
  public void crearArchivo(){
    //Funcion pendiente //
    //Aqui se carga el editor//
  }
  
}

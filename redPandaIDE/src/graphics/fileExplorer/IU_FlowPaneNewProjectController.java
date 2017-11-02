/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.fileExplorer;

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
public class IU_FlowPaneNewProjectController implements Initializable {

  @FXML
  private Pane paneNewFIle;

  @FXML
  private ImageView imageVNewFile;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    listeners();
    actionButtons();
  }  
  
  
  public void listeners(){
    paneNewFIle.setOnMouseEntered((e -> imageVNewFile.setImage(new Image("/resources/icons/mouse_sobreArchivo.png"))));
    paneNewFIle.setOnMouseExited((e -> imageVNewFile.setImage(new Image("/resources/icons/nuevo_Archivo.png"))));
 
  }
  
  public void actionButtons(){
    paneNewFIle.setOnMouseClicked((e -> createFile()));
  }
  
  public void createFile(){
    //Funcion pendiente //
    //Aqui se carga el editor//
  }
  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.fileExplorer;

import graphics.textEditor.IU_EditorController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FlowPaneNewProjectController implements Initializable {

  @FXML
  private FlowPane fPaneNewProject;

  @FXML
  private Pane paneNewFIle;

  @FXML
  private ImageView imageVNewFile;

  private ResourceBundle rb;
  
  private Stage mainFileExplorerStage;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
    listeners();
  }

  public Stage getMainFileExplorerStage() {
    return mainFileExplorerStage;
  }

  public void setMainFileExplorerStage(Stage mainFileExplorerStage) {
    this.mainFileExplorerStage = mainFileExplorerStage;
  }

  @FXML
  void onPaneNewFileSelected(MouseEvent event) {
    createFile();
  }

  public void listeners(){
    paneNewFIle.setOnMouseEntered((e -> imageVNewFile.setImage(new Image("/resources/icons/mouse_sobreArchivo.png"))));
    paneNewFIle.setOnMouseExited((e -> imageVNewFile.setImage(new Image("/resources/icons/nuevo_Archivo.png"))));
 
  }
  
  public void createFile(){
// get a handle to the stage
    Stage stage = (Stage) fPaneNewProject.getScene().getWindow();
    // do what you have to do
    stage.close();

    try {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Editor.fxml"), rb);
      Parent root1 = (Parent) fxmlLoader.load();
      IU_EditorController controller = new IU_EditorController();
      fxmlLoader.setController(controller);
      
      //Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }

    //Funcion pendiente //
    //Aqui se carga el editor//
  }
  
}

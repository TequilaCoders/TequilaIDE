package graphics.textEditor;

import entities.Archivo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_EditorController implements Initializable {

    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem menuItemNew;
    @FXML
    private Menu menuTools;
    @FXML
    private MenuItem menuItemOptions;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuItemAbout;
    @FXML
    private TabPane tabPaneArchivos;
    
    private ArrayList<Archivo> fileList;
    
    private int idProject;
    
    private ResourceBundle rb;

  public int getIdProject() {
    return idProject;
  }

  public void setIdProject(int idProject) {
    this.idProject = idProject;
  }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb; 
        System.out.println("id projecto = "+ idProject);
    }    
    
       @FXML
    void addTab(ActionEvent event) {
      try {
        Tab tab = new Tab();
        tab.setText("untitled");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Tab.fxml"), rb);
        ScrollPane newFile = loader.load();
        tab.setContent(newFile);
        tabPaneArchivos.getTabs().add(tab); 
        tabPaneArchivos.getSelectionModel().selectLast();
      } catch (IOException ex) {
        Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    public void open_Editor(int idProject, Stage fileExplorerStage, ResourceBundle rb) {
      //this.rb = rb;
    try { 

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Editor.fxml"), rb);
      
      IU_EditorController controller = new IU_EditorController();
      fxmlLoader.setController(controller);
      System.out.println("abriendo otra ventana, proyecto = "+idProject);
      controller.setIdProject(idProject);
      
      Parent root1 = (Parent) fxmlLoader.load();
      
      //Stage stage = new Stage();
      fileExplorerStage.setScene(new Scene(root1));
      fileExplorerStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
    
}

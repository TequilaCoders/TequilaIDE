package graphics.textEditor;

import entities.Archivo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_EditorController implements Initializable {

    /*@FXML
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
    private MenuItem menuItemAbout;*/
    @FXML
    private TabPane tabPaneArchivos;
    
    List<Archivo> fileList = new ArrayList<>();
    
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
        loadFiles();
        showFirstTab(checkNumberOfFiles());
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
    
    /**
     * Metodo sobrecargado para crear la primera pestaña de un proyecto que no tiene archivos
     */
    @FXML
    void addTab() {
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
    
    /**
     * Metodo sobrecargado para crear la primera pestaña de un proyecto que tiene archivos
     */
    @FXML
    void addTab(String title, String content) {
      try {
        Tab tab = new Tab();
        tab.setText(title);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Tab.fxml"), rb);
        
        IU_TabController controller = new IU_TabController();
        loader.setController(controller);
        
        ScrollPane newFile = loader.load();
        controller.setContent(content);
        tab.setContent(newFile);
        
        tabPaneArchivos.getTabs().add(tab); 
        tabPaneArchivos.getSelectionModel().selectLast();
      } catch (IOException ex) {
        Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    /**
     * Method used to see when to open a new tab, and when to open an existing file
     * @return 
     */
    public int checkNumberOfFiles(){
      int fileCuantity;
      
      if (fileList.isEmpty()) {
        return fileCuantity = 0;
      } else {
        return fileCuantity = fileList.size();
      }
    }
    
    public void showFirstTab(int fileCuantity){
      if (fileCuantity == 0) {
        addTab();
      } else {
        String name = fileList.get(0).getNombre();
        String content = fileList.get(0).getContenido();
        addTab(name, content);
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
    
    public void loadFiles() {

    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    TypedQuery<Archivo> query
        = entitymanager.createNamedQuery("Archivo.findByProyectoidProyecto", Archivo.class).setParameter("proyectoidProyecto", idProject); //CAMBIAR EL 1 POR EL ID DE USUARIO>>>>
    fileList = query.getResultList();
  }
    
}

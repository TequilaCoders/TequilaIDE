package graphics.textEditor;

import entities.Archivo;
import entities.ArchivoPK;
import graphics.fileExplorer.IU_FileExplorerController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
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

  @FXML
  private Pane paneToolBar;

  List<Archivo> fileList = new ArrayList<>();

  private int idProject;

  private ResourceBundle rb;
  
  Stage mainStage;

  public int getIdProject() {
    return idProject;
  }

  public void setIdProject(int idProject) {
    this.idProject = idProject;
  }

  @FXML
  void returnToFileExplorer(MouseEvent event) {
    
    mainStage = (Stage) tabPaneArchivos.getScene().getWindow();
    IU_FileExplorerController explorador = new IU_FileExplorerController();
    explorador.open_FileExplorer(mainStage, rb);
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
  void newTab(MouseEvent event) {
    if (event.getButton().equals(MouseButton.PRIMARY)) {
      if (event.getClickCount() == 2) {
        addTab();
      }
    }
  }

  @FXML
  void addTab(ActionEvent event) {
    try {
      Tab tab = new Tab();
      tab.setText("untitled");
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Tab.fxml"), rb);

      IU_TabController controller = new IU_TabController();
      loader.setController(controller);

      ScrollPane newFile = loader.load();
      tab.setContent(newFile);
      tabPaneArchivos.getTabs().add(tab);
      tabPaneArchivos.getSelectionModel().selectLast();
      listener_TabClosed(tab, controller, -1);
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

      IU_TabController controller = new IU_TabController();
      loader.setController(controller);

      ScrollPane newFile = loader.load();

      tab.setContent(newFile);

      tabPaneArchivos.getTabs().add(tab);
      
      tabPaneArchivos.getSelectionModel().selectLast();
      listener_TabClosed(tab, controller, -1);
    } catch (IOException ex) {
      Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Metodo sobrecargado para crear la primera pestaña de un proyecto que tiene archivos
   */
  @FXML
  void addTab(String title, String content, int fileId) {
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
      listener_TabClosed(tab, controller, fileId);
    } catch (IOException ex) {
      Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void listener_TabClosed(Tab tab, IU_TabController controller, int fileId) {

    tab.setOnClosed(e -> {
      if (controller.getContent().isEmpty() == false) {
        System.out.println("se activo guardado automatico");
        saveTextContent(tab, controller, fileId);
      }
    });
    
    
  }

  public void saveTextContent(Tab tab, IU_TabController controller, int fileId) {
    String content = controller.getContent();
    String name = tab.getText();

    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    //se asocia el archivo al proyecto
    ArchivoPK fileKeys = new ArchivoPK();

    //si el archivo ya existe, se le asigna el id correspondiente (Solo se actualiza el objeto)
    if (fileId != -1) {
      fileKeys.setIdArchivo(fileId);
      fileKeys.setProyectoidProyecto(idProject);

      Archivo fileToUpdate = entitymanager.find(Archivo.class, fileKeys);

      fileKeys.setProyectoidProyecto(idProject);

      entitymanager.getTransaction().begin();
      fileToUpdate.setContenido(content);
      entitymanager.getTransaction().commit();

      entitymanager.close();

      //si no existe, entonces se crea y se guarda
    } else {

      fileKeys.setProyectoidProyecto(idProject);

      //se asignan los atributos al archivo
      Archivo fileToSave = new Archivo();
      fileToSave.setNombre(name);
      fileToSave.setTipo("java");
      fileToSave.setContenido(content);
      fileToSave.setArchivoPK(fileKeys);

      //se registra en la unidad de persistencia
      //Se inserta el objeto en la BD
      entitymanager.getTransaction().begin();
      entitymanager.persist(fileToSave);
      entitymanager.getTransaction().commit();

      entitymanager.close();
    }
  }

  /**
   * Method used to see when to open a new tab, and when to open an existing file
   *
   * @return
   */
  public int checkNumberOfFiles() {
    int fileCuantity;

    if (fileList.isEmpty()) {
      return fileCuantity = 0;
    } else {
      return fileCuantity = fileList.size();
    }
  }

  public void showFirstTab(int fileCuantity) {
    if (fileCuantity == 0) {
      addTab();
    } else {
      int fileId = fileList.get(0).getArchivoPK().getIdArchivo();
      String name = fileList.get(0).getNombre();
      String content = fileList.get(0).getContenido();
      addTab(name, content, fileId);
    }
  }

  public void open_Editor(int idProject, Stage fileExplorerStage, ResourceBundle rb) {
    //this.rb = rb;
    try {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Editor.fxml"), rb);

      IU_EditorController controller = new IU_EditorController();
      fxmlLoader.setController(controller);
      System.out.println("abriendo otra ventana, proyecto = " + idProject);
      controller.setIdProject(idProject);

      Parent root1 = (Parent) fxmlLoader.load();

      //Stage stage = new Stage();
      fileExplorerStage.setScene(new Scene(root1));

      fileExplorerStage.show();
      //mainStage = fileExplorerStage;

      fileExplorerStage.setMaximized(true);
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

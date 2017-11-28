package graphics.textEditor;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import entities.Archivo;
import entities.ArchivoPK;
import entities.Proyecto;
import graphics.fileExplorer.IU_FileExplorerController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

  @FXML
  private TabPane tabPaneArchivos;

  @FXML
  private JFXDrawer drawerFileTree;

  @FXML
  private Pane paneHamburger;

  @FXML
  private ImageView imageVHamburger;

  @FXML
  private Pane paneFileExplorer;

  @FXML
  private ImageView imageVFileExplorer;

  List<Archivo> fileList = new ArrayList<>();

  List<String> currentTabs = new ArrayList<>();

  private Proyecto selectedProject;

  private int projectID;

  private ResourceBundle rb;

  Stage mainStage;

  public void setSelectedProject(Proyecto selectedProject) {
    this.selectedProject = selectedProject;
  }

  public void setProjectID() {
    this.projectID = selectedProject.getProyectoPK().getIdProyecto();
  }

  public List<String> getCurrentTabs() {
    return currentTabs;
  }

  /**
   * Método que cierra la ventana IU_Editor y abre IU_FileExplorer
   * @param event 
   */
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
    setProjectID();
    loadFiles();
    showFirstTab(checkNumberOfFiles());
    listeners();
  }

  /**
   * Método que crea una nueva pestaña en el evento de 2 clics sobre el tabPane
   * @param event 
   */
  @FXML
  void newTab(MouseEvent event) {
    if (event.getButton().equals(MouseButton.PRIMARY)) {
      if (event.getClickCount() == 2) {
        addTab();
      }
    }
  }

  /**
   * 
   * @param event 
   */
  @FXML
  void addTab(ActionEvent event) {
    try {
      Tab tab = new Tab();
      tab.setText("untitled");
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Tab.fxml"), rb);

      IU_TabController controller = new IU_TabController();
      loader.setController(controller);
      controller.setTab(tab);
      controller.setFileList(fileList);

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
   * Metodo sobrecargado para crear la primera pestaña de un proyecto que no tiene archivos.
   */
  @FXML
  void addTab() {
    try {
      Tab tab = new Tab();
      tab.setText("untitled");
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Tab.fxml"), rb);

      IU_TabController controller = new IU_TabController();
      loader.setController(controller);
      controller.setTab(tab);
      controller.setFileList(fileList);

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
   * Metodo sobrecargado para crear la primera pestaña de un proyecto que tiene archivos.
   */
  @FXML
  void addTab(String title, String content, int fileId) {
    try {
      Tab tab = new Tab();
      tab.setText(title);
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Tab.fxml"), rb);
      
      IU_TabController controller = new IU_TabController();
      loader.setController(controller);
      controller.setTab(tab);
      controller.setFileList(fileList);

      ScrollPane newFile = loader.load();
      controller.setContent(content);
      tab.setContent(newFile);

      tabPaneArchivos.getTabs().add(tab);
      //se agrega el titulo de la pestaña a la lista de pestañas abiertas actualmente
      currentTabs.add(title);
      
      tabPaneArchivos.getSelectionModel().selectLast();
      listener_TabClosed(tab, controller, fileId);
    } catch (IOException ex) {
      Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Carga un drawer con un vbox que contiene el explorador de archivos en forma de treeview
   * @param event 
   */
  @FXML
  void openDrawer(MouseEvent event) {   

    try {
      
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_FileTree.fxml"), rb);
      IU_FileTreeController controller = new IU_FileTreeController();
      
      loader.setController(controller);
      
      controller.setSelectedProject(selectedProject);
      controller.setFileList(fileList);
      controller.setEditorController(this);
      
      VBox vbox = loader.load();
      
      drawerFileTree.setSidePane(vbox);
      
      if (drawerFileTree.isShown()) {
 
        drawerFileTree.close();
        drawerFileTree.toBack();
        
      } else {
        drawerFileTree.toFront();
        drawerFileTree.open();
        
      }
    } catch (IOException ex) {
      Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Método que esta a la escucha del evento de cierre de una pestaña.
   * @param tab
   * @param controller
   * @param fileId 
   */
  public void listener_TabClosed(Tab tab, IU_TabController controller, int fileId) {

    tab.setOnClosed(e -> {
      if (controller.getContent().trim().isEmpty() == false) {
        
        //se elimina la pestaña de la lista de pestañas abiertas actuales
        String title = tab.getText();
        currentTabs.remove(title);
        
        //se guarda el contenido de la pestaña
        saveTextContent(tab, controller, fileId);
        loadFiles();
      }
    }); 
  }

  /**
   * Método encargado de guardar el contenido de una pestaña en el evento de cierre.
   * @param tab
   * @param controller
   * @param fileId 
   */
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
      fileKeys.setProyectoidProyecto(projectID);

      Archivo fileToUpdate = entitymanager.find(Archivo.class, fileKeys);

      fileKeys.setProyectoidProyecto(projectID);

      entitymanager.getTransaction().begin();
      fileToUpdate.setContenido(content);
      entitymanager.getTransaction().commit();

      entitymanager.close();

      //si no existe, entonces se crea y se guarda
    } else {

      fileKeys.setProyectoidProyecto(projectID);

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

  /**
   * Método que carga la primera pestaña con el primer archivo de la variable fileList.
   * @param fileCuantity 
   */
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

  /**
   * Método que carga la ventana IU_Editor.fxml, recibe el proyecto seleccionado 
   * @param selectedProject
   * @param fileExplorerStage
   * @param rb 
   */
  public void open_Editor(Proyecto selectedProject, Stage fileExplorerStage, ResourceBundle rb) {
    //this.rb = rb;
    try {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Editor.fxml"), rb);

      IU_EditorController controller = new IU_EditorController();
      fxmlLoader.setController(controller);
      System.out.println("abriendo otra ventana, proyecto = " + selectedProject.getProyectoPK().getIdProyecto());
      controller.setSelectedProject(selectedProject);

      Parent root1 = (Parent) fxmlLoader.load();
      fileExplorerStage.setScene(new Scene(root1));

      fileExplorerStage.show();
      fileExplorerStage.setMaximized(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Metodo que recupera todos los archivos relacionados al proyecto.
   */
  public void loadFiles() {

    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    TypedQuery<Archivo> query
        = entitymanager.createNamedQuery("Archivo.findByProyectoidProyecto", Archivo.class).setParameter("proyectoidProyecto", projectID); //CAMBIAR EL 1 POR EL ID DE USUARIO>>>>
    fileList = query.getResultList();
  }

  /**
   * Métodos que estan a la escucha de eventos en los elementos paneHamburger y paneFileExplorer.
   */
  public void listeners(){
    paneHamburger.setOnMouseEntered((e -> imageVHamburger.setImage(new Image("/resources/icons/hamburger_white.png"))));
    paneHamburger.setOnMouseExited((e -> imageVHamburger.setImage(new Image("/resources/icons/hamburger_orange.png"))));

    paneFileExplorer.setOnMouseEntered((e -> imageVFileExplorer.setImage(new Image("/resources/icons/proyecto_seleccionado.png"))));
    paneFileExplorer.setOnMouseExited((e -> imageVFileExplorer.setImage(new Image("/resources/icons/proyecto.png"))));
    
  }
}

package graphics.textEditor;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXDrawer;
import graphics.fileExplorer.IU_FileExplorerController;
import static graphics.login.IU_LogInController.socket;
import graphics.tools.Tools;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import logic.Collaborator;
import logic.File;
import logic.Project;
import logic.User;
import org.json.JSONArray;
import org.json.JSONObject;

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

  @FXML
  private Button buttonNewCollaborator;

  @FXML
  private ImageView imageVNewCollaborator;

  @FXML
  private MenuButton menuButtonUser;

  @FXML
  private MenuItem menuItemCollaboratorInformation;

  @FXML
  private MenuItem menuItemDeleteCollaborator;

  @FXML
  private ImageView imageVUser;

  @FXML
  private ToolBar tbAddedCollaborator;

  List<File> fileList = new ArrayList<>();

  List<Collaborator> collaboratorsList = new ArrayList<>();

  List<String> currentTabs = new ArrayList<>();

  List<MenuButton> collaboratorsButtons = new ArrayList<>();

  private Project selectedProject;

  private int projectID;

  private ResourceBundle rb;

  Stage mainStage;

  User user;

  AnchorPane anchorAddCollaborator;

  private boolean savedStatus;

  public void setSelectedProject(Project selectedProject) {
    this.selectedProject = selectedProject;
  }

  public void setProjectID() {
    this.projectID = selectedProject.getIdProyecto();
  }

  public List<String> getCurrentTabs() {
    return currentTabs;
  }

  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Método que cierra la ventana IU_Editor y abre IU_FileExplorer
   *
   * @param event
   */
  @FXML
  void returnToFileExplorer(MouseEvent event) {

    System.out.println("entrando al explorador");
    mainStage = (Stage) tabPaneArchivos.getScene().getWindow();
    IU_FileExplorerController explorador = new IU_FileExplorerController();
    explorador.open_FileExplorer(mainStage, rb, user);
  }

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    this.rb = rb;
    setProjectID();
    loadFiles();
    System.out.println("cargando id");
    
    System.out.println("datos proyecto seleccionado ");
    System.out.println("id " +selectedProject.getIdProyecto());
    System.out.println("id " +selectedProject.getNombre());
    
    this.projectID = selectedProject.getIdProyecto();
    
    System.out.println("el proyecto es compartido ? " + selectedProject.isShared());
    if (!selectedProject.isShared()) {
      setUserIcons();
    }

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        showFirstTab(checkNumberOfFiles());
        loadCollaborators();
      }

    });

    listeners();
    System.out.println("id proyecto " + projectID);
  }

  public void setUserIcons() {
    String alias = user.getAlias();
    Tooltip tootTip = new Tooltip();

    tootTip.setText(alias);
    menuButtonUser.setTooltip(tootTip);

    menuItemCollaboratorInformation.setText(alias + " (tu)");
    
    menuButtonUser.setDisable(false);
    buttonNewCollaborator.setDisable(false);
  }

  public void setCollaboratorsIcons() {

    for (int i = 0; i < collaboratorsList.size(); i++) {
      String collaboratorAlias = collaboratorsList.get(i).getAlias();
      System.out.println("alias " + collaboratorAlias);
      System.out.println("id " + collaboratorsList.get(i).getIdUsuario());
      ImageView imagev = new ImageView();
      imagev.setFitHeight(30);
      imagev.setFitWidth(29);
      imagev.setImage(new Image("/resources/icons/user_white.png"));

      MenuButton button = new MenuButton();
      button.setPrefHeight(37);
      button.setPrefWidth(45);

      button.setStyle("-fx-background-color: transparent;");

      button.setGraphic(imagev);

      Tooltip tootTip = new Tooltip();

      tootTip.setText(collaboratorAlias);
      button.setTooltip(tootTip);

      MenuItem menuItem = new MenuItem(collaboratorAlias);
      MenuItem menuItem2 = new MenuItem("Eliminar Colaborador");
      
      if (selectedProject.isShared()) {
        menuItem2.setDisable(true);
      }
      
 
      button.getItems().addAll(menuItem, menuItem2);
      collaboratorsButtons.add(button);
    }
    tbAddedCollaborator.getItems().clear();
    tbAddedCollaborator.getItems().addAll(collaboratorsButtons);
  }

  public void hoverListeners() {
    for (int i = 0; i < collaboratorsButtons.size(); i++) {
      ImageView im1 = (ImageView) collaboratorsButtons.get(i).getGraphic();
      collaboratorsButtons.get(i).setOnMouseEntered((e -> im1.setImage(new Image("/resources/icons/user_yellow.png"))));
      ImageView im2 = (ImageView) collaboratorsButtons.get(i).getGraphic();
      collaboratorsButtons.get(i).setOnMouseExited((e -> im2.setImage(new Image("/resources/icons/user_white.png"))));
    }
  }

  public void menuItemsSelectedAction() {
    for (int i = 0; i < collaboratorsButtons.size(); i++) {
      int indice = i;
      menuItemCollaboratorInformation = collaboratorsButtons.get(i).getItems().get(0);

      menuItemCollaboratorInformation.setOnAction(new EventHandler() {
        @Override
        public void handle(Event event) {
          String alias =  collaboratorsList.get(indice).getAlias();
          String biography =  collaboratorsList.get(indice).getBiografia();
          Tools.displayInformation(alias, biography);
        }

      });

      menuItemDeleteCollaborator = collaboratorsButtons.get(i).getItems().get(1);

      menuItemDeleteCollaborator.setOnAction(new EventHandler() {
        @Override
        public void handle(Event event) {
          System.out.println("aqui esta tu informacion :V de nuevo ");
          System.out.println("fuente : " + collaboratorsList.get(indice).getAlias());
          int collaboratorID = collaboratorsList.get(indice).getIdUsuario();
          deleteCollaborator(collaboratorID);
        }

      });
    }
  }

  @FXML
  void addNewCollaborator(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("IU_AddCollaborator.fxml"));
    Stage stageAddCollaborator;
    IU_AddCollaboratorController controller = new IU_AddCollaboratorController();
    controller.setProjectID(projectID);
    stageAddCollaborator = loadAddCollaboratorWindow(loader, controller);
    stageAddCollaborator.show();

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        System.out.println("escuchando el cierre");
        listener_AddCollaboratorWindow_Closed(loader, stageAddCollaborator);
        System.out.println("cierre completado");
      }
    });
  }

  /**
   * Método que crea una nueva pestaña en el evento de 2 clics sobre el tabPane
   *
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
      
      Platform.runLater(new Runnable() {
      @Override
      public void run() {
        listener_TabClosed(tab, controller, -1);
      }

    });
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
      
      Platform.runLater(new Runnable() {
      @Override
      public void run() {
        listener_TabClosed(tab, controller, -1);
      }

    });
   
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
      controller.setFile(fileId);
      controller.setFileList(fileList);

      ScrollPane newFile = loader.load();
      controller.setContent(content);
      tab.setContent(newFile);

      tabPaneArchivos.getTabs().add(tab);
      //se agrega el titulo de la pestaña a la lista de pestañas abiertas actualmente
      currentTabs.add(title);

      tabPaneArchivos.getSelectionModel().selectLast();
      
      Platform.runLater(new Runnable() {
      @Override
      public void run() {
        listener_TabClosed(tab, controller, fileId);
      }

    });
    } catch (IOException ex) {
      Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Carga un drawer con un vbox que contiene el explorador de archivos en forma de treeview
   *
   * @param event
   */
  @FXML
  void openDrawer(MouseEvent event) {
    loadFiles();
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
  /*
  public void closeDrawer() {
    if (drawerFileTree.isShown()) {

        drawerFileTree.close();
        drawerFileTree.toBack();

      }
  }*/

  /**
   * Método que esta a la escucha del evento de cierre de una pestaña.
   *
   * @param tab
   * @param controller
   * @param fileId
   */
  public void listener_TabClosed(Tab tab, IU_TabController controller, int fileId) {
    
    tab.setOnClosed(e -> {
      if (controller.getContent().trim().isEmpty() == false) {

        System.out.println("ENTRO A LA VALIDACION :d");
        //se elimina la pestaña de la lista de pestañas abiertas actuales
        String title = tab.getText();
        currentTabs.remove(title);

        if (fileId != -1) {
          updateFile(tab, controller, fileId);
        } else {
          createNewFile(tab, controller);
        }
      }
    });
  }

  public void createNewFile(Tab tab, IU_TabController controller) {
    String content = controller.getContent();
    String name = tab.getText();

    JSONObject fileToSave = new JSONObject();

    fileToSave.accumulate("name", name);
    fileToSave.accumulate("content", content);
    fileToSave.accumulate("projectID", projectID);

    //------------VALOR TEMPORAL !!!--------------------
    fileToSave.accumulate("fileType", "java");
    //--------------------------------------------------

    socket.connect();
    System.err.println("logrado");

    socket.emit("saveFile", fileToSave);

    socket.on("fileSaved", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        System.out.println("file succesfully saved");
        socket.close();
        loadFiles();
      }
    });

  }

  public void updateFile(Tab tab, IU_TabController controller, int fileId) {
    String content = controller.getContent();
    String name = tab.getText();

    JSONObject fileToUpdate = new JSONObject();

    fileToUpdate.accumulate("fileID", fileId);
    fileToUpdate.accumulate("name", name);
    fileToUpdate.accumulate("content", content);

    socket.connect();
    System.err.println("logrado");

    socket.emit("updateFile", fileToUpdate);

    socket.on("fileUpdated", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        System.out.println("file succesfully updated");
        socket.close();
        loadFiles();
      }

    });
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
   *
   * @param fileCuantity
   */
  public void showFirstTab(int fileCuantity) {
    if (fileCuantity == 0) {
      addTab();
      System.out.println("cantidad de archivos " + fileCuantity);
      System.out.println("entro a la primera validacion");
    } else {
      int fileId = fileList.get(0).getIdArchivo();
      String name = fileList.get(0).getNombre();
      String content = fileList.get(0).getContenido();
      System.out.println("entro a primera pestaña");
      addTab(name, content, fileId);
    }
  }

  /**
   * Método que carga la ventana IU_Editor.fxml, recibe el proyecto seleccionado
   *
   * @param selectedProject
   * @param fileExplorerStage
   * @param rb
   */
  public void open_Editor(Project selectedProject, Stage fileExplorerStage, ResourceBundle rb, User user) {
    //this.rb = rb;
    try {

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/textEditor/IU_Editor.fxml"), rb);

      IU_EditorController controller = new IU_EditorController();
      fxmlLoader.setController(controller);
      System.out.println("abriendo otra ventana, proyecto = " + selectedProject.getIdProyecto());
      controller.setSelectedProject(selectedProject);
      controller.setUser(user);

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

    JSONObject projectIDToSend = new JSONObject();
    projectIDToSend.accumulate("projectID", projectID);

    socket.connect();
    System.err.println("logrado");

    socket.emit("loadFiles", projectIDToSend);

    socket.on("filesRecovered", new Emitter.Listener() {
      @Override
      public void call(Object... os) {

        JSONArray receivedList = (JSONArray) os[1];
        String jsonString = receivedList.toString();

        Gson gson = new Gson();

        File[] jsonFileList = gson.fromJson(jsonString, File[].class);
        fileList = Arrays.asList(jsonFileList);

        System.out.println("cantidad de arcCChivos " + fileList.size());
        socket.emit("filesreceived");
        socket.close();
        System.out.println("desconectado");

      }
    });

  }

  public void loadCollaborators() {
    System.out.println("se abrio funcion loadCollaborators");
    JSONObject projectIDToSend = new JSONObject();
    projectIDToSend.accumulate("projectID", projectID);

    socket.connect();
    System.err.println("logrado");

    socket.emit("getCollaborators", projectIDToSend);

    socket.on("collaboratorsRecovered", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        System.out.println("objetos recuperados " + Arrays.toString(os));

        JSONArray receivedList = (JSONArray) os[0];
        String jsonString = receivedList.toString();

        Gson gson = new Gson();

        Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
        System.out.println("lista recuperada de colaboradores: " + Arrays.toString(jsonFileList));
        collaboratorsList = Arrays.asList(jsonFileList);

        System.out.println("lista convertida de colaboradores: " + collaboratorsList);

        socket.close();

        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            collaboratorsButtons.clear();
            setCollaboratorsIcons();
            hoverListeners();
            menuItemsSelectedAction();
          }

        });
      }

    });

  }

  public void deleteCollaborator(int collaboratorID) {
    JSONObject collaborationToSend = new JSONObject();

    collaborationToSend.accumulate("projectID", projectID);
    collaborationToSend.accumulate("collaboratorID", collaboratorID);

    socket.connect();
    System.err.println("logrado");

    socket.emit("deleteCollaborator", collaborationToSend);

    socket.on("collaborationDeleted", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        socket.close();
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            Tools.displayInformation("Colaborador Eliminado", "El colaborador ha sido eliminado");
            collaboratorsButtons.clear();
            loadCollaborators();
          }

        });
      }

    });
  }
  
  public void deleteFile(int fileID) {
    System.out.println("ENTRO AL METODO DE ELIMINAR ARCHIVO!!!!!!!!");
    JSONObject fileToSend = new JSONObject();

    fileToSend.accumulate("fileID", fileID);

    socket.connect();
    System.err.println("logrado");

    socket.emit("deleteFile", fileToSend);

    socket.on("fileDeleted", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        System.out.println("file deleted");
        socket.close();
        loadFiles();
      }

    });
  }

  /**
   * Métodos que estan a la escucha de eventos en los elementos paneHamburger y paneFileExplorer.
   */
  public void listeners() {

    paneHamburger.setOnMouseEntered((e -> imageVHamburger.setImage(new Image("/resources/icons/hamburger_white.png"))));
    paneHamburger.setOnMouseExited((e -> imageVHamburger.setImage(new Image("/resources/icons/hamburger_orange.png"))));

    paneFileExplorer.setOnMouseEntered((e -> imageVFileExplorer.setImage(new Image("/resources/icons/proyecto_seleccionado.png"))));
    paneFileExplorer.setOnMouseExited((e -> imageVFileExplorer.setImage(new Image("/resources/icons/proyecto.png"))));

    buttonNewCollaborator.setOnMouseEntered((e -> imageVNewCollaborator.setImage(new Image("/resources/icons/add_user_yellow.png"))));
    buttonNewCollaborator.setOnMouseExited((e -> imageVNewCollaborator.setImage(new Image("/resources/icons/add_user_white.png"))));

    menuButtonUser.setOnMouseEntered((e -> imageVUser.setImage(new Image("/resources/icons/user_yellow.png"))));
    menuButtonUser.setOnMouseExited((e -> imageVUser.setImage(new Image("/resources/icons/user_white.png"))));
  }

  public Stage loadAddCollaboratorWindow(FXMLLoader loader, IU_AddCollaboratorController controller) {
    try {
      loader.setController(controller);
      anchorAddCollaborator = loader.load();
    } catch (IOException ex) {
      Logger.getLogger(IU_FileExplorerController.class.getName()).log(Level.SEVERE, null, ex);
    }

    Stage stageAddCollaborator = new Stage();

    stageAddCollaborator.initOwner(menuButtonUser.getScene().getWindow());
    Scene escena = new Scene(anchorAddCollaborator);
    stageAddCollaborator.initStyle(StageStyle.UNDECORATED);
    stageAddCollaborator.setScene(escena);

    return stageAddCollaborator;
  }

  public void listener_AddCollaboratorWindow_Closed(FXMLLoader loader, Stage stageAddCollaborator) {
    stageAddCollaborator.setOnHiding(new EventHandler<WindowEvent>() {

      @Override
      public void handle(WindowEvent we) {
        savedStatus = loader.<IU_AddCollaboratorController>getController().isSavedStatus();

        if (savedStatus) {
          //collaboratorsList.clear();
          collaboratorsButtons.clear();
          loadCollaborators();
          
        }
      }
    });
  }
}

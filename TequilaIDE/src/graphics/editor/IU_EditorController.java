package graphics.editor;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import graphics.explorer.IU_FileExplorerController;
import graphics.tools.Tools;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import logic.domain.Collaborator;
import logic.domain.File;
import logic.domain.Project;
import logic.domain.User;
import logic.sockets.SocketCollaborator;
import logic.sockets.SocketFile;
import logic.sockets.SocketProject;
import org.json.JSONArray;
import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

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
  private MenuItem menuItemDeleteProject;
  @FXML
  private MenuItem menuItemAddNewCollaborator;
  @FXML
  private Menu menuHelp;
  @FXML
  private MenuItem menuItemAbout;
  @FXML
  private ToolBar tbCollaborators;
  @FXML
  private MenuButton menuButtonUser;
  @FXML
  private MenuItem menuItemCollaboratorInformation;
  @FXML
  private MenuItem menuItemDeleteCollaborator;
  @FXML
  private MenuItem menuItemAbandonProject;
  @FXML
  private MenuItem menuItemDeleteFile;
  @FXML
  private ImageView imageVUser;
  @FXML
  private ToolBar tbAddedCollaborator;
  @FXML
  private ImageView buttonFileExplorer;
  @FXML
  private ImageView buttonNewFile;
  @FXML
  private ImageView buttonCompile;
  @FXML
  private ImageView buttonRun;
  @FXML
  private TabPane tabPaneFiles;
  @FXML
  private JFXDrawer drawerFileTree;
  @FXML
  private JFXHamburger hamburgerButton;

  List<File> fileList = new ArrayList<>();

  List<Collaborator> collaboratorsList = new ArrayList<>();

  List<String> currentTabs = new ArrayList<>();

  List<MenuButton> collaboratorsButtons = new ArrayList<>();
  
  List<Collaborator> collaboratorsConnected = new ArrayList<>();

  private Project selectedProject;

  private ResourceBundle rb;

  Stage mainStage;

  User user;

  AnchorPane anchorAddCollaborator;

  private boolean savedStatus;
  
  private HamburgerBackArrowBasicTransition hamburgerTransition; 
  
    /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    menuItemNew.setOnAction(e -> {
      addTab();
    });
    
    hamburgerTransition = new HamburgerBackArrowBasicTransition(hamburgerButton);
    hamburgerTransition.setRate(-1);
    this.rb = rb;
    loadFiles();

    if (!selectedProject.isShared()) {
      setUserIcons();
      menuItemAbandonProject.setDisable(true);
    } else {
      menuItemDeleteFile.setDisable(true);
      menuItemDeleteProject.setDisable(true);
      menuItemAddNewCollaborator.setDisable(true);
    }

    Platform.runLater(() -> {
      loadCollaborators();
      Platform.runLater(() -> {
        //joinProjectRoom();
        Platform.runLater(() -> {
          joinProjectRoom();
          //showFirstTab(checkNumberOfFiles());
        });
        showFirstTab(checkNumberOfFiles());
      });
    });

    drawerFileTree.setOnDrawerClosed(event -> {
      drawerFileTree.toBack();
    });
	
	if (selectedProject.getLenguaje().equals("py")) {
	  buttonCompile.setDisable(true);
	  buttonCompile.setOpacity(0.5);
	}
	
    menuButtonUser.setOnMouseEntered((e -> imageVUser.setImage(new Image("/resources/icons/user_yellow.png"))));
    menuButtonUser.setOnMouseExited((e -> imageVUser.setImage(new Image("/resources/icons/user_white.png"))));
    listenServer();
  }

  public void setSelectedProject(Project selectedProject) {
    this.selectedProject = selectedProject;
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
    SocketFile socketFile = new SocketFile();
    socketFile.leaveFilesRoom();
    
    SocketProject socketProject = new SocketProject();
    socketProject.leaveProjectRoom(selectedProject.getIdProyecto(), user.getIdUsuario());

    mainStage = (Stage) tabPaneFiles.getScene().getWindow();
    IU_FileExplorerController explorador = new IU_FileExplorerController();
    explorador.openFileExplorer(mainStage, rb, user);
  }
  
  /**
   * Metodo sobrecargado que cierra la ventana IU_Editor y abre IU_FileExplorer sin recibir argumentos.
   */
  public void returnToFileExplorer() {
    SocketFile socketFile = new SocketFile();
    socketFile.leaveFilesRoom();
    
    SocketProject socketProject = new SocketProject();
    socketProject.leaveProjectRoom(selectedProject.getIdProyecto(), user.getIdUsuario());

    mainStage = (Stage) tabPaneFiles.getScene().getWindow();
    IU_FileExplorerController explorador = new IU_FileExplorerController();
    explorador.openFileExplorer(mainStage, rb, user);
  }

  /**
   * Elimina el proyecto actual, primero elimina la lista de archivos y la lista de colaboradores.
   * @param event 
   */
  @FXML
  void onDeleteProject(ActionEvent event) {
    boolean choice;
    String intStringDeleteProjectConfirmation = rb.getString("DeleteProjectConfirmation");
    choice = Tools.displayWarningAlertWithChoice(intStringDeleteProjectConfirmation, rb);
    if (choice) {
      SocketProject socketProject = new SocketProject();
      socketProject.deleteProject(selectedProject.getIdProyecto());
      returnToFileExplorer();
      
      String intStringDeletedProjectConfirmation = rb.getString("DeletedProjectConfirmation");
      Tools.displayConfirmationAlert(intStringDeletedProjectConfirmation, rb);
    }
  }

  /**
   * Elimina el archivo que este seleccionado en el tabPaneFiles.
   * @param event 
   */
  @FXML
  void onDeleteFile(ActionEvent event) {
    if (!tabPaneFiles.getSelectionModel().isEmpty()) {
      Tab tab = tabPaneFiles.getSelectionModel().getSelectedItem();
      File fileToDelete = searchFileByName(tab.getId());
      int fileID = fileToDelete.getIdArchivo();

      tab.getTabPane().getTabs().remove(tab);
      SocketFile socketFile = new SocketFile();
      socketFile.deleteFile(fileID);
      String intStringDeletedFileTitle = rb.getString("deletedFileTitle");
      String intStringDeletedFileMessage = rb.getString("deletedFileMessage");
      Tools.displayInformation(intStringDeletedFileTitle, intStringDeletedFileMessage);

    }
  }
  
  /**
   * Regresa el archivo cuyo nombre coincida con la cadena recibida
   * @param name
   * @return 
   */
  public File searchFileByName(String name){
    File foundFile = null;
    for (int i = 0; i < fileList.size(); i++) {
      if (fileList.get(i).getNombre().equals(name)) {
        foundFile = fileList.get(i);
      }
    }
    return foundFile;
  }

  /**
   * Despliega el icono del usuario creador del proyecto.
   */
  public void setUserIcons() {
    String alias = user.getAlias();
    Tooltip tootTip = new Tooltip();

    tootTip.setText(alias);
    menuButtonUser.setTooltip(tootTip);
    
    String youPronoun = rb.getString("youPronoun");
    menuItemCollaboratorInformation.setText(alias + " " + youPronoun);
    
    menuButtonUser.setDisable(false);
  }

  /**
   * Despliega los iconos de los colaboradores secundarios.
   */
  public void setCollaboratorsIcons() {
    for (int i = 0; i < collaboratorsList.size(); i++) {
      Collaborator collaborator = collaboratorsList.get(i);
      String collaboratorAlias = collaborator.getAlias();
      
      ImageView imagev = new ImageView();
      imagev.setFitHeight(30);
      imagev.setFitWidth(29);

      if (collaborator.isConnected()) {
        imagev.setImage(new Image("/resources/icons/user_forestGreen.png"));
      } else {
        imagev.setImage(new Image("/resources/icons/user_orange.png"));
      }

      MenuButton button = new MenuButton();
      button.setPrefHeight(37);
      button.setPrefWidth(45);
      button.setStyle("-fx-background-color: transparent;");
      button.setGraphic(imagev);

      Tooltip tootTip = new Tooltip();

      tootTip.setText(collaboratorAlias);
      button.setTooltip(tootTip);

      String intStringDeleteCollaborator = rb.getString("stringDeleteCollaborator");
      MenuItem menuItem = new MenuItem(collaboratorAlias);
      MenuItem menuItem2 = new MenuItem(intStringDeleteCollaborator);
      
      if (selectedProject.isShared()) {
        menuItem2.setDisable(true);
      }
    
      button.getItems().addAll(menuItem, menuItem2);
      collaboratorsButtons.add(button);
    }
    tbAddedCollaborator.getItems().clear();
    tbAddedCollaborator.getItems().addAll(collaboratorsButtons);
    hoverListeners();
  }

  /**
   * Elimina la colaboracion del colaborador actual del proyecto.
   * @param event 
   */
  @FXML
  void abandonProject(ActionEvent event) {

    boolean choice;
    String intStringLeaveProjectAlert = rb.getString("leaveProjectAlert");
    choice = Tools.displayWarningAlertWithChoice(intStringLeaveProjectAlert, rb);
    if (choice) {
      SocketCollaborator socketCollaborator = new SocketCollaborator();
      socketCollaborator.deleteCollaborator(user.getIdUsuario(), selectedProject.getIdProyecto());

      returnToFileExplorer();
    }
  }

  /**
   * Agrega listeners que se activan cuando el cursor se posiciona en los botones de los
   * colaboradores.
   */
  public void hoverListeners() {
    try {
      for (int i = 0; i < collaboratorsButtons.size(); i++) {

      Collaborator collaborator = collaboratorsList.get(i);
      ImageView im1 = (ImageView) collaboratorsButtons.get(i).getGraphic();
      
      collaboratorsButtons.get(i).setOnMouseEntered((new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          
          if (collaborator.isConnected()) {
            im1.setImage(new Image("/resources/icons/user_lightGreen.png"));
          } else {
            im1.setImage(new Image("/resources/icons/user_yellow.png"));
          }
        }
      }));
      ImageView im2 = (ImageView) collaboratorsButtons.get(i).getGraphic();
      
      collaboratorsButtons.get(i).setOnMouseExited((new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
          if (collaborator.isConnected()) {
            im2.setImage(new Image("/resources/icons/user_forestGreen.png"));
          } else {
            im2.setImage(new Image("/resources/icons/user_orange.png"));
          }
        }
      }));
    }
    }catch(IndexOutOfBoundsException ex){
      Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Agrega acciones a los botones de los colaboradores.
   */
  public void menuItemsSelectedAction() {
    SocketCollaborator socketCollaborator = new SocketCollaborator();
 
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
          int collaboratorID = collaboratorsList.get(indice).getIdUsuario();
          socketCollaborator.deleteCollaborator(collaboratorID, selectedProject.getIdProyecto());
          
          String intStringWarningTitle = rb.getString("intStringWarningTitle");
          String intStringCollaboratorDeleted = rb.getString("intStringCollaboratorDeleted");
          
          Tools.displayInformation(intStringWarningTitle, intStringCollaboratorDeleted);
        }
      });
    }
  }

  /**
   * Método que abre la ventana IU_AddCollaborator para buscar y agregar un colaborador secundario
   * @param event 
   */
  @FXML
  void addNewCollaborator(ActionEvent event) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("IU_AddCollaborator.fxml"), rb);
    Stage stageAddCollaborator;
    IU_AddCollaboratorController controller = new IU_AddCollaboratorController();
    controller.setProjectID(selectedProject.getIdProyecto());
    controller.setCollaboratorsList(collaboratorsList);
    controller.setUser(user);
    stageAddCollaborator = loadAddCollaboratorWindow(loader, controller);
    stageAddCollaborator.show();

    Platform.runLater(() -> {
      onAddNewCollaboratorClosed(loader, stageAddCollaborator);
    });
  }
  
  /**
   * Carga la ventana IU_AddCollaborator para poder buscar y agregar un colaborador al proyecto.
   * @param loader
   * @param controller
   * @return 
   */
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
  
  /**
   * Método que es llamado cuando se cierra la ventana IU_AddCollaborator, verifica el atributo savedStatus
   * de la ventana, si este es verdadero recarga la barra de colaboradores del proyecto para mostrar
   * la actualización
   * @param loader
   * @param stageAddCollaborator 
   */
  public void onAddNewCollaboratorClosed(FXMLLoader loader, Stage stageAddCollaborator) {
    stageAddCollaborator.setOnHiding((WindowEvent we) -> {
      savedStatus = loader.<IU_AddCollaboratorController>getController().isSavedStatus();
      
      if (savedStatus) {
        String intStringCollaboratorSaved = rb.getString("intStringCollaboratorSaved");
        Tools.displayConfirmationAlert(intStringCollaboratorSaved, rb);
        
        collaboratorsButtons.clear();
        loadCollaborators();
      }
    });
  }

  /**
   *Método que agrega un nuevo archivo (pestaña) al proyecto
   * @param event
   */
  @FXML
  void addTab(MouseEvent event) {
	String className = Tools.displayTextInputDialog("Nueva clase", "Nombre de la clase: ");
	if (!className.equals("")) {
	  SocketFile socketFile = new SocketFile();
	  socketFile.createNewFile(className, "", selectedProject.getIdProyecto(), selectedProject.getLenguaje());
	}
  }
  
  /**
   *Método que agrega un nuevo archivo (pestaña) al proyecto
   * @param event
   */
  void addTab() {
	String className = Tools.displayTextInputDialog("Nueva clase", "Nombre de la clase: ");
	if (!className.equals("")) {
	  SocketFile socketFile = new SocketFile();
	  socketFile.createNewFile(className, "", selectedProject.getIdProyecto(), selectedProject.getLenguaje());
	}
  }

  /**
   * Metodo sobrecargado para crear la primera pestaña de un proyecto que tiene archivos.
   */
  @FXML
  void addTab(String title, String content, int fileId) {
    SocketFile socketFile = new SocketFile();
    
    try {
      Tab tab = new Tab();
      tab.setId(title);
      tab.setText(title + "." + selectedProject.getLenguaje());
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_Tab.fxml"), rb);

      IU_TabController controller = new IU_TabController();
      loader.setController(controller);
      controller.setTab(tab);
      controller.setFile(fileId);
      controller.setFileList(fileList);
      controller.setProjectID(selectedProject.getIdProyecto());

      ScrollPane newFile = loader.load();
      tab.setContent(newFile);
      socketFile.joinFilesRoom(fileId);
      controller.setContent(content);

      tabPaneFiles.getTabs().add(tab);
      tabPaneFiles.getSelectionModel().selectLast();
      currentTabs.add(title);

      tab.setOnSelectionChanged((Event t) -> {
        if (tab.isSelected()) {
          reloadFiles();
          String newContent = getUpdatedContent(fileId);
          controller.setContent(newContent);
          socketFile.joinFilesRoom(fileId);
        }
      });

      tab.setOnClosed(e -> {
        currentTabs.remove(title);
      });
    } catch (IOException ex) {
      Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Obtiene el atributo "contenido" reciente de un archivo
   * @param fileID
   * @return 
   */
  public String getUpdatedContent(int fileID) {
    String newContent = "";
    for (int i = 0; i < fileList.size(); i++) {
      if (fileList.get(i).getIdArchivo() == fileID) {
        newContent = fileList.get(i).getContenido();
      }
    }
    return newContent;
  }

  /**
   * Carga un drawer que contiene el explorador de archivos en forma de treeview
   * @param event
   */
  @FXML
  void openDrawer(MouseEvent event) {
	hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
	hamburgerTransition.play();
	try {

	  FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_FileTree.fxml"), rb);
	  IU_FileTreeController controller = new IU_FileTreeController();

	  loader.setController(controller);

	  controller.setSelectedProject(selectedProject);
	  controller.setFileList(fileList);
	  controller.setEditorController(this);

	  JFXTreeView pane = loader.load();

	  drawerFileTree.setSidePane(pane);

	  if (drawerFileTree.isShown()) {
		drawerFileTree.close();
	  } else {
		drawerFileTree.toFront();
		drawerFileTree.open();
	  }
	} catch (IOException ex) {
	  Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
	}
  }

  /**
   * Método utilizado para ver cuándo abrir una nueva pestaña y cuándo abrir un archivo existente
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
	} else {
	  int fileId = fileList.get(0).getIdArchivo();
	  String name = fileList.get(0).getNombre();
	  String content = fileList.get(0).getContenido();
	  addTab(name, content, fileId);
	}
  }

  /**
   * Método que carga la ventana IU_Editor.fxml, recibe el proyecto seleccionado
   *
   * @param selectedProject
   * @param fileExplorerStage
   * @param rb
   * @param user
   */
  public void openEditor(Project selectedProject, Stage fileExplorerStage, ResourceBundle rb, User user) {
	try {
	  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_Editor.fxml"), rb);

	  IU_EditorController controller = new IU_EditorController();
	  fxmlLoader.setController(controller);
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
    SocketFile socketFile = new SocketFile();
    socketFile.loadFiles(selectedProject.getIdProyecto());

    socket.on("filesRecovered", (Object... os) -> {
      List<File> aux;
      
      JSONArray receivedList = (JSONArray) os[1];
      String jsonString = receivedList.toString();
      
      Gson gson = new Gson();

      File[] jsonFileList = gson.fromJson(jsonString, File[].class);
      aux = Arrays.asList(jsonFileList);
      fileList = new ArrayList<>(aux);
    });
  }

  /**
   * Actualiza la lista de archivos del proyecto.
   */
  public void reloadFiles() {
    SocketFile socketFile = new SocketFile();
    socketFile.reloadFiles(selectedProject.getIdProyecto());

    socket.on("filesReloaded", (Object... os) -> {
      List<File> aux;
      
      JSONArray receivedList = (JSONArray) os[1];
      String jsonString = receivedList.toString();
      
      Gson gson = new Gson();
      
      File[] jsonFileList = gson.fromJson(jsonString, File[].class);
      aux = Arrays.asList(jsonFileList);
      
      fileList.clear();
      fileList = new ArrayList<>(aux);
    });
  }

  /**
   * Método que suscribe al usuario con el id del proyecto en el room correspondiente.
   */
  public void joinProjectRoom() {
    SocketProject socketProject = new SocketProject();
    socketProject.joinProjectRoom(selectedProject.getIdProyecto(), user.getIdUsuario());
    
    socket.on("connectedToProjectRoom", (Object... os) -> {
      JSONArray receivedList = (JSONArray) os[0];
      String jsonString = receivedList.toString();
      
      Gson gson = new Gson();
      
      Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
      collaboratorsConnected = Arrays.asList(jsonFileList);
      collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
      
      Platform.runLater(() -> {
        collaboratorsButtons.clear();
        setCollaboratorsIcons();
        menuItemsSelectedAction();
      });
    });
  }

  /**
   * Método que cambia el atributo "connected" de los colaboradores de la segunda lista recibida
   * como parametro a verdadero, en base a los colaboradores del primera lista.
   * @param collaboratorsConnected
   * @param collaboratorsList
   * @return 
   */
  public List<Collaborator> markCollaboratorsAsConnected(List<Collaborator> collaboratorsConnected, 
      List<Collaborator> collaboratorsList) {

    for (int i = 0; i < collaboratorsList.size(); i++) {
      collaboratorsList.get(i).setConnected(false);
    }
    for (int i = 0; i < collaboratorsList.size(); i++) {
      for (int j = 0; j < collaboratorsConnected.size(); j++) {
        if (collaboratorsList.get(i).getIdUsuario() == collaboratorsConnected.get(j).getIdUsuario()) {
          collaboratorsList.get(i).setConnected(true);
        }
      }
    }
    return collaboratorsList;
  }
  
  /**
   * Método que cambia el atributo "connected" de los colaboradores de la segunda lista recibida
   * como parametro a falso, en base a los colaboradores del primera lista.
   * @param collaboratorsConnected
   * @param collaboratorsList
   * @return 
   */
  public List<Collaborator> markCollaboratorsAsDisconnected(List<Collaborator> collaboratorsConnected, 
      List<Collaborator> collaboratorsList){
    for (int i = 0; i < collaboratorsConnected.size(); i++) {
      for (int j = 0; j < collaboratorsList.size(); j++) {
        if (collaboratorsList.get(j).getIdUsuario() == collaboratorsConnected.get(i).getIdUsuario()) {
          collaboratorsList.get(j).setConnected(false);
        }
      }
    }
    return collaboratorsList;
  }
  
  /**
   * Recupera todos los colaboradores relacionados al proyecto. 
   */
  public void loadCollaborators() {
    SocketCollaborator socketCollaborator = new SocketCollaborator();
    socketCollaborator.loadCollaborators(selectedProject.getIdProyecto());
    
    socket.on("collaboratorsRecovered", (Object... os) -> {
      List<Collaborator> aux = new ArrayList<>();
      JSONArray receivedList = (JSONArray) os[0];
      String jsonString = receivedList.toString();
      
      Gson gson = new Gson();
      
      Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
      aux = Arrays.asList(jsonFileList);
      collaboratorsList = new ArrayList<>(aux);
      
      Platform.runLater(() -> {
        collaboratorsButtons.clear();
        setCollaboratorsIcons();
        menuItemsSelectedAction();
      });
    });
  }

  /**
   * Metodo que esta a la escucha de cualquier evento recibido del servidor.
   */
  public void listenServer() {
    socket.on("fileSaved", (Object... os) -> {
      Platform.runLater(() -> {
        String tabName = (String) os[1];
        int fileID = (int) os[0];
        
        addTab(tabName, "", fileID);
        reloadFiles();
      });
    }).on("disconnectedFromProjectRoom", (Object... os) -> {
      JSONArray receivedList = (JSONArray) os[0];
      String jsonString = receivedList.toString();
      
      Gson gson = new Gson();
      
      Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
      collaboratorsConnected = Arrays.asList(jsonFileList);
      collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
      
      Platform.runLater(() -> {
        collaboratorsButtons.clear();
        setCollaboratorsIcons();
        menuItemsSelectedAction();
      });
    }).on("fileDeleted", (Object... os) -> {
      reloadFiles();
    }).on("collaborationDeleted", (Object... os) -> {
      Platform.runLater(() -> {
        loadCollaborators();
        int deletedUserID = (int) os[0];
        
        if (deletedUserID == user.getIdUsuario()) {
          String intStringYourCollaborationDeleted = rb.getString("intStringYourCollaborationDeleted");
          Tools.displayWarningAlert(intStringYourCollaborationDeleted, rb);
        }
        
        collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
        collaboratorsButtons.clear();
        setCollaboratorsIcons();
        menuItemsSelectedAction();
      });
    }).on("collaborationSaved", (Object... os) -> {
      loadCollaborators();
      Platform.runLater(() -> {
        collaboratorsButtons.clear();
        
        collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
        
        setCollaboratorsIcons();
        menuItemsSelectedAction();
      });
    });
  }
  
  @FXML
  void runCompiler(MouseEvent event) {
	JSONObject projectToSend = new JSONObject();
	projectToSend.accumulate("projectID", selectedProject.getIdProyecto());
	projectToSend.accumulate("language", selectedProject.getLenguaje());

	socket.emit("runCompiler", projectToSend);
	openConsole();
  }
  
  @FXML
  void runProgram(MouseEvent event) {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/GUIRunProject.fxml"), rb);
	GUIRunProjectController controller = new GUIRunProjectController();
	loader.setController(controller);
	controller.setFileList(fileList);
	controller.setParent(this);
	controller.setProjectToRun(selectedProject);
	mainStage = (Stage) tabPaneFiles.getScene().getWindow();
	controller.openRunProject(mainStage, rb, loader);
  }
  
  public void openConsole(){
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/GUIConsole.fxml"), rb);
	GUIConsoleController controller = new GUIConsoleController();
	loader.setController(controller);
	mainStage = (Stage) tabPaneFiles.getScene().getWindow();
	controller.openConsole(mainStage, rb);
  }
}

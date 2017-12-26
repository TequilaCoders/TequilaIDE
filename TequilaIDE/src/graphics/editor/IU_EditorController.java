package graphics.editor;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import graphics.explorer.IU_FileExplorerController;
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
  private MenuItem menuItemOptions;
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
  private TabPane tabPaneArchivos;
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

  private int projectID;

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
	hamburgerTransition = new HamburgerBackArrowBasicTransition(hamburgerButton);
	hamburgerTransition.setRate(-1);
	this.rb = rb;
	setProjectID();
	loadFiles();
	this.projectID = selectedProject.getIdProyecto();

	if (!selectedProject.isShared()) {
	  setUserIcons();
	}

	Platform.runLater(() -> {
      showFirstTab(checkNumberOfFiles());
      loadCollaborators();
      
      Platform.runLater(() -> {
        joinProjectRoom();
      });
    });
	
	drawerFileTree.setOnDrawerClosed(event -> {
	  drawerFileTree.toBack();
	});

    menuButtonUser.setOnMouseEntered((e -> imageVUser.setImage(new Image("/resources/icons/user_yellow.png"))));
    menuButtonUser.setOnMouseExited((e -> imageVUser.setImage(new Image("/resources/icons/user_white.png"))));
	
	if (selectedProject.getLenguaje().equals("py")) {
	  //buttonCompile.setDisable(true);
	  //buttonCompile.setOpacity(0.50);
	}
    listenServer();
  }

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
    SocketFile socketFile = new SocketFile();
    System.out.println("dejando el room");
    leaveProjectRoom();

    for (int i = 0; i < fileList.size(); i++) {
      socketFile.leaveFilesRoom(fileList.get(i).getIdArchivo());
    }
    mainStage = (Stage) tabPaneArchivos.getScene().getWindow();
    IU_FileExplorerController explorador = new IU_FileExplorerController();
    explorador.open_FileExplorer(mainStage, rb, user);
  }

  /**
   * Despliega el icono del usuario creador del proyecto.
   */
  public void setUserIcons() {
    String alias = user.getAlias();
    Tooltip tootTip = new Tooltip();

    tootTip.setText(alias);
    menuButtonUser.setTooltip(tootTip);

    menuItemCollaboratorInformation.setText(alias + " (tu)");
    
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

  /**
   * Agrega listeners que se activan cuando el cursor se posiciona en los botones de los colaboradores.
   */
  public void hoverListeners() {
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
          socketCollaborator.deleteCollaborator(collaboratorID, projectID);
          Tools.displayInformation("Colaborador Eliminado", "El colaborador ha sido eliminado");
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
    FXMLLoader loader = new FXMLLoader(getClass().getResource("IU_AddCollaborator.fxml"));
    Stage stageAddCollaborator;
    IU_AddCollaboratorController controller = new IU_AddCollaboratorController();
    controller.setProjectID(projectID);
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
	  socketFile.createNewFile(className, "", projectID, selectedProject.getLenguaje());
	}
  }

  /**
   * Metodo sobrecargado para crear la primera pestaña de un proyecto que no tiene archivos.
   */
  @FXML
  void addTab() {
	String className = selectedProject.getNombre();
	System.out.println("El nombre del proyecto seleccionado es: "+className);
	SocketFile socketFile = new SocketFile();
	socketFile.createNewFile(className, "", projectID, selectedProject.getLenguaje());
	
	socket.on("fileSaved", (Object... os) -> {
      Platform.runLater(()->{
        try {
          Tab tab = new Tab();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_Tab.fxml"), rb);
          IU_TabController controller = new IU_TabController();
          loader.setController(controller);
          controller.setTab(tab);
          controller.setFileList(fileList);
          controller.setProjectID(projectID);
          
          ScrollPane newFile = loader.load();
          
          tab.setContent(newFile);
          tab.setText(selectedProject.getNombre() + "." + selectedProject.getLenguaje());
          tabPaneArchivos.getTabs().add(tab);
          tabPaneArchivos.getSelectionModel().selectLast();
          int fileID = (int) os[0];
          controller.setFile(fileID);
          loadFiles();
        } catch (IOException ex) {
          Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
      });
    });
  }

  /**
   * Metodo sobrecargado para crear la primera pestaña de un proyecto que tiene archivos.
   */
  @FXML
  void addTab(String title, String content, int fileId) {
    SocketFile socketFile = new SocketFile();
    
    try {
      Tab tab = new Tab();
      tab.setText(title + "." + selectedProject.getLenguaje());
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_Tab.fxml"), rb);

      IU_TabController controller = new IU_TabController();
      loader.setController(controller);
      controller.setTab(tab);
      controller.setFile(fileId);
      controller.setFileList(fileList);
      controller.setProjectID(projectID);

      ScrollPane newFile = loader.load();
      tab.setContent(newFile);
      socketFile.joinFilesRoom(fileId);
      controller.setContent(content);

      tabPaneArchivos.getTabs().add(tab);
      tabPaneArchivos.getSelectionModel().selectLast();
      currentTabs.add(title);

      tab.setOnSelectionChanged((Event t) -> {
        if (tab.isSelected()) {
          loadFiles();
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
      Platform.runLater(() -> {
        addTab();
      });
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
  public void open_Editor(Project selectedProject, Stage fileExplorerStage, ResourceBundle rb, User user) {
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
    socketFile.loadFiles(projectID);

    socket.on("filesRecovered", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        List<File> aux = new ArrayList<>();
        
        JSONArray receivedList = (JSONArray) os[1];
        String jsonString = receivedList.toString();
        
        Gson gson = new Gson();
        
        File[] jsonFileList = gson.fromJson(jsonString, File[].class);
        aux = Arrays.asList(jsonFileList);
        fileList = new ArrayList<>(aux);
      }
    });
  }

  /**
   * Método que suscribe al usuario con el id del proyecto en el room correspondiente.
   */
  public void joinProjectRoom() {
    JSONObject membershipToSend = new JSONObject();
    membershipToSend.accumulate("projectID", projectID);
    membershipToSend.accumulate("userID", user.getIdUsuario());

    socket.emit("joinProjectRoom", membershipToSend);
    
    socket.on("connectToProjectRoom", (Object... os) -> {
      JSONArray receivedList = (JSONArray) os[0];
      String jsonString = receivedList.toString();
      
      Gson gson = new Gson();
      
      Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
      collaboratorsConnected = Arrays.asList(jsonFileList);
      collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
      
      Platform.runLater(() -> {
        collaboratorsButtons.clear();
        setCollaboratorsIcons();
        hoverListeners();
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
   * Método usado cuando un usuario cierra el proyecto, el room correspondiente se da de baja.
   */
  public void leaveProjectRoom(){
    JSONObject membershipToSend = new JSONObject();
    membershipToSend.accumulate("projectID", projectID);
    membershipToSend.accumulate("userID", user.getIdUsuario());
    
    socket.emit("leaveProjectRoom", membershipToSend);
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
    socketCollaborator.loadCollaborators(projectID);
    
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
        hoverListeners();
        menuItemsSelectedAction();
      });
    });
  }

  public void listenServer() {
	socket.on("fileSaved", new Emitter.Listener() {
	  @Override
	  public void call(Object... os) {
		Platform.runLater(() -> {
		  try {
			Tab tab = new Tab();
			tab.setText(os[1] + "." + selectedProject.getLenguaje());
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_Tab.fxml"), rb);
			IU_TabController controller = new IU_TabController();
			loader.setController(controller);
			controller.setTab(tab);
			controller.setFileList(fileList);
			controller.setFile((int) os[0]);
			ScrollPane newFile = loader.load();
			tab.setContent(newFile);
			tabPaneArchivos.getTabs().add(tab);
			tabPaneArchivos.getSelectionModel().selectLast();
			loadFiles();
		  } catch (IOException ex) {
			Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
		  }
		});
	  }
	});

	socket.on("disconnectFromRoom", new Emitter.Listener() {
	  @Override
	  public void call(Object... os) {

		JSONArray receivedList = (JSONArray) os[0];
		String jsonString = receivedList.toString();

		Gson gson = new Gson();

		Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
		System.out.println("NUEVA lista recuperada de colaboradores: (un usuario se desconecto) " + jsonString);
		collaboratorsConnected = Arrays.asList(jsonFileList);
		collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);

		Platform.runLater(new Runnable() {
		  @Override
		  public void run() {
			System.out.println("SE ACTIVA LA RECARGA DE ICONOS DE COLABORADORES");
			collaboratorsButtons.clear();
			setCollaboratorsIcons();
			hoverListeners();
			menuItemsSelectedAction();
		  }

		});
	  }
	});
	
	socket.on("fileDeleted", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        System.out.println("file deleted");
		Platform.runLater(()->{
		  loadFiles();
		});
      }
    });

    socket.on("collaborationDeleted", (Object... os) -> {
      Platform.runLater(() -> {
        loadCollaborators();
 
        //deleteCollaboratorFromCollaboratorsList((int) os[0]);
   
        collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
        
        collaboratorsButtons.clear();
        setCollaboratorsIcons();
        hoverListeners();
        menuItemsSelectedAction();
      });
    });
    
    socket.on("collaborationSaved", (Object... os) -> {
      loadCollaborators();
      Platform.runLater(() -> {
        collaboratorsButtons.clear();
        
        collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
        
        setCollaboratorsIcons();
        hoverListeners();
        menuItemsSelectedAction();
      });
    });
	
	
  }
  
  public void deleteCollaboratorFromCollaboratorsList(int collaboratorID){
    int deleteIndex = 0;
    for (int i = 0; i < collaboratorsList.size(); i++) {
      if (collaboratorsList.get(i).getIdUsuario() == collaboratorID) {
        deleteIndex = i;
        System.out.println("id = "+ collaboratorsList.get(i).getIdUsuario());
      }
    }
    collaboratorsList.remove(deleteIndex);
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
	String mainClass = Tools.displayChoiceDialog("Ejecutar el programa", "Selecciona la clase principal", fileList);
	
	if (!mainClass.equals("")) {
	  JSONObject projectToSend = new JSONObject();
	  projectToSend.accumulate("projectID", selectedProject.getIdProyecto());
	  projectToSend.accumulate("language", selectedProject.getLenguaje());
	  projectToSend.accumulate("mainClass", mainClass);
	 
	  socket.emit("runProgram", projectToSend);

	  //AQUI ABRIR LA TERMINAL 
	  openConsole();
	}
	
  }
  
  public void openConsole(){
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/GUIConsole.fxml"), rb);
	GUIConsoleController controller = new GUIConsoleController();
	loader.setController(controller);
	mainStage = (Stage) tabPaneArchivos.getScene().getWindow();
	controller.openConsole(mainStage, rb);
  }
}

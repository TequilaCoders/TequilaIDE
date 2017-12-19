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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import logic.domain.Collaborator;
import logic.domain.File;
import logic.domain.Project;
import logic.domain.User;
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
  private ImageView paneFileExplorer;
  @FXML
  private ImageView paneFileExplorer1;
  @FXML
  private ImageView paneFileExplorer11;
  @FXML
  private ImageView paneFileExplorer111;
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

	Platform.runLater(new Runnable() {
	  @Override
	  public void run() {
		showFirstTab(checkNumberOfFiles());
		loadCollaborators();

		Platform.runLater(new Runnable() {
		  @Override
		  public void run() {
			joinProjectRoom();
		  }

		});
	  }

	});
	
	drawerFileTree.setOnDrawerClosed(event -> {
	  drawerFileTree.toBack();
	});

	listeners();
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
    System.out.println("dejando el room");
    leaveProjectRoom();
    mainStage = (Stage) tabPaneArchivos.getScene().getWindow();
    IU_FileExplorerController explorador = new IU_FileExplorerController();
    explorador.open_FileExplorer(mainStage, rb, user);
  }

  public void setUserIcons() {
    String alias = user.getAlias();
    Tooltip tootTip = new Tooltip();

    tootTip.setText(alias);
    menuButtonUser.setTooltip(tootTip);

    menuItemCollaboratorInformation.setText(alias + " (tu)");
    
    menuButtonUser.setDisable(false);
  }

  public void setCollaboratorsIcons() {
    for (int i = 0; i < collaboratorsList.size(); i++) {
      Collaborator collaborator = collaboratorsList.get(i);
      String collaboratorAlias = collaborator.getAlias();
      int id = collaborator.getIdUsuario();
      
      ImageView imagev = new ImageView();
      imagev.setFitHeight(30);
      imagev.setFitWidth(29);
      
      System.out.println("estatus en la lista de colaboradores " + collaboratorsList.get(i).isConnected());
      System.out.println("El usuario con el id " + id);
      System.out.println("el colaborador esta conectado? " +collaboratorAlias+" estatus: "+ collaborator.isConnected());
      
      if (collaborator.isConnected()) {
        System.out.println("esta conectado !!!!!!!");
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
        listener_AddCollaboratorWindow_Closed(loader, stageAddCollaborator);
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
        addTab(event);
      }
    }
  }

  /**
   *
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
	
	socket.on("fileSaved", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
		Platform.runLater(()->{
		  try {
			Tab tab = new Tab();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_Tab.fxml"), rb);
			IU_TabController controller = new IU_TabController();
			loader.setController(controller);
			controller.setTab(tab);
			controller.setFileList(fileList);
			
			ScrollPane newFile = loader.load();
			
			tab.setContent(newFile);
			tab.setText(selectedProject.getNombre()+"."+selectedProject.getLenguaje());
			tabPaneArchivos.getTabs().add(tab);
			tabPaneArchivos.getSelectionModel().selectLast();
			controller.setFile((int)os[0]);
			loadFiles();
		  } catch (IOException ex) {
			Logger.getLogger(IU_EditorController.class.getName()).log(Level.SEVERE, null, ex);
		  }
		});
      }
    });
  }

  /**
   * Metodo sobrecargado para crear la primera pestaña de un proyecto que tiene archivos.
   */
  @FXML
  void addTab(String title, String content, int fileId) {
    try {
      Tab tab = new Tab();
      tab.setText(title+"."+selectedProject.getLenguaje());
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/editor/IU_Tab.fxml"), rb);

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
	  Platform.runLater(()->{
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

    JSONObject projectIDToSend = new JSONObject();
    projectIDToSend.accumulate("projectID", projectID);

    //socket.connect();
    socket.emit("loadFiles", projectIDToSend);

    socket.on("filesRecovered", new Emitter.Listener() {
      @Override
      public void call(Object... os) {

        JSONArray receivedList = (JSONArray) os[1];
        String jsonString = receivedList.toString();

        Gson gson = new Gson();

        File[] jsonFileList = gson.fromJson(jsonString, File[].class);
        fileList = Arrays.asList(jsonFileList);
        socket.emit("filesreceived");

      }
    });

    //joinProjectRoom();

  }

  public void joinProjectRoom() {
    JSONObject membershipToSend = new JSONObject();
    membershipToSend.accumulate("projectID", projectID);
    membershipToSend.accumulate("userID", user.getIdUsuario());

    System.out.println("CARGANDO CUARTO--------------------------------- ");
    socket.emit("joinRoom", membershipToSend);
    
    socket.on("connectToRoom", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
  
        JSONArray receivedList = (JSONArray) os[0];
        String jsonString = receivedList.toString();
        
        System.out.println("lista recibida de usuarios en el cuarto " + jsonString);

        Gson gson = new Gson();

        Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
        collaboratorsConnected = Arrays.asList(jsonFileList);
        collaboratorsList = markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
        System.out.print("lista colaboradores conectados ");
        imprimirLista(collaboratorsConnected);
        
        System.out.print("lista colaboradores l¿locales ");
        imprimirLista(collaboratorsList);
        
        
        Platform.runLater(new Runnable() {
          @Override
          public void run() {  
           
            System.out.print("SE ACTIVA LA RECARGA DE ICONOS DE COLABORADORES");
            collaboratorsButtons.clear();
            setCollaboratorsIcons();
            hoverListeners();
            menuItemsSelectedAction();
          }

        });
      }
    });
  }
  
  public List<Collaborator> markCollaboratorsAsConnected(List<Collaborator> collaboratorsConnected, List<Collaborator> collaboratorsList){
 
    /*for (int i = 0; i < collaboratorsConnected.size(); i++) {
      for (int j = 0; j < collaboratorsList.size(); j++) {
        if (collaboratorsList.get(j).getIdUsuario() == collaboratorsConnected.get(i).getIdUsuario()) {
          System.out.println("colaborador match! idUsuario: " + collaboratorsList.get(j).getIdUsuario());
          collaboratorsList.get(j).setConnected(true);
          
          System.out.println("estatus actual " + collaboratorsList.get(j).isConnected());
        } else {
          System.out.println("colaborador NO match! idUsuario: " + collaboratorsList.get(j).getIdUsuario());
           collaboratorsList.get(j).setConnected(false);
        }
      }
    }*/
    
    for (int i = 0; i < collaboratorsList.size(); i++) {
      collaboratorsList.get(i).setConnected(false);
    }
    
    for (int i = 0; i < collaboratorsList.size(); i++) {
      for (int j = 0; j < collaboratorsConnected.size(); j++) {
        if (collaboratorsList.get(i).getIdUsuario() == collaboratorsConnected.get(j).getIdUsuario()) {
          System.out.println("colaborador match! idUsuario: " + collaboratorsList.get(i).getIdUsuario());
          collaboratorsList.get(i).setConnected(true);
        }
      }
    }
   
    return collaboratorsList;
  }
  
  public void imprimirLista(List<Collaborator> lista){
    for (int i = 0; i < lista.size(); i++) {
      System.out.println("elemento de la lista [" + i +"] "+ lista.get(i).getIdUsuario());
    }
  }

  public void leaveProjectRoom(){
    JSONObject membershipToSend = new JSONObject();
    membershipToSend.accumulate("projectID", projectID);
    membershipToSend.accumulate("userID", user.getIdUsuario());
    System.out.println("metodo LeaveRoom");
    socket.emit("leaveRoom", membershipToSend);
  }
  
  public List<Collaborator> markCollaboratorsAsDisconnected(List<Collaborator> collaboratorsConnected, List<Collaborator> collaboratorsList){
    for (int i = 0; i < collaboratorsConnected.size(); i++) {
      for (int j = 0; j < collaboratorsList.size(); j++) {
        if (collaboratorsList.get(j).getIdUsuario() == collaboratorsConnected.get(i).getIdUsuario()) {
          System.out.println("colaborador match! idUsuario: " + collaboratorsList.get(j).getIdUsuario());
          collaboratorsList.get(j).setConnected(false);
          
          System.out.println("estatus actual " + collaboratorsList.get(j).isConnected());
        }
      }
    }
   
    return collaboratorsList;
  }
  
  public void loadCollaborators() {
    JSONObject projectIDToSend = new JSONObject();
    projectIDToSend.accumulate("projectID", projectID);
   
    socket.emit("getCollaborators", projectIDToSend);

    socket.on("collaboratorsRecovered", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        JSONArray receivedList = (JSONArray) os[0];
        String jsonString = receivedList.toString();

        Gson gson = new Gson();

        Collaborator[] jsonFileList = gson.fromJson(jsonString, Collaborator[].class);
        collaboratorsList = Arrays.asList(jsonFileList);

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

    //socket.connect();
    
    socket.emit("deleteCollaborator", collaborationToSend);

    socket.on("collaborationDeleted", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        //socket.close();
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
    JSONObject fileToSend = new JSONObject();
    fileToSend.accumulate("fileID", fileID);
    socket.emit("deleteFile", fileToSend);
  }

  /**
   * Métodos que estan a la escucha de eventos en los elementos paneHamburger y paneFileExplorer.
   */
  public void listeners() {
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
	
	
  }
  
  @FXML
  void runCompiler(MouseEvent event) {
	String mainClass = Tools.displayChoiceDialog("Compilar", "Selecciona la clase principal", fileList);
	if (!mainClass.equals("")) {
	  JSONObject projectToSend = new JSONObject();
	  projectToSend.accumulate("projectID", selectedProject.getIdProyecto());
	  projectToSend.accumulate("language", selectedProject.getLenguaje());
	  projectToSend.accumulate("mainClass", mainClass);

	  socket.emit("runCompiler", projectToSend);
	  openConsole();
	}
  }
  
  @FXML
  void runProgram(MouseEvent event) {
	String mainClass = Tools.displayChoiceDialog("Compilar", "Selecciona la clase principal", fileList);
	
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

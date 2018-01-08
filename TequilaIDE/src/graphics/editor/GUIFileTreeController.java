package graphics.editor;

import com.jfoenix.controls.JFXTreeView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import logic.domain.File;
import logic.domain.Project;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class GUIFileTreeController implements Initializable {

  @FXML
  private JFXTreeView<String> treeViewProjectFiles;

  private Project selectedProject;

  List<File> fileList = new ArrayList<>();

  GUIEditorController editorController;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	createTreeView();
	listenerSelectedFile();
  }

  public void setSelectedProject(Project selectedProject) {
	this.selectedProject = selectedProject;
  }

  public void setFileList(List<File> fileList) {
	this.fileList = fileList;
  }

  public void setEditorController(GUIEditorController editorController) {
	this.editorController = editorController;
  }

  /**
   * Crea una treeview de los archivos contenidos dentro del proyecto seleccionado.
   */
  public void createTreeView() {
	ImageView imageRootNode = new ImageView(new Image("/resources/icons/proyecto.png", 20, 20, false, false));
	String titleValue = selectedProject.getNombre();
	TreeItem<String> root = new TreeItem<>(titleValue, imageRootNode);
	root.setExpanded(true);

	treeViewProjectFiles.setRoot(root);

	for (int i = 0; i < fileList.size(); i++) {
	  ImageView imageLeafNode = new ImageView(new Image("/resources/icons/archivo_seleccioado.png", 20, 20, false, false));
	  String fileTitle = fileList.get(i).getNombre();
	  TreeItem<String> treeItem = new TreeItem<>(fileTitle, imageLeafNode);
	  root.getChildren().add(treeItem);
	}
  }

  /**
   * Esta a la escucha de la selección con doble clic de algún archivo en la treeview.
   */
  public void listenerSelectedFile() {
	treeViewProjectFiles.setOnMouseClicked(new EventHandler<MouseEvent>() {
	  @Override
	  public void handle(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 2 && !treeViewProjectFiles.getSelectionModel().isEmpty()) {
		  TreeItem<String> item = treeViewProjectFiles.getSelectionModel().getSelectedItem();

		  String fileName = item.getValue();
		  File selectedFile = searchFileByName(fileName);

		  String name = selectedFile.getNombre();
		  String content = selectedFile.getContenido();
		  int fileId = selectedFile.getIdArchivo();

		  if (!tabIsOpened(name)) {
			editorController.addTab(name, content, fileId);
		  }

		  editorController.openDrawer(mouseEvent);
		}
	  }
	});
  }

  /**
   * Regresa el archivo de la variable fileList cuyo nombre coincida con el argumento de entrada.
   * @param name
   * @return
   */
  public File searchFileByName(String name) {
	File foundFile = null;
	for (int i = 0; i < fileList.size(); i++) {
	  String auxFileName = fileList.get(i).getNombre();
	  if (auxFileName.equals(name)) {
		foundFile = fileList.get(i);
	  }
	}
	return foundFile;
  }

  /**
   * Regresa verdadero si el nombre de la pestaña coincide con una pestaña ya abierta en el editor
   * @param name
   * @return
   */
  public boolean tabIsOpened(String name) {
	boolean status = false;
	List<String> tabNameList = editorController.getCurrentTabs();

	for (int i = 0; i < tabNameList.size(); i++) {
	  String auxName = tabNameList.get(i);
	  if (auxName.equals(name)) {
		status = true;
	  }
	}
	return status;
  }
}

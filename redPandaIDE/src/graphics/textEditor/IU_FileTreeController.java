/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.textEditor;

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
import logic.File;
import logic.Project;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_FileTreeController implements Initializable {

  @FXML
  private JFXTreeView<String> treeViewProjectFiles;
  
  private Project selectedProject;
  
  List<File> fileList = new ArrayList<>();
  
  IU_EditorController editorController;

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

  public void setEditorController(IU_EditorController editorController) {
    this.editorController = editorController;
  }
  
  /**
   * Metodo que crea una treeview de los archivos contenidos dentro del proyecto seleccionado.
   */
  public void createTreeView() {
    
    //se crea el nodo raíz
    ImageView imageRootNode = new ImageView(new Image("/resources/icons/proyecto.png", 20, 20, false, false));
    String titleValue = selectedProject.getNombre();
    TreeItem<String> root = new TreeItem<>(titleValue, imageRootNode);
    root.setExpanded(true);

    treeViewProjectFiles.setRoot(root); 

    //se crean los nodos hoja
    for (int i = 0; i < fileList.size(); i++) {
      ImageView imageLeafNode = new ImageView(new Image("/resources/icons/archivo_seleccioado.png", 20, 20, false, false));
      String fileTitle = fileList.get(i).getNombre();
      TreeItem<String> treeItem = new TreeItem<>(fileTitle, imageLeafNode);
      root.getChildren().add(treeItem);
    }
  }
  
  /**
   * Metodo que esta a la escucha de la selección con doble clic de algún archivo en la treeview.
   */
  public void listenerSelectedFile() {
    
    //Listener for item selected in treeView
    treeViewProjectFiles.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
          TreeItem<String> item = treeViewProjectFiles.getSelectionModel().getSelectedItem();

          System.out.println("item value " + item.getValue());
          String fileName = item.getValue();
          //se busca el proyecto por su nombre
          File selectedFile = searchFileByName(fileName);
          
          String name = selectedFile.getNombre();
          String content = selectedFile.getContenido();
          int fileId = selectedFile.getIdArchivo();

          //se comprueba si la pestaña esta abierta en el editor
          if (tabIsOpened(name) == false) {
            // Se crea una nueva pestaña
            editorController.addTab(name, content, fileId);
          }

          //se cierra el drawer
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
  public File searchFileByName(String name){
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
  public boolean tabIsOpened(String name){
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
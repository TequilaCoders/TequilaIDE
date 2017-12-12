/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.fileExplorer;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import graphics.login.IU_LogInController;
import static graphics.login.IU_LogInController.socket;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.domainClasses.Project;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_NewProjectController implements Initializable {

  @FXML
  private Pane paneNewProject;

  @FXML
  private JFXComboBox cbLanguages;

  @FXML
  private JFXButton buttonCancel;

  @FXML
  private JFXTextField tfName;
  
  private boolean estatusDeGuardado;
  
  private int idProject;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    estatusDeGuardado = false;
    loadComboBoxProgramingLanguages();
  }

  public boolean getEstatusDeGuardado() {
    return estatusDeGuardado;
  }
  
  public int getIdProject(){
    return idProject;
  }

  /**
   * Metodo llamado al accionar el elemento buttonCancel
   * @param event 
   */
  @FXML
  void closeWindow(ActionEvent event) {
    Stage stage = (Stage) buttonCancel.getScene().getWindow();
    stage.close();
  }

  /**
   * Método que carga el elemento cbLanguages con los lenguajes de programación del IDE.
   */
  public void loadComboBoxProgramingLanguages() {
    String[] programingLanguagesList = {"Java", "Python", "C++"};

    ObservableList<String> programingLanguages = FXCollections.observableArrayList(
        programingLanguagesList);

    cbLanguages.setItems(programingLanguages);
  }

  @FXML
  void createProject(ActionEvent event) {
    estatusDeGuardado = true;
    String name;
    String programmingLanguage;

    name = tfName.getText();
    programmingLanguage = (String) cbLanguages.getSelectionModel().getSelectedItem();
    
    JSONObject projectToSend = new JSONObject();
    
    projectToSend.accumulate("name", name);
    projectToSend.accumulate("programmingLanguage", programmingLanguage);
    
    //----------VALOR TEMPORAL------------------
    projectToSend.accumulate("userID", "1");
    //-------------------------------------------

    socket.connect();
    System.err.println("logrado");

    socket.emit("saveProject", projectToSend);
    
    socket.on("projectSaved", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        if ((boolean) os[0]) {
          System.out.println("proyecto guardado exitosamente -- ");
          idProject = (int) os[1];
          
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              Stage stage = (Stage) paneNewProject.getScene().getWindow();
              stage.close();
              socket.disconnect();
            }
          });
        } else {
          System.out.println((String) os[1]);
          socket.disconnect();
        }
      }
    });

  }
}

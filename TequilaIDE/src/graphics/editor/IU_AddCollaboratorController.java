package graphics.editor;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import static graphics.login.IU_LogInController.socket;
import graphics.tools.Tools;
import io.socket.emitter.Emitter;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import logic.domain.Collaborator;
import logic.domain.User;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_AddCollaboratorController implements Initializable {

  @FXML
  private JFXTextField tfSearch;

  @FXML
  private Label labelAlias;

  @FXML
  private JFXTextArea taBiography;

  @FXML
  private Label labelError;

  @FXML
  private JFXButton buttonAddCollaborator;

  User collaboratorToSave;

  int projectID;
  
  private boolean savedStatus;
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    savedStatus = false;
  }  

  public void setProjectID(int projectID) {
    this.projectID = projectID;
  }

  public boolean isSavedStatus() {
    return savedStatus;
  }

  public User getCollaboratorToSave() {
    return collaboratorToSave;
  }
 
  /**
   * Metodo llamado al accionar el elemento buttonCancel
   *
   * @param event
   */
  @FXML
  void closeWindow(ActionEvent event) {
    Stage stage = (Stage) tfSearch.getScene().getWindow();
    stage.close();
  }

  @FXML
  void searchListener(KeyEvent event) {
    String searchCriteria = tfSearch.getText() + event.getText();
    System.out.println(tfSearch.getText() + event.getText());
    searchCollaborator(searchCriteria);
  }

  public void searchCollaborator(String searchCriteria) {

    JSONObject searchCriteriaToSend = new JSONObject();
    searchCriteriaToSend.accumulate("searchCriteria", searchCriteria);

    //socket.connect();

    socket.emit("searchUser", searchCriteriaToSend);

    socket.on("searchFinalized", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        if ((boolean) os[0]) {
          System.out.println("resultado " + Arrays.toString(os));

          JSONArray listRecovered;
          JSONObject objectRecovered;
          String jsonString;
          User receivedUser;

          listRecovered = (JSONArray) os[1];
          objectRecovered = listRecovered.getJSONObject(0);
          jsonString = objectRecovered.toString();

          Gson gson = new Gson();

          receivedUser = gson.fromJson(jsonString, User.class);
          System.out.println(receivedUser.getAlias());
          //socket.disconnect();
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
              loadInformation(receivedUser);
            }

          });

        } else {
          Platform.runLater(new Runnable() {
            @Override
            public void run() {
             loadNoMatchMessage();
            }

          });
          System.out.println("no se encontraron resultado");
          
          //socket.disconnect();
        }

      }

    });
  }
  
  public void loadInformation(User userFound){
    String name = userFound.getAlias();
    System.out.println("id usuario : " + userFound.getIdUsuario());
    String biography = userFound.getBiografia();
    
    labelAlias.setText(name);
    taBiography.setText(biography);
    labelError.setText("");
    buttonAddCollaborator.setDisable(false);
    collaboratorToSave = userFound;
  }
  
  public void loadNoMatchMessage(){
    String noMatchMessage = "No hay coincidencias";
    labelError.setText(noMatchMessage);
    labelAlias.setText("");
    taBiography.setText("");
    buttonAddCollaborator.setDisable(true);
  }

  @FXML
  void addCollaborator(ActionEvent event) {
    int collaboratorID = collaboratorToSave.getIdUsuario();
    
    JSONObject collaborationToSend = new JSONObject();
    collaborationToSend.accumulate("collaboratorID", collaboratorID);
    collaborationToSend.accumulate("projectID", projectID);

    System.out.println(collaborationToSend);
    //socket.connect();

    socket.emit("saveCollaborator", collaborationToSend);
    
    socket.on("collaborationSaved", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        System.out.println("entro a guardar colaborador");
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            //socket.close();
            savedStatus = true;

            Stage stage = (Stage) labelAlias.getScene().getWindow();
            stage.close();
            
            Tools.displayInformation("Guardado", "Colaboración guardada exitosamente!");
          }

        });

      }

    });
  }
}

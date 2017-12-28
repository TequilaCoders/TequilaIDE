package graphics.editor;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import graphics.tools.Tools;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import logic.domain.Collaborator;
import logic.domain.User;
import logic.sockets.SocketCollaborator;
import org.json.JSONArray;
import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

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

  List<Collaborator> collaboratorsList = new ArrayList<>();
  
  private User user;
  
  private ResourceBundle rb;

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
    savedStatus = false;
  }

  public void setCollaboratorsList(List<Collaborator> collaboratorsList) {
    this.collaboratorsList = collaboratorsList;
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

  public void setUser(User user) {
    this.user = user;
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
    SocketCollaborator socketCollaborator = new SocketCollaborator();
    socketCollaborator.searchCollaborator(searchCriteria);

    socket.on("searchFinalized", (Object... os) -> {
      if ((boolean) os[0]) {
  
        JSONArray listRecovered;
        JSONObject objectRecovered;
        String jsonString;
        User receivedUser;

        listRecovered = (JSONArray) os[1];
        objectRecovered = listRecovered.getJSONObject(0);
        jsonString = objectRecovered.toString();

        Gson gson = new Gson();

        receivedUser = gson.fromJson(jsonString, User.class);
        Platform.runLater(() -> {
          loadInformation(receivedUser);
        });

      } else {
        Platform.runLater(() -> {
          loadNoMatchMessage();
        });
      }
    });
  }

  public void loadInformation(User userFound) {
    String name = userFound.getAlias();
    String biography = userFound.getBiografia();

    labelAlias.setText(name);
    taBiography.setText(biography);
    labelError.setText("");
    buttonAddCollaborator.setDisable(false);
    collaboratorToSave = userFound;
  }

  public void loadNoMatchMessage() {
    String noMatchMessage = "No hay coincidencias";
    labelError.setText(noMatchMessage);
    labelAlias.setText("");
    taBiography.setText("");
    buttonAddCollaborator.setDisable(true);
  }

  @FXML
  void addCollaborator(ActionEvent event) {
    int collaboratorID = collaboratorToSave.getIdUsuario();

    boolean isCollaboratorRepeated = searchCollaboratorInList(collaboratorID, collaboratorsList);
    boolean isCollaboratorAndUserTheSame = isCollaboratorAndUserTheSame(collaboratorID);

    if (isCollaboratorRepeated) {
      String intStringRepeatedCollaborator = rb.getString("repeatedCollaborator");
      Tools.displayWarningAlert(intStringRepeatedCollaborator, rb);
    } else if (isCollaboratorAndUserTheSame) {
      String intStringCollaboratorAndUserSame = rb.getString("collaboratorAndUserSame");
      Tools.displayWarningAlert(intStringCollaboratorAndUserSame, rb);
    } else {
      SocketCollaborator socketCollaborator = new SocketCollaborator();
      socketCollaborator.addCollaborator(collaboratorID, projectID);

      socket.on("collaborationSaved", (Object... os) -> {
        Platform.runLater(() -> {
          savedStatus = true;

          Stage stage = (Stage) labelAlias.getScene().getWindow();
          stage.close();

          Tools.displayInformation("Guardado", "Colaboración guardada exitosamente!");
        });
      });
    }

  }
  
  private boolean searchCollaboratorInList(int collaboratorID, List<Collaborator> collaboratorsList){
    boolean flag = false;
    for (int i = 0; i < collaboratorsList.size(); i++) {
      if (collaboratorsList.get(i).getIdUsuario() == collaboratorID) {
        flag = true;
      }
    }
    return flag;
  }
  
  public boolean isCollaboratorAndUserTheSame(int collaboratorID){
    boolean flag = false;
    if (user.getIdUsuario() == collaboratorID) {
      flag = true;
    }
    return flag;
  }
}

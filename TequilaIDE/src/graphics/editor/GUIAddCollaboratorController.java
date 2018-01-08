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
public class GUIAddCollaboratorController implements Initializable {

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
   * @param event
   */
  @FXML
  void closeWindow(ActionEvent event) {
    Stage stage = (Stage) tfSearch.getScene().getWindow();
    stage.close();
  }

  /**
   * Esta a la escucha de una tecla presionada en el textfield tfSearch
   * @param event 
   */
  @FXML
  void searchListener(KeyEvent event) {
    String searchCriteria = tfSearch.getText() + event.getText();
    searchCollaborator(searchCriteria);
  }

  /**
   * Busca a un colaborador en el servidor que coincida con el criterio de busqueda recibido
   * @param searchCriteria 
   */
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

  /**
   * Muestra la biografia del usuario recibido
   * @param userFound 
   */
  public void loadInformation(User userFound) {
    String name = userFound.getAlias();
    String biography = userFound.getBiografia();

    labelAlias.setText(name);
    taBiography.setText(biography);
    labelError.setText("");
    buttonAddCollaborator.setDisable(false);
    collaboratorToSave = userFound;
  }

  /**
   * Muestra un mensaje de no coinidencias.
   */
  public void loadNoMatchMessage() {
    String intStringNoMatchMessage = rb.getString("NoMatchMessage");
    labelError.setText(intStringNoMatchMessage);
    labelAlias.setText("");
    taBiography.setText("");
    buttonAddCollaborator.setDisable(true);
  }

  /**
   * Agrega un colaborador al sistema, verifica si este no esta repetido, si no es el mismo usuario
   * creador del proyecto y si no se ha sobrepasado la cantidad maxima de colaboradores en el sistema.
   * @param event 
   */
  @FXML
  void addCollaborator(ActionEvent event) {
    int collaboratorID = collaboratorToSave.getIdUsuario();

    boolean isCollaboratorRepeated = searchCollaboratorInList(collaboratorID, collaboratorsList);
    boolean isCollaboratorAndUserTheSame = isCollaboratorAndUserTheSame(collaboratorID);
    boolean isCollaboratorLimitSurpassed = checkCollaboratorLimits();

    if (isCollaboratorRepeated) {
      String intStringRepeatedCollaborator = rb.getString("repeatedCollaborator");
      Tools.displayWarningAlert(intStringRepeatedCollaborator, rb);
    } else if (isCollaboratorAndUserTheSame) {
      String intStringCollaboratorAndUserSame = rb.getString("collaboratorAndUserSame");
      Tools.displayWarningAlert(intStringCollaboratorAndUserSame, rb);
    } else if (isCollaboratorLimitSurpassed) {
      String intStringCollaboratorSurpassed = rb.getString("collaboratorSurpassed");
      Tools.displayWarningAlert(intStringCollaboratorSurpassed, rb);
    } else {
      SocketCollaborator socketCollaborator = new SocketCollaborator();
      socketCollaborator.addCollaborator(collaboratorID, projectID);

      socket.on("collaborationSaved", (Object... os) -> {
        Platform.runLater(() -> {
          savedStatus = true;

          Stage stage = (Stage) labelAlias.getScene().getWindow();
          stage.close();
         
        });
      });
    }
  }

  /**
   * Busca a un colaborador en una lista de colaborades, regresa el colaborador que coincida con el 
   * id recibido.
   * @param collaboratorID
   * @param collaboratorsList
   * @return 
   */
  public boolean searchCollaboratorInList(int collaboratorID, List<Collaborator> collaboratorsList){
    boolean flag = false;
    for (int i = 0; i < collaboratorsList.size(); i++) {
      if (collaboratorsList.get(i).getIdUsuario() == collaboratorID) {
        flag = true;
      }
    }
    return flag;
  }
  
  /**
   * Verifica que el id del colaborador a agregar y y el id del usuario no sean iguales.
   * @param collaboratorID
   * @return 
   */
  public boolean isCollaboratorAndUserTheSame(int collaboratorID){
    boolean flag = false;
    if (user.getIdUsuario() == collaboratorID) {
      flag = true;
    }
    return flag;
  }
  
  /**
   * Verifica si se ha alcanzado el limite de colaboradores, regresa true si es asi.
   * @return 
   */
  public boolean checkCollaboratorLimits(){
    boolean flag = false;
    if (collaboratorsList.size() > 4) {
      flag = true;
    }
    return flag;
  }
}

package graphics.editor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import logic.sockets.SocketFile;
import org.fxmisc.richtext.CodeArea;
import static tequilaide.TequilaIDE.socket;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class GUITabController implements Initializable {
  @FXML
  private BorderPane borderPane;
  @FXML
  private ScrollPane scrollPane;
  @FXML
  private VBox vboxNumberLines;
  @FXML
  private CodeArea taEditor;

  String content;

  ContextMenu contextMenu = new ContextMenu();
  
  Tab tab;

  int fileID;

  private int projectID;
  
  private int caretPosition;

  private static final String NUMBER_LINES_FORMAT = "-fx-font-size: 20px; -fx-font-family: \"Courier New\"; -fx-text-fill: #90908A;";

  public void setTab(Tab tab) {
	this.tab = tab;
  }

  public void setProjectID(int projectID) {
	this.projectID = projectID;
  }

  public void setFile(int fileID) {
	this.fileID = fileID;
  }

  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	borderPane.prefHeightProperty().bind(scrollPane.heightProperty());
	borderPane.prefWidthProperty().bind(scrollPane.widthProperty());

	scrollPane.vvalueProperty().bind(vboxNumberLines.heightProperty());
	scrollPane.vvalueProperty().bind(taEditor.heightProperty());

	Label numberLine = new Label("1");
	numberLine.setStyle(NUMBER_LINES_FORMAT);
	vboxNumberLines.getChildren().add(numberLine);
	refreshCodeArea();
  }

  /**
   * Hace visible el numero de lineas en el editor. 
   */
  @FXML
  private void addNumberLines() {
	int currentLines = vboxNumberLines.getChildren().size();
	int numberLines = taEditor.getParagraphs().size();

	if (numberLines > currentLines) {
	  for (int i = currentLines; i < numberLines; i++) {
		Label numberLine = new Label(i + 1 + "");
		numberLine.setStyle(NUMBER_LINES_FORMAT);
		vboxNumberLines.getChildren().add(numberLine);
	  }
	} else if (numberLines < currentLines) {
	  for (int i = currentLines; i > numberLines; i--) {
		vboxNumberLines.getChildren().remove(i - 1);
	  }
	}
  }

  public void setContent(String content) {
	this.content = content;
    taEditor.replaceText("");
    
	taEditor.replaceText(content);
	addNumberLines();
  }

  public String getContent() {
	return taEditor.getText();
  }

  /**
   * Crea una solicitud a un método de la capa lógica que actualiza el archivo actual. 
   * @param event 
   */
  @FXML
  void updateFile(KeyEvent event) {
    int roomNumber = projectID;
	SocketFile socketFile = new SocketFile();
    caretPosition = taEditor.getCaretPosition();
	socketFile.updateFile(taEditor.getText(), fileID, roomNumber);
  }
  
  /**
   * Actualiza el contenido del codeArea mediante la escucha de eventos emitidos por el servidor. 
   */
  public void refreshCodeArea() {
    socket.on("fileUpdated", (Object... os) -> {
      Platform.runLater(() -> {
        int fileIdBroadcasted = (int) os[1];
        if (fileIdBroadcasted == fileID) {
          String newContent = (String) os[0];
          setContent(newContent);
          taEditor.replaceText(newContent);
          addNumberLines();
          if (caretPosition <= taEditor.getLength()) {
            taEditor.moveTo(caretPosition);
          } else {
            taEditor.moveTo(taEditor.getLength());
          }
        }
      });
    });
  }
}

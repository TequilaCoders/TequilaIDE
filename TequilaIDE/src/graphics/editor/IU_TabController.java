package graphics.editor;

import static graphics.login.IU_LogInController.socket;
import graphics.tools.Tools;
import io.socket.emitter.Emitter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import logic.domain.File;
import logic.sockets.SocketFile;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class IU_TabController implements Initializable {

  private static final String[] KEYWORDS = new String[]{
	"abstract", "assert", "boolean", "break", "byte",
	"case", "catch", "char", "class", "const",
	"continue", "default", "do", "double", "else",
	"enum", "extends", "final", "finally", "float",
	"for", "goto", "if", "implements", "import",
	"instanceof", "int", "interface", "long", "native",
	"new", "package", "private", "protected", "public",
	"return", "short", "static", "strictfp", "super",
	"switch", "synchronized", "this", "throw", "throws",
	"transient", "try", "void", "volatile", "while"
  };

  private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
  private static final String PAREN_PATTERN = "\\(|\\)";
  private static final String BRACE_PATTERN = "\\{|\\}";
  private static final String BRACKET_PATTERN = "\\[|\\]";
  private static final String SEMICOLON_PATTERN = "\\;";
  private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
  private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

  private static final Pattern PATTERN = Pattern.compile(
		  "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
		  + "|(?<PAREN>" + PAREN_PATTERN + ")"
		  + "|(?<BRACE>" + BRACE_PATTERN + ")"
		  + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
		  + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
		  + "|(?<STRING>" + STRING_PATTERN + ")"
		  + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
  );

  public void setFileList(List<File> fileList) {
	this.fileList = fileList;
  }

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

  List<File> fileList = new ArrayList<>();

  Tab tab;

  int fileID;

  private int projectID;

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
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
	loadContextMenu();
	borderPane.prefHeightProperty().bind(scrollPane.heightProperty());
	borderPane.prefWidthProperty().bind(scrollPane.widthProperty());

	scrollPane.vvalueProperty().bind(vboxNumberLines.heightProperty());
	scrollPane.vvalueProperty().bind(taEditor.heightProperty());

	Label numberLine = new Label("1");
	numberLine.setStyle(NUMBER_LINES_FORMAT);
	vboxNumberLines.getChildren().add(numberLine);
	refreshCodeArea();
  }

  @FXML
  private void addNumberLines(KeyEvent event) {
	int currentLines = vboxNumberLines.getChildren().size();
	int numberLines = taEditor.getParagraphs().size();

	//autoComplete(event);
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

  /**
   * Metodo sobrecargado para visualizar el numero de lineas desde que se carga el tab
   */
  private void addNumberLines() {
	int currentLines = vboxNumberLines.getChildren().size();
	int numberLines = taEditor.getParagraphs().size();

	//autoComplete(event);
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

  //METODO PENDIENTE
  @FXML
  void braceOpenedListener(KeyEvent event) {
	/*System.out.println(event.getCharacter());
    if (event.getCharacter().equals("a")) {
      System.out.println("se aumento cuenta");
    }*/
  }

  private static StyleSpans<Collection<String>> computeHighlighting(String text) {
	Matcher matcher = PATTERN.matcher(text);
	int lastKwEnd = 0;
	StyleSpansBuilder<Collection<String>> spansBuilder
			= new StyleSpansBuilder<>();
	while (matcher.find()) {
	  String styleClass
			  = matcher.group("KEYWORD") != null ? "keyword"
			  : matcher.group("PAREN") != null ? "paren"
			  : matcher.group("BRACE") != null ? "brace"
			  : matcher.group("BRACKET") != null ? "bracket"
			  : matcher.group("SEMICOLON") != null ? "semicolon"
			  : matcher.group("STRING") != null ? "string"
			  : matcher.group("COMMENT") != null ? "comment"
			  : null;
	  /* never happens */ assert styleClass != null;
	  spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
	  spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
	  lastKwEnd = matcher.end();
	}
	spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
	return spansBuilder.create();
  }

  public void setContent(String content) {
	this.content = content;
	taEditor.replaceText(content);
	addNumberLines();
  }

  public String getContent() {
	return taEditor.getText();
  }

  /**
   * Regresa 0 si no esta duplicado, en caso de estar duplicado regresa el indice de duplicidad.
   *
   * @param title
   * @return
   */
  public int checkDuplicateTitle(String title) {
	int duplicateIndex = 0;

	for (int i = 0; i < fileList.size(); i++) {
	  String fileTitle = fileList.get(i).getNombre();

	  if (fileTitle.equals(title)) {
		String lastCharacter = fileTitle.substring(fileTitle.length() - 1);
		System.out.println("ultimo caracter" + lastCharacter);
		try {
		  duplicateIndex = Integer.parseInt(lastCharacter) + 1;
		} catch (NumberFormatException e) {
		  duplicateIndex = 1;
		}
	  }
	}
	return duplicateIndex;
  }

  public void loadContextMenu() {
	MenuItem menuItemCloseAndSave = new MenuItem("Guardar");
	MenuItem menuItemDelete = new MenuItem("Eliminar");

	contextMenu.getItems().addAll(menuItemCloseAndSave, menuItemDelete);

	menuItemCloseAndSave.setOnAction(new EventHandler() {
	  @Override
	  public void handle(Event event) {
		closeTab(tab);
		tab.getTabPane().getTabs().remove(tab);
		Tools.displayInformation("Confirmation", "Archivo guardado exitosamente!");
	  }

	});

	menuItemDelete.setOnAction(new EventHandler() {
	  @Override
	  public void handle(Event event) {
		tab.getTabPane().getTabs().remove(tab);
		IU_EditorController controller = new IU_EditorController();
		controller.deleteFile(fileID);
		Tools.displayInformation("Archivo Eliminado", "El archivo seleccionado ha sido eliminado");
	  }

	});
  }

  private void closeTab(Tab tab) {
	EventHandler<Event> handler = tab.getOnClosed();
	if (null != handler) {
	  handler.handle(null);
	} else {
	  tab.getTabPane().getTabs().remove(tab);
	}
  }

  @FXML
  void loadContextMenu(ContextMenuEvent event) {
	contextMenu.show(taEditor, event.getScreenX(), event.getScreenY());
	event.consume();
  }

  @FXML
  void hideContextMenu(MouseEvent event) {
	contextMenu.hide();

	IU_EditorController controller = new IU_EditorController();
  }
  
  @FXML
  void updateFile(KeyEvent event) {
	SocketFile socketFile = new SocketFile();
	System.out.println("Actualizando el id: " + fileID);
	socketFile.updateFile(taEditor.getText(), fileID);
  }
  
  public void refreshCodeArea(){
	socket.on("fileUpdated", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
		Platform.runLater(()->{
		  taEditor.replaceText((String) os[0]);
		});
        System.out.println("file succesfully updated");
      }
    });
  }
}

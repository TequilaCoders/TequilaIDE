package graphics.textEditor;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
  
  @FXML
  private BorderPane borderPane;
  @FXML
  private ScrollPane scrollPane;
  @FXML
  private VBox vboxNumberLines;
  @FXML
  private CodeArea taEditor;

  private String content;
  
  private int braceCount;
  
  Tab tab;

  public void setTab(Tab tab) {
    this.tab = tab;
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    borderPane.prefHeightProperty().bind(scrollPane.heightProperty());
    borderPane.prefWidthProperty().bind(scrollPane.widthProperty());

    scrollPane.vvalueProperty().bind(vboxNumberLines.heightProperty());
    scrollPane.vvalueProperty().bind(taEditor.heightProperty());

    Label numberLine = new Label("1");
    numberLine.setStyle("-fx-font-size: 20px; -fx-font-family: \"Courier New\"; -fx-text-fill: #90908A;");
    vboxNumberLines.getChildren().add(numberLine);

    taEditor.richChanges()
        .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
        .subscribe(change -> {
          try {
            taEditor.setStyleSpans(0, computeHighlighting(taEditor.getText()));
          } catch (Exception ex) {

          }
        });
  }

  @FXML
  private void addNumberLines(KeyEvent event) {
    int currentLines = vboxNumberLines.getChildren().size();
    int numberLines = taEditor.getParagraphs().size();
    
    //autoComplete(event);
    if (numberLines > currentLines) {
      for (int i = currentLines; i < numberLines; i++) {
        Label numberLine = new Label(i + 1 + "");
        numberLine.setStyle("-fx-font-size: 20px; -fx-font-family: \"Courier New\"; -fx-text-fill: #90908A;");
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
  private void addNumberLines(){
    int currentLines = vboxNumberLines.getChildren().size();
    int numberLines = taEditor.getParagraphs().size();
    
    //autoComplete(event);
    if (numberLines > currentLines) {
      for (int i = currentLines; i < numberLines; i++) {
        Label numberLine = new Label(i + 1 + "");
        numberLine.setStyle("-fx-font-size: 20px; -fx-font-family: \"Courier New\"; -fx-text-fill: #90908A;");
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
    System.out.println(event.getCharacter());
    if (event.getCharacter().equals("a")) {
      System.out.println("se aumento cuenta");
    }
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

      @FXML
    void setTabTitle(InputMethodEvent event) {
        System.out.println("presionas tecla");
    }
    
  /* Metodo incompleto
  @FXML
  void autoComplete(KeyEvent event) {
    
    if (event.getCharacter().equals("(")) {
      String newText = getContent() + ")";
      setContent(newText);
    } else if (event.getCharacter().equals("[")) {
      String newText = getContent() + "]";
      setContent(newText);
      
    } else if (event.getCharacter().equals("{")) {
      String newText = getContent() + "}";
      setContent(newText);
    }
  }
*/

  public void setContent(String content) {
    taEditor.replaceText(content);
    //se carga el numero de lineas al cargar el tab
    addNumberLines();
  }

  public String getContent() {
    return taEditor.getText();
  }

}

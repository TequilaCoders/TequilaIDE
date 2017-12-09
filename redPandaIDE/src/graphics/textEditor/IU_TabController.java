package graphics.textEditor;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import logic.File;
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
  @FXML
  private JFXComboBox<String> cbTextLanguage;
  
  String content;

  List<File> fileList = new ArrayList<>();

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
    loadComboBoxTextLanguages();

    if (tab.getText().equals("untitled")) {
      listenerSetTabTitle();
    }
  }

  @FXML
  void setLanguageSintax(ActionEvent event) {
    cbTextLanguage = (JFXComboBox<String>) event.getSource();
    String selectedItem = cbTextLanguage.getSelectionModel().getSelectedItem();
    
    switch(selectedItem){
      case "Texto":
        loadPlainText();
        System.out.println("seleccionaste texto");
        break;
      case "Java":
        loadJavaText();
        System.out.println("seleccionaste java");
        break;
      default:
        System.out.println("lenguaje aun no soportado :v ");
        break;
    }
  }

  public void loadPlainText() {
    //String currentText = taEditor.getText().trim();
    taEditor.getStyleSpans(0,taEditor.getLength()).getStyleSpan(0).getStyle().clear();
    //taEditor.replaceText(currentText);
    
  }
  
  public void loadJavaText(){
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
  private void addNumberLines() {
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
    this.content = content;
    taEditor.replaceText(content);
    //se carga el numero de lineas al cargar el tab
    addNumberLines();
  }

  public String getContent() {
    return taEditor.getText();
  }  
  
  public void loadComboBoxTextLanguages() {
    String[] textLanguagesList = {"Texto", "Java", "Python", "C++"};

    ObservableList<String> textLanguages = FXCollections.observableArrayList(
        textLanguagesList);

    cbTextLanguage.setItems(textLanguages);
  }

  public void listenerSetTabTitle() {
    taEditor.textProperty().addListener((obs, old, niu) -> {
      int numberLines = taEditor.getParagraphs().size();
      String newValue = niu.trim();

      if (taEditor.getCaretPosition() < 51 && numberLines == 1) {

        if (newValue.isEmpty()) {
          tab.setText("untitled");
        } else
        if (taEditor.getText().length() < 51) {
          tab.setText(newValue);
          int duplicateIndex = checkDuplicateTitle(newValue);

          if (duplicateIndex == 0) {
            tab.setText(newValue);
          } else {
            tab.setText(newValue + duplicateIndex);
          }
          
        } else {
          String titleToBe = newValue.substring(0, 51);
          int duplicateIndex = checkDuplicateTitle(titleToBe);

          if (duplicateIndex == 0) {
            tab.setText(titleToBe);
          } else {
            tab.setText(titleToBe + duplicateIndex);
          }
        }
      }
    });
  }

  /**
   * regrese 0 si no esta duplicado, en caso de estar duplicado regresa el indice de duplicidad.
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

}

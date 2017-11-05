package graphics;

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
    private static final String[] KEYWORDS = new String[] {
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
                    taEditor.setStyleSpans(0, computeHighlighting(taEditor.getText()));
                });
    }

    @FXML
    private void addNumberLines(KeyEvent event) {
        System.out.println(event.getCode().toString());
        System.out.println("tecla");
        vboxNumberLines.getChildren().clear(); 
        int numberLines = taEditor.getParagraphs().size();
        for (int i = 0; i < numberLines; i++) {
            Label numberLine = new Label(i + 1 + "");
            numberLine.setStyle("-fx-font-size: 20px; -fx-font-family: \"Courier New\"; -fx-text-fill: #90908A;");
            vboxNumberLines.getChildren().add(numberLine);
        }
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

}

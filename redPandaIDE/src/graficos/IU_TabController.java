package graficos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Alan Yoset García C
 */
public class IU_TabController implements Initializable {

    @FXML
    private VBox vboxNumberLines;
    @FXML
    private TextArea taEditor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Label numberLine = new Label("1");
        numberLine.setStyle("-fx-font-size: 20px; -fx-font-family: \"Courier New\"; -fx-text-fill: #FFFFFF;");
        vboxNumberLines.getChildren().add(numberLine); 
    }    

    @FXML
    private void addNumberLines(KeyEvent event) {
        vboxNumberLines.getChildren().clear();
        int numberLines = taEditor.getText().split("\n").length;
        System.out.println("Lineas: "+numberLines);
        for (int i = 0; i < numberLines; i++) {
            Label numberLine = new Label(i+1+"");
            numberLine.setStyle("-fx-font-size: 20px; -fx-font-family: \"Courier New\"; -fx-text-fill: #FFFFFF;");
            vboxNumberLines.getChildren().add(numberLine); 
        }
    }
    
}

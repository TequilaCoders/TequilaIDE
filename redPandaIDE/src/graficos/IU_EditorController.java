package graficos;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_EditorController implements Initializable {

    
    @FXML
    private TextArea textAreaUno;

    @FXML
    private VBox vBoxTabUno;

    @FXML
    private TextArea textAreaDos;

    @FXML
    private VBox vBoxTab2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textAreaUno.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addNewLine();
            }
        });
    }

    public void addNewLine(){
        int numeroLineas = textAreaUno.getText().split("\n").length;
        Label linea = new Label(numeroLineas+"");
        linea.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px;");
        vBoxTabUno.getChildren().add(linea);
    }
    
}

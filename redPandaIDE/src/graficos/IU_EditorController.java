package graficos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_EditorController implements Initializable {

    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem menuItemNew;
    @FXML
    private Menu menuTools;
    @FXML
    private MenuItem menuItemOptions;
    @FXML
    private Menu menuHelp;
    @FXML
    private MenuItem menuItemAbout;
    @FXML
    private TabPane tabPaneArchivos;
    
    private ResourceBundle rb; 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb; 
    }    

    @FXML
    private void addTab(ActionEvent event) throws IOException {
        Tab tab = new Tab();
        tab.setText("untitled");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/graficos/IU_Tab.fxml"), rb);
        ScrollPane newFile = loader.load();
        tab.setContent(newFile);
        tabPaneArchivos.getTabs().add(tab); 
        tabPaneArchivos.getSelectionModel().selectLast();
    }
    
}

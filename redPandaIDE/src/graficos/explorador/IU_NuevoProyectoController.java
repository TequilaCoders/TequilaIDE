/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graficos.explorador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entidades.Proyecto;
import entidades.ProyectoPK;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_NuevoProyectoController implements Initializable {

  @FXML
  private AnchorPane anchorPaneNuevoProyecto;

  @FXML
  private Pane paneNuevoProyecto;

  @FXML
  private JFXComboBox CBLenguajes;

  @FXML
  private JFXButton buttonCrear;

  @FXML
  private JFXButton buttonCancelar;

  @FXML
  private JFXTextField TextFieldNombre;
  
  private boolean estatusDeGuardado;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    estatusDeGuardado = false;
    try {
      llenarComboBoxSucursal();
      accionBotones();
    } catch (SQLException ex) {
      Logger.getLogger(IU_NuevoProyectoController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void llenarComboBoxSucursal() throws SQLException {
    String[] listaLenguajesProgramacion = {"Java", "Python", "C++"};

    ObservableList<String> lenguajesProgramacion = FXCollections.observableArrayList(
        listaLenguajesProgramacion);

    CBLenguajes.setItems(lenguajesProgramacion);
  }

  public void accionBotones() {
    buttonCancelar.setOnAction((new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
      }
    }));

    buttonCrear.setOnAction(e -> crearProyecto());
  }

  public void crearProyecto() {
    estatusDeGuardado = true;
    String nombre;
    String lenguajeProgramacion;

    nombre = TextFieldNombre.getText();
    lenguajeProgramacion = (String) CBLenguajes.getSelectionModel().getSelectedItem();

    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    //FALTA AGREGAR EL USUARIO DE MANERA FORMAL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><
    ProyectoPK clavesProyecto = new ProyectoPK();
    clavesProyecto.setUsuarioidUsuario(1);

    Proyecto proyectoACrear = new Proyecto(clavesProyecto, nombre, lenguajeProgramacion);

    //Se inserta el objeto en la BD
    entitymanager.getTransaction().begin();
    entitymanager.persist(proyectoACrear);
    entitymanager.getTransaction().commit();
    
    //una vez creado el proyecto se cierra el stage y se muestra la alerta de confirmación
    Stage stage = (Stage) paneNuevoProyecto.getScene().getWindow();
    
        
    mostrarAlerta();
    stage.close();
  }

  public void mostrarAlerta() {

      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirmación");
      alert.setHeaderText(null);
      alert.setContentText("El proyecto ha sido creado");

      alert.show();

  }
  
  public boolean getEstatusDeGuardado(){
    return estatusDeGuardado;
  }
  
}

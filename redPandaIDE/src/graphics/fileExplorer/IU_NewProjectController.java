/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.fileExplorer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entities.Proyecto;
import entities.ProyectoPK;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_NewProjectController implements Initializable {

  @FXML
  private Pane paneNewProject;

  @FXML
  private JFXComboBox cbLanguages;

  @FXML
  private JFXButton buttonCancel;

  @FXML
  private JFXTextField tfName;
  
  private boolean estatusDeGuardado;
  
  private int idProject;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    estatusDeGuardado = false;
    try {
      llenarComboBoxSucursal();
    } catch (SQLException ex) {
      Logger.getLogger(IU_NewProjectController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public boolean getEstatusDeGuardado(){
    return estatusDeGuardado;
  }
  
  public int getIdProject(){
    return idProject;
  }

  /**
   * Metodo llamado al accionar el elemento buttonCancel
   * @param event 
   */
  @FXML
  void closeWindow(ActionEvent event) {
    Stage stage = (Stage) buttonCancel.getScene().getWindow();
    stage.close();
  }

  /**
   * Método que carga el elemento cbLanguages con los lenguajes de programación del IDE.
   * @throws SQLException 
   */
  public void llenarComboBoxSucursal() throws SQLException {
    String[] listaLenguajesProgramacion = {"Java", "Python", "C++"};

    ObservableList<String> lenguajesProgramacion = FXCollections.observableArrayList(
        listaLenguajesProgramacion);

    cbLanguages.setItems(lenguajesProgramacion);
  }

  /**
   * NOTA --- ELIMINAR CONSULTA A LA BD DESDE AQUÍ
   * @param event 
   */
  @FXML
  void createProject(ActionEvent event) {
    estatusDeGuardado = true;
    String nombre;
    String lenguajeProgramacion;

    nombre = tfName.getText();
    lenguajeProgramacion = (String) cbLanguages.getSelectionModel().getSelectedItem();

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
    
    entitymanager.close();
    
    //se obtiene el id del proyecto recien creado
    getIdFromProject(nombre);
    
    //una vez creado el proyecto se cierra el stage y se muestra la alerta de confirmación
    Stage stage = (Stage) paneNewProject.getScene().getWindow();
        
    //mostrarAlerta();
    stage.close();
  }

  /**
   * Regresa el id del proyecto cuyo atributo nombre coincida con el parametro de entrada
   * @param nombre
   * @return 
   */
  public int getIdFromProject(String nombre){
    
    //Se abre la conexion con la BD
    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();
    
    TypedQuery<Proyecto> query
        = entitymanager.createNamedQuery("Proyecto.findByNombre", Proyecto.class).setParameter("nombre", nombre); //CAMBIAR EL 1 POR EL ID DE USUARIO>>>>
    idProject = query.getSingleResult().getProyectoPK().getIdProyecto();
    
    return idProject;
  }  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graficos.explorador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_ExploradorController implements Initializable {

  @FXML
  private AnchorPane anchorPanePrincipal;

  @FXML
  private JFXNodesList nodeListPerfil;

  @FXML
  private JFXButton buttonPerfil;

  @FXML
  ImageView imageVPerfilUsuario;

  @FXML
  private JFXButton buttonAyuda;

  @FXML
  private JFXButton buttonConfiguracion;

  @FXML
  private JFXButton buttonOpcionesPerfil;

  @FXML
  private JFXButton buttonProyectos;

  @FXML
  private JFXButton buttonProyectosCompartidos;

  @FXML
  private JFXButton buttonTodos;

  @FXML
  private JFXButton buttonAleman;

  @FXML
  private JFXButton buttonIngles;

  @FXML
  private FlowPane flowPaneProyectos;

  @FXML
  private Pane paneNuevoProyecto;

  @FXML
  private ImageView imageVNuevoProyecto;

  Pane paneMenuProyectos;
  
  Pane paneDialogoNuevoProyecto;
  
  AnchorPane anchorPaneNuevoProyecto;
  
  boolean estatusDeGuardado;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    listeners();
    cargarBotonesMenuPerfil();
    accionBotones();
  }

  public FlowPane getFlowPaneProyectos() {
    return flowPaneProyectos;
  }

  public void setFlowPaneProyectos(FlowPane flowPaneProyectos) {
    this.flowPaneProyectos = flowPaneProyectos;
  }  

  public void cargarBotonesMenuPerfil() {
    nodeListPerfil.addAnimatedNode(buttonPerfil);
    nodeListPerfil.addAnimatedNode(buttonOpcionesPerfil);
    nodeListPerfil.addAnimatedNode(buttonConfiguracion);
    nodeListPerfil.addAnimatedNode(buttonAyuda);

    nodeListPerfil.setSpacing(15);
  }

  public void listeners() {

    buttonPerfil.setOnMouseEntered((e -> imageVPerfilUsuario.setImage(new Image("/graficos/iconos/Male_User_Filled_White.png"))));
    buttonPerfil.setOnMouseExited((e -> imageVPerfilUsuario.setImage(new Image("/graficos/iconos/Male_User_Filled.png"))));

    buttonProyectos.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonProyectos.setStyle("-fx-background-color:white;");
        buttonProyectos.setPrefWidth(200);
      }
    }));
    buttonProyectos.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonProyectos.setStyle("-fx-background-color:#A1D6E2;");
        buttonProyectos.setPrefWidth(185);
      }
    }));

    buttonProyectosCompartidos.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonProyectosCompartidos.setStyle("-fx-background-color:white;");
        buttonProyectosCompartidos.setPrefWidth(200);
      }
    }));
    buttonProyectosCompartidos.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonProyectosCompartidos.setStyle("-fx-background-color:#A1D6E2;");
        buttonProyectosCompartidos.setPrefWidth(185);
      }
    }));

    buttonTodos.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonTodos.setStyle("-fx-background-color:white;");
        buttonTodos.setPrefWidth(200);
      }
    }));
    buttonTodos.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonTodos.setStyle("-fx-background-color:#A1D6E2;");
        buttonTodos.setPrefWidth(185);
      }
    }));

    buttonIngles.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonIngles.setStyle("-fx-background-color:white;");
        buttonIngles.setPrefWidth(105);
      }
    }));
    buttonIngles.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonIngles.setStyle("-fx-background-color:#A1D6E2;");
        buttonIngles.setPrefWidth(90);
      }
    }));

    buttonAleman.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonAleman.setStyle("-fx-background-color:white;");
        buttonAleman.setPrefWidth(105);
      }
    }));
    buttonAleman.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonAleman.setStyle("-fx-background-color:#A1D6E2;");
        buttonAleman.setPrefWidth(90);
      }
    }));
    
    buttonAyuda.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonAyuda.setStyle("-fx-background-color:white;");
        buttonAyuda.setPrefHeight(36);
        buttonAyuda.setMaxHeight(36);
        buttonAyuda.setMinHeight(36);
      }
    }));
    buttonAyuda.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonAyuda.setStyle("-fx-background-color:#A1D6E2;");
        buttonAyuda.setPrefHeight(32);
        buttonAyuda.setMaxHeight(32);
        buttonAyuda.setMinHeight(32);
      }
    }));
    
    buttonConfiguracion.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonConfiguracion.setStyle("-fx-background-color:white;");
        buttonConfiguracion.setPrefHeight(36);
        buttonConfiguracion.setMaxHeight(36);
        buttonConfiguracion.setMinHeight(36);
      }
    }));
    buttonConfiguracion.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonConfiguracion.setStyle("-fx-background-color:#A1D6E2;");
        buttonConfiguracion.setPrefHeight(32);
        buttonConfiguracion.setMaxHeight(32);
        buttonConfiguracion.setMinHeight(32);
      }
    }));

    buttonOpcionesPerfil.setOnMouseEntered((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonOpcionesPerfil.setStyle("-fx-background-color:white;");
        buttonOpcionesPerfil.setPrefHeight(36);
        buttonOpcionesPerfil.setMaxHeight(36);
        buttonOpcionesPerfil.setMinHeight(36);
      }
    }));
    buttonOpcionesPerfil.setOnMouseExited((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        buttonOpcionesPerfil.setStyle("-fx-background-color:#A1D6E2;");
        buttonOpcionesPerfil.setPrefHeight(32);
        buttonOpcionesPerfil.setMaxHeight(32);
        buttonOpcionesPerfil.setMinHeight(32);
      }
    }));

    paneNuevoProyecto.setOnMouseEntered((e -> imageVNuevoProyecto.setImage(new Image("/graficos/iconos/nuevo_proyectoSeleccionado.png"))));
    paneNuevoProyecto.setOnMouseExited((e -> imageVNuevoProyecto.setImage(new Image("/graficos/iconos/nuevo_proyecto.png"))));
  }

  public void accionBotones() {
    //Al selecciona crear un nuevo proyecto------------------------------------
    paneNuevoProyecto.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

        imageVNuevoProyecto.setImage(new Image("/graficos/iconos/nuevo_proyectoClic.png"));

        //se crea el cargador del nuevo componente
        FXMLLoader crearProyecto = new FXMLLoader(getClass().getResource("IU_NuevoProyecto.fxml"));
        //se crea un nuevo Stage con el fxml IU_NuevoProyecto y se muestra
        Stage iuCrearNuevoProyecto;
        iuCrearNuevoProyecto = ventanaIngresoDatosNuevoProyecto(crearProyecto);
        iuCrearNuevoProyecto.show();
        
        listenerNuevoProyectoCerrado(crearProyecto, iuCrearNuevoProyecto);
      }
    }));
    //--------------------------------------------------------------------------------------------
    
   buttonProyectos.setOnAction((e-> crearFlowPaneProyectosExistentes()));
  }

  public Stage ventanaIngresoDatosNuevoProyecto(FXMLLoader crearProyecto) {
    try {
      anchorPaneNuevoProyecto = crearProyecto.load();
    } catch (IOException ex) {
      Logger.getLogger(IU_ExploradorController.class.getName()).log(Level.SEVERE, null, ex);
    }

    Stage iuCrearNuevoProyecto = new Stage();

    iuCrearNuevoProyecto.initOwner(anchorPanePrincipal.getScene().getWindow());
    Scene escena = new Scene(anchorPaneNuevoProyecto);
    iuCrearNuevoProyecto.initStyle(StageStyle.UNDECORATED);
    iuCrearNuevoProyecto.setScene(escena);
    
    return iuCrearNuevoProyecto;
  }
  
  public void listenerNuevoProyectoCerrado(FXMLLoader crearProyecto, Stage iuCrearNuevoProyecto){
    //en el evento de cierre de la IU_NuevoProyecto, se obtendra el atributo estatusDeGaurdar, el cual
    //dira si se guardo un proyecto o no
    iuCrearNuevoProyecto.setOnHiding(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent we) {
        estatusDeGuardado = crearProyecto.<IU_NuevoProyectoController>getController().getEstatusDeGuardado();
        //se comprueba si se guardo o no un proyecto para mostrar los archivos de este
        if (estatusDeGuardado) {
          //se muestra la ventana 
          crearFlowPaneNuevoProyecto();
        }
      }
    });
  }
  
  public FlowPane crearFlowPaneNuevoProyecto(){
    try {
      FlowPane crearProyecto = FXMLLoader.load(getClass().getResource("/graficos/explorador/IU_FlowPaneNuevoProyecto.fxml"));
      flowPaneProyectos.getChildren().setAll(crearProyecto);

    } catch (IOException ex) {
      Logger.getLogger(IU_ExploradorController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    paneNuevoProyecto.setVisible(false);
    flowPaneProyectos.setVisible(true);
    
    return flowPaneProyectos;
  } 
  
  public FlowPane crearFlowPaneProyectosExistentes(){
    try {
      FlowPane crearProyecto = FXMLLoader.load(getClass().getResource("/graficos/explorador/IU_FlowPaneProyectosExistentes.fxml"));
      flowPaneProyectos.getChildren().setAll(crearProyecto);
     
    } catch (IOException ex) {
      Logger.getLogger(IU_ExploradorController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    //paneNuevoProyecto.setVisible(false);
    flowPaneProyectos.getChildren().add(0, paneNuevoProyecto);
    
    flowPaneProyectos.setVisible(true);
    paneNuevoProyecto.setVisible(true);
    
    return flowPaneProyectos;
  } 
  
  

}

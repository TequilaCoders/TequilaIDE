/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package graficos;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * 
 * @author Miguel Alejandro Cámara Árciga
 */
public class Alerta {
  private AlertType tipoAlerta;
  private String titulo;
  private String cabecera;
  private String contenido;
  
  public Alerta(AlertType tipoAlerta, String titulo, String cabecera, String contenido){
    this.tipoAlerta = tipoAlerta;
    this.titulo = titulo;
    this.cabecera = cabecera;
    this.contenido = contenido;
  }
  
  public Alert crearAlerta(){
    Alert alert = new Alert(tipoAlerta);
    alert.setTitle(titulo);
    alert.setHeaderText(cabecera);
    alert.setContentText(contenido);
    return alert;
  }
}

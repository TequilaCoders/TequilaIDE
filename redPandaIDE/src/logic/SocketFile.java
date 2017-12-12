package logic;

import static graphics.login.IU_LogInController.socket;//este socket global se debe mover a la capa de red
import io.socket.emitter.Emitter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.json.JSONObject;

/**
 * Provee métodos que interactuan con el servidor
 * @author Alan Yoset García C
 */
public class SocketFile {
  
  private int fileId;
  
  /**
   * Permite crear un nuevo archivo para un proyecto en la base de datos
   * @param nameTab
   * @param content
   * @param projectID
   * @param language 
   * @return id del archivo creado
   */
  public int createNewFile(String nameTab, String content, int projectID, String language) {
	fileId = -1; 
    JSONObject fileToSave = new JSONObject();

    fileToSave.accumulate("name", nameTab);
    fileToSave.accumulate("content", content);
    fileToSave.accumulate("projectID", projectID);
    fileToSave.accumulate("fileType", language);

    socket.connect();
    socket.emit("saveFile", fileToSave).on("fileSaved", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        fileId = (int) os[0];
        socket.disconnect();
		//Por ahora este método esta en la capa lógica, realmente en aquella capa debería ejecutarlo después de ejecutar este        
		//loadFiles();
      }
    });
	
	//Le damos unos milisegundos al servidor para que nos envie la respuesta
	try {
	  Thread.sleep(100);
	} catch (InterruptedException ex) {
	  Logger.getLogger(SocketFile.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	return fileId;
  }
  
  public void updateFile(String content, int fileId) {
    JSONObject fileToUpdate = new JSONObject();

    fileToUpdate.accumulate("fileID", fileId);
    fileToUpdate.accumulate("content", content);

    socket.connect();
    System.err.println("logrado");

    socket.emit("updateFile", fileToUpdate);

    socket.on("fileUpdated", new Emitter.Listener() {
      @Override
      public void call(Object... os) {
        System.out.println("file succesfully updated");
        socket.disconnect();
        //Este método ahora esta en la capa gráfica, realmente no se va a utilizar, porque cuando se agrega un archivo este se carga, aqui solo actualizo
		//loadFiles();
      }

    });
  }
}

package logic.sockets;

import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 * Provee métodos que interactuan con el servidor
 * @author Alan Yoset García C
 */
public class SocketFile {
  
  /**
   * Permite crear un nuevo archivo para un proyecto en la base de datos
   * @param nameTab
   * @param content
   * @param projectID
   * @param language 
   */
  public void createNewFile(String nameTab, String content, int projectID, String language) {
    JSONObject fileToSave = new JSONObject();

    fileToSave.accumulate("name", nameTab);
    fileToSave.accumulate("content", content);
    fileToSave.accumulate("projectID", projectID);
    fileToSave.accumulate("fileType", language);
    socket.emit("saveFile", fileToSave);
	
	/*
	
	*/
  }
  
  public void createNewFile(String name, int projectID, String language) {
    JSONObject fileToSave = new JSONObject();

    fileToSave.accumulate("name", name);
    fileToSave.accumulate("projectID", projectID);
    fileToSave.accumulate("fileType", language);
    socket.emit("saveFile", fileToSave);
	
	/*
	
	*/
  }

  public void updateFile(String content, int fileId) {
    JSONObject fileToUpdate = new JSONObject();

    fileToUpdate.accumulate("fileID", fileId);
    fileToUpdate.accumulate("content", content);
    socket.emit("updateFile", fileToUpdate);
  }
}

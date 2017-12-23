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

  public void updateFile(String content, int fileId, int room) {
    JSONObject fileToUpdate = new JSONObject();

    fileToUpdate.accumulate("fileID", fileId);
    fileToUpdate.accumulate("content", content);
    fileToUpdate.accumulate("room", room);
    socket.emit("updateFile", fileToUpdate);
  }
  
  public void deleteFile(int fileID) {
    JSONObject fileToSend = new JSONObject();
    
    fileToSend.accumulate("fileID", fileID);
    socket.emit("deleteFile", fileToSend);
  }
  
  public void loadFiles(int projectID) {
    JSONObject projectIDToSend = new JSONObject();
    
    projectIDToSend.accumulate("projectID", projectID);
    socket.emit("loadFiles", projectIDToSend);
  }
  
  public void joinFilesRoom(int fileID) {
    JSONObject fileToJoin = new JSONObject();
    
    fileToJoin.accumulate("fileID", fileID);
    socket.emit("joinFilesRoom", fileToJoin);
  }
  
  public void leaveFilesRoom(int fileID) {
    JSONObject fileToLeave = new JSONObject();
    
    fileToLeave.accumulate("fileID", fileID);
    socket.emit("leaveFilesRoom", fileToLeave);
  }
}

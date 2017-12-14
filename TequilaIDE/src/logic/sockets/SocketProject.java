package logic.sockets;

import static graphics.login.IU_LogInController.socket;
import org.json.JSONObject;

/**
 *
 * @author Alan Yoset García C
 */
public class SocketProject {
  public void loadProjects(int userId){
	JSONObject userIDToSend = new JSONObject();
    userIDToSend.accumulate("userID", userId);
    socket.emit("loadProjects", userIDToSend);
  }
  
  public void loadSharedProjects(int userId){
	JSONObject userIDToSend = new JSONObject();
    userIDToSend.accumulate("userID", userId);
    socket.emit("loadSharedProjects", userIDToSend);
  }
  
  public void createNewProject(String name, String programmingLanguage, int userId){
	JSONObject projectToSend = new JSONObject();
    
    projectToSend.accumulate("name", name);
    projectToSend.accumulate("programmingLanguage", programmingLanguage);
    projectToSend.accumulate("userID", userId);

    socket.emit("saveProject", projectToSend);
  }
}

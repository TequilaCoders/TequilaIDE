package logic.sockets;


import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

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
  
  public void joinProjectRoom(int projectID, int userID){
	JSONObject membershipToSend = new JSONObject();
    
    membershipToSend.accumulate("projectID", projectID);
    membershipToSend.accumulate("userID", userID);

    socket.emit("joinProjectRoom", membershipToSend);
  }
  
  public void leaveProjectRoom(int projectID, int userID){
    JSONObject membershipToSend = new JSONObject();
    membershipToSend.accumulate("projectID", projectID);
    membershipToSend.accumulate("userID", userID);
    
    socket.emit("leaveProjectRoom", membershipToSend);
  }
}

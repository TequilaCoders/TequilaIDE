package logic.sockets;


import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 *
 * @author Alan Yoset García C
 */
public class SocketProject {
  
  private static final String USER_ID = "userID"; 
  private static final String PROJECT_ID = "projectID"; 
  
  public void loadProjects(int userId){
	JSONObject userIDToSend = new JSONObject();
    userIDToSend.accumulate(USER_ID, userId);
    socket.emit("loadProjects", userIDToSend);
  }
  
  public void loadSharedProjects(int userId){
	JSONObject userIDToSend = new JSONObject();
    userIDToSend.accumulate(USER_ID, userId);
    socket.emit("loadSharedProjects", userIDToSend);
  }
  
  public void createNewProject(String name, String programmingLanguage, int userId){
	JSONObject projectToSend = new JSONObject();
    
    projectToSend.accumulate("name", name);
    projectToSend.accumulate("programmingLanguage", programmingLanguage);
    projectToSend.accumulate(USER_ID, userId);

    socket.emit("saveProject", projectToSend);
  }
  
  public void joinProjectRoom(int projectID, int userID){
	JSONObject membershipToSend = new JSONObject();
    
    membershipToSend.accumulate(PROJECT_ID, projectID);
    membershipToSend.accumulate(USER_ID, userID);

    socket.emit("joinProjectRoom", membershipToSend);
  }
  
  public void leaveProjectRoom(int projectID, int userID){
    JSONObject membershipToSend = new JSONObject();
    membershipToSend.accumulate(PROJECT_ID, projectID);
    membershipToSend.accumulate(USER_ID, userID);
    
    socket.emit("leaveProjectRoom", membershipToSend);
  }
  
  public void deleteProject(int projectID){
	JSONObject projectToSend = new JSONObject();
    projectToSend.accumulate(PROJECT_ID, projectID);
    socket.emit("deleteProject", projectToSend);
  }
  
  public void compileProject(int projectID, String language){
	JSONObject projectToSend = new JSONObject();
	projectToSend.accumulate(PROJECT_ID, projectID);
	projectToSend.accumulate("language", language);

	socket.emit("runCompiler", projectToSend);
  }
  
  public void runProject(int projectID, String language, String mainClass, String arguments){
	JSONObject projectToSend = new JSONObject();
	projectToSend.accumulate(PROJECT_ID, projectID);
	projectToSend.accumulate("language", language);
	projectToSend.accumulate("mainClass", mainClass);
	projectToSend.accumulate("arguments", arguments);
	socket.emit("runProgram", projectToSend);
  }
}

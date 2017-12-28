package logic.sockets;

import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 *
 * @author Alan Yoset García C
 */
public class SocketCollaborator {
  
  public void loadCollaborators(int projectID) {
    JSONObject projectIDToSend = new JSONObject();
    
    projectIDToSend.accumulate("projectID", projectID);
   
    socket.emit("getCollaborators", projectIDToSend);
  }
  
  public void deleteCollaborator(int collaboratorID, int projectID) {
    JSONObject collaborationToSend = new JSONObject();

    collaborationToSend.accumulate("projectID", projectID);
    collaborationToSend.accumulate("collaboratorID", collaboratorID);

    socket.emit("deleteCollaborator", collaborationToSend);
  }
  
  public void searchCollaborator(String searchCriteria) {
    JSONObject searchCriteriaToSend = new JSONObject();
    
    searchCriteriaToSend.accumulate("searchCriteria", searchCriteria);

    socket.emit("searchUser", searchCriteriaToSend);
  }
  
  public void addCollaborator(int collaboratorID, int projectID) {
    JSONObject collaborationToSend = new JSONObject();
    collaborationToSend.accumulate("collaboratorID", collaboratorID);
    collaborationToSend.accumulate("projectID", projectID);

    socket.emit("saveCollaborator", collaborationToSend);
  }
}

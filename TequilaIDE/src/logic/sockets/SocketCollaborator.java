package logic.sockets;

import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 *
 * @author Alan Yoset García C
 */
public class SocketCollaborator {
  private static final String PROJECTID = "projectID";
  
  public void loadCollaborators(int projectID) {
    JSONObject projectIDToSend = new JSONObject();
    
    projectIDToSend.accumulate(PROJECTID, projectID);
   
    socket.emit("getCollaborators", projectIDToSend);
  }
  
  public void deleteCollaborator(int collaboratorID, int projectID) {
    JSONObject collaborationToSend = new JSONObject();

    collaborationToSend.accumulate(PROJECTID, projectID);
    collaborationToSend.accumulate("collaboratorID", collaboratorID);

    socket.emit("deleteCollaborator", collaborationToSend);
  }
  
  public void searchCollaborator(String searchCriteria) {
    JSONObject searchCriteriaToSend = new JSONObject();
    
    searchCriteriaToSend.accumulate("searchCriteria", searchCriteria);

    socket.emit("searchUser", searchCriteriaToSend);
  }
  
  public void addCollaborator(int collaboratorID, int projectID, String alias, String mainCollaboratorAlias) {
    JSONObject collaborationToSend = new JSONObject();
    collaborationToSend.accumulate("collaboratorID", collaboratorID);
    collaborationToSend.accumulate(PROJECTID, projectID);
	collaborationToSend.accumulate("alias", alias);
	collaborationToSend.accumulate("mainCollaboratorAlias", mainCollaboratorAlias);

    socket.emit("saveCollaborator", collaborationToSend);
  }
}

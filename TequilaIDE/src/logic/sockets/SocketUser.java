package logic.sockets;

import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 *
 * @author Alan Yoset García C
 */
public class SocketUser {
  private static final String ALIAS = "alias";
  
  public void accesUser(String alias, String password) {
    JSONObject user = new JSONObject();
    user.accumulate(ALIAS, alias);
    user.accumulate("clave", password);

    socket.emit("access", user);
  }

  public void createUser(String name, String alias, String email, String password) {
    JSONObject userToSend = new JSONObject();
	userToSend.accumulate("name", name);
	userToSend.accumulate(ALIAS, alias);
	userToSend.accumulate("email", email);
	userToSend.accumulate("password", password);
	
	socket.emit("saveUser", userToSend);
  }
  
  public void checkAlias(String alias) {
	JSONObject aliasToSend = new JSONObject();
	aliasToSend.accumulate(ALIAS, alias);
	
	socket.emit("aliasChanged", aliasToSend);
  }
  
  public void checkEmail(String email) {
	JSONObject emailToSend = new JSONObject();
	emailToSend.accumulate("email", email);
	
	socket.emit("emailChanged", emailToSend);
  }
  
  public void updateBiography(int userId, String biography){
	JSONObject userToSend = new JSONObject();
	
	userToSend.accumulate("userId", userId);
	userToSend.accumulate("biography", biography);
	
	socket.emit("updateBiography", userToSend);
  }
}

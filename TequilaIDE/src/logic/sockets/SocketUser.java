package logic.sockets;

import org.json.JSONObject;
import static tequilaide.TequilaIDE.socket;

/**
 *
 * @author Alan Yoset García C
 */
public class SocketUser {

  public void createUser(String name, String alias, String email, String password) {
	JSONObject userToSend = new JSONObject();
	userToSend.accumulate("name", name);
	userToSend.accumulate("alias", alias);
	userToSend.accumulate("email", email);
	userToSend.accumulate("password", password);
	socket.emit("saveUser", userToSend);
  }
  
  public void checkAlias(String alias) {
	JSONObject aliasToSend = new JSONObject();
	aliasToSend.accumulate("alias", alias);
	socket.emit("aliasChanged", aliasToSend);
  }
  
  public void checkEmail(String email) {
	JSONObject emailToSend = new JSONObject();
	emailToSend.accumulate("email", email);
	socket.emit("emailChanged", emailToSend);
  }
}

package logic.domain;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class Collaborator {
  private int idUsuario; 
  private int room;
  private String alias;
  private String biografia;
  private boolean connected;

  public Collaborator() {
  }

  public Collaborator(int idUsuario, int room, boolean connected) {
    this.idUsuario = idUsuario;
    this.room = room;
    this.connected = connected;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getBiografia() {
    return biografia;
  }

  public void setBiografia(String biografia) {
    this.biografia = biografia;
  }

  public int getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
  }

  public int getRoom() {
    return room;
  }

  public void setRoom(int room) {
    this.room = room;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }
  
  
  
  
}

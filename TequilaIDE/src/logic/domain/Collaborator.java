package logic.domain;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class Collaborator {
  private int idUsuario; 
  private int idProyecto;
  private String alias;
  private String biografia;
  private boolean Connected;

  public Collaborator() {
  }

  public Collaborator(int idUsuario, int idProyecto) {
    this.idUsuario = idUsuario;
    this.idProyecto = idProyecto;
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

  public int getIdProyecto() {
    return idProyecto;
  }

  public void setIdProyecto(int idProyecto) {
    this.idProyecto = idProyecto;
  }

  public boolean isConnected() {
    return Connected;
  }

  public void setConnected(boolean Connected) {
    this.Connected = Connected;
  }
  
  
  
  
}

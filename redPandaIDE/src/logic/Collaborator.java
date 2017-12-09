package logic;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class Collaborator {
  private int Usuario_idUsuario; 
  private int idProyecto;
  private String alias;
  private String biografia;

  public Collaborator() {
  }

  public Collaborator(int idUsuario, int idProyecto) {
    this.Usuario_idUsuario = idUsuario;
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
    return Usuario_idUsuario;
  }

  public void setIdUsuario(int idUsuario) {
    this.Usuario_idUsuario = idUsuario;
  }

  public int getIdProyecto() {
    return idProyecto;
  }

  public void setIdProyecto(int idProyecto) {
    this.idProyecto = idProyecto;
  }
  
  
}

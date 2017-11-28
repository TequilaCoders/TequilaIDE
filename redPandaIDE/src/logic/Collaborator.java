package logic;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class Collaborator {
  private int idUsuario; 
  private int idProyecto; 

  public Collaborator() {
  }

  public Collaborator(int idUsuario, int idProyecto) {
    this.idUsuario = idUsuario;
    this.idProyecto = idProyecto;
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
  
  
}

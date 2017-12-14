package logic.domain;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class Project {
  private int idProyecto; 
  private int idUsuario; 
  private String nombre; 
  private String lenguaje; 
  private boolean shared;

  public Project() {
  }

  public Project(int idProyecto, int idUsuario, String nombre, String lenguaje) {
    this.idProyecto = idProyecto;
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.lenguaje = lenguaje;
  }

  
  public int getIdProyecto() {
    return idProyecto;
  }

  public void setIdProyecto(int idProyecto) {
    this.idProyecto = idProyecto;
  }

  public int getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getLenguaje() {
    return lenguaje;
  }

  public void setLenguaje(String lenguaje) {
    this.lenguaje = lenguaje;
  }

  public boolean isShared() {
    return shared;
  }

  public void setShared(boolean shared) {
    this.shared = shared;
  }

  
}

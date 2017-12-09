package logic;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class File {
  private int idArchivo;
  private int idProyecto; 
  private String nombre; 
  private String tipo;
  private String contenido; 

  public File() {
  }
  
  public File(int idArchivo, int idProyecto, String nombre, String tipo, String contenido) {
    this.idArchivo = idArchivo;
    this.idProyecto = idProyecto;
    this.nombre = nombre;
    this.tipo = tipo;
    this.contenido = contenido;
  }

  public int getIdArchivo() {
    return idArchivo;
  }

  public void setIdArchivo(int idArchivo) {
    this.idArchivo = idArchivo;
  }

  public int getIdProyecto() {
    return idProyecto;
  }

  public void setIdProyecto(int idProyecto) {
    this.idProyecto = idProyecto;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getContenido() {
    return contenido;
  }

  public void setContenido(String contenido) {
    this.contenido = contenido;
  }
  
  
  
}
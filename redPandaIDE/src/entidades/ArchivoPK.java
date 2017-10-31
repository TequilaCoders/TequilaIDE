/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Miguel Alejandro Cámara Árciga
 */
@Embeddable
public class ArchivoPK implements Serializable {

  @Basic(optional = false)
  @Column(name = "idArchivo")
  private int idArchivo;
  @Basic(optional = false)
  @Column(name = "Proyecto_idProyecto")
  private int proyectoidProyecto;

  public ArchivoPK() {
  }

  public ArchivoPK(int idArchivo, int proyectoidProyecto) {
    this.idArchivo = idArchivo;
    this.proyectoidProyecto = proyectoidProyecto;
  }

  public int getIdArchivo() {
    return idArchivo;
  }

  public void setIdArchivo(int idArchivo) {
    this.idArchivo = idArchivo;
  }

  public int getProyectoidProyecto() {
    return proyectoidProyecto;
  }

  public void setProyectoidProyecto(int proyectoidProyecto) {
    this.proyectoidProyecto = proyectoidProyecto;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) idArchivo;
    hash += (int) proyectoidProyecto;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ArchivoPK)) {
      return false;
    }
    ArchivoPK other = (ArchivoPK) object;
    if (this.idArchivo != other.idArchivo) {
      return false;
    }
    if (this.proyectoidProyecto != other.proyectoidProyecto) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entidades.ArchivoPK[ idArchivo=" + idArchivo + ", proyectoidProyecto=" + proyectoidProyecto + " ]";
  }

}

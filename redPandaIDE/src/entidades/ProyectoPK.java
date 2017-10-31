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
public class ProyectoPK implements Serializable {

  @Basic(optional = false)
  @Column(name = "idProyecto")
  private int idProyecto;
  @Basic(optional = false)
  @Column(name = "Usuario_idUsuario")
  private int usuarioidUsuario;

  public ProyectoPK() {
  }

  public ProyectoPK(int idProyecto, int usuarioidUsuario) {
    this.idProyecto = idProyecto;
    this.usuarioidUsuario = usuarioidUsuario;
  }

  public int getIdProyecto() {
    return idProyecto;
  }

  public void setIdProyecto(int idProyecto) {
    this.idProyecto = idProyecto;
  }

  public int getUsuarioidUsuario() {
    return usuarioidUsuario;
  }

  public void setUsuarioidUsuario(int usuarioidUsuario) {
    this.usuarioidUsuario = usuarioidUsuario;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (int) idProyecto;
    hash += (int) usuarioidUsuario;
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ProyectoPK)) {
      return false;
    }
    ProyectoPK other = (ProyectoPK) object;
    if (this.idProyecto != other.idProyecto) {
      return false;
    }
    if (this.usuarioidUsuario != other.usuarioidUsuario) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entidades.ProyectoPK[ idProyecto=" + idProyecto + ", usuarioidUsuario=" + usuarioidUsuario + " ]";
  }

}

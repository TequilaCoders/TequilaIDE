/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Miguel Alejandro Cámara Árciga
 */
@Entity
@Table(name = "proyecto")
@XmlRootElement
@NamedQueries({
  
  @NamedQuery(name = "Proyecto.findAll", query = "SELECT p FROM Proyecto p"),
  @NamedQuery(name = "Proyecto.findByIdProyecto", query = "SELECT p FROM Proyecto p WHERE p.proyectoPK.idProyecto = :idProyecto"),
  @NamedQuery(name = "Proyecto.findByNombre", query = "SELECT p FROM Proyecto p WHERE p.nombre = :nombre"),
  @NamedQuery(name = "Proyecto.findByLenguaje", query = "SELECT p FROM Proyecto p WHERE p.lenguaje = :lenguaje"),
  @NamedQuery(name = "Proyecto.findByUsuarioidUsuario", query = "SELECT p FROM Proyecto p WHERE p.proyectoPK.usuarioidUsuario = :usuarioidUsuario")})
public class Proyecto implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected ProyectoPK proyectoPK;
  @Basic(optional = false)
  @Column(name = "nombre")
  private String nombre;
  @Basic(optional = false)
  @Column(name = "lenguaje")
  private String lenguaje;

  public Proyecto() {
  }

  public Proyecto(ProyectoPK proyectoPK) {
    this.proyectoPK = proyectoPK;
  }

  public Proyecto(ProyectoPK proyectoPK, String nombre, String lenguaje) {
    this.proyectoPK = proyectoPK;
    this.nombre = nombre;
    this.lenguaje = lenguaje;
  }

  public Proyecto(int idProyecto, int usuarioidUsuario) {
    this.proyectoPK = new ProyectoPK(idProyecto, usuarioidUsuario);
  }

  public ProyectoPK getProyectoPK() {
    return proyectoPK;
  }

  public void setProyectoPK(ProyectoPK proyectoPK) {
    this.proyectoPK = proyectoPK;
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

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (proyectoPK != null ? proyectoPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Proyecto)) {
      return false;
    }
    Proyecto other = (Proyecto) object;
    if ((this.proyectoPK == null && other.proyectoPK != null) || (this.proyectoPK != null && !this.proyectoPK.equals(other.proyectoPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entidades.Proyecto[ proyectoPK=" + proyectoPK + " ]";
  }

}

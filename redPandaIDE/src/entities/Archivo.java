/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

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
@Table(name = "archivo")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Archivo.findAll", query = "SELECT a FROM Archivo a"),
  @NamedQuery(name = "Archivo.findByIdArchivo", query = "SELECT a FROM Archivo a WHERE a.archivoPK.idArchivo = :idArchivo"),
  @NamedQuery(name = "Archivo.findByNombre", query = "SELECT a FROM Archivo a WHERE a.nombre = :nombre"),
  @NamedQuery(name = "Archivo.findByTipo", query = "SELECT a FROM Archivo a WHERE a.tipo = :tipo"),
  @NamedQuery(name = "Archivo.findByProyectoidProyecto", query = "SELECT a FROM Archivo a WHERE a.archivoPK.proyectoidProyecto = :proyectoidProyecto"),
  @NamedQuery(name = "Archivo.findByContenido", query = "SELECT a FROM Archivo a WHERE a.contenido = :contenido")})
public class Archivo implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected ArchivoPK archivoPK;
  @Basic(optional = false)
  @Column(name = "nombre")
  private String nombre;
  @Basic(optional = false)
  @Column(name = "tipo")
  private String tipo;
  @Column(name = "contenido")
  private String contenido;

  public Archivo() {
  }

  public Archivo(ArchivoPK archivoPK) {
    this.archivoPK = archivoPK;
  }

  public Archivo(ArchivoPK archivoPK, String nombre, String tipo) {
    this.archivoPK = archivoPK;
    this.nombre = nombre;
    this.tipo = tipo;
  }

  public Archivo(int idArchivo, int proyectoidProyecto) {
    this.archivoPK = new ArchivoPK(idArchivo, proyectoidProyecto);
  }

  public ArchivoPK getArchivoPK() {
    return archivoPK;
  }

  public void setArchivoPK(ArchivoPK archivoPK) {
    this.archivoPK = archivoPK;
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

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (archivoPK != null ? archivoPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Archivo)) {
      return false;
    }
    Archivo other = (Archivo) object;
    if ((this.archivoPK == null && other.archivoPK != null) || (this.archivoPK != null && !this.archivoPK.equals(other.archivoPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entities.Archivo[ archivoPK=" + archivoPK + " ]";
  }

}

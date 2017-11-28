/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Miguel Alejandro Cámara Árciga
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario")
    , @NamedQuery(name = "Usuario.findByNombres", query = "SELECT u FROM Usuario u WHERE u.nombres = :nombres")
    , @NamedQuery(name = "Usuario.findByCorreo", query = "SELECT u FROM Usuario u WHERE u.correo = :correo")
    , @NamedQuery(name = "Usuario.findByAlias", query = "SELECT u FROM Usuario u WHERE u.alias = :alias")
    , @NamedQuery(name = "Usuario.findByClave", query = "SELECT u FROM Usuario u WHERE u.clave = :clave")
    , @NamedQuery(name = "Usuario.findByBiografia", query = "SELECT u FROM Usuario u WHERE u.biografia = :biografia")
    , @NamedQuery(name = "Usuario.compareAlias",
        query = "SELECT u "
        + "FROM Usuario u "
        + "WHERE u.alias = :alias"),
  @NamedQuery(name = "Usuario.compareEmail",
        query = "SELECT u "
        + "FROM Usuario u "
        + "WHERE u.correo = :email")})
public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idUsuario")
  private Integer idUsuario;
  @Basic(optional = false)
  @Column(name = "nombres")
  private String nombres;
  @Basic(optional = false)
  @Column(name = "correo")
  private String correo;
  @Basic(optional = false)
  @Column(name = "alias")
  private String alias;
  @Basic(optional = false)
  @Column(name = "clave")
  private String clave;
  @Column(name = "biografia")
  private String biografia;

  public Usuario() {
  }

  public Usuario(Integer idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Usuario(Integer idUsuario, String nombres, String correo, String alias, String clave) {
    this.idUsuario = idUsuario;
    this.nombres = nombres;
    this.correo = correo;
    this.alias = alias;
    this.clave = clave;
  }

  public Integer getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Integer idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getClave() {
    return clave;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getBiografia() {
    return biografia;
  }

  public void setBiografia(String biografia) {
    this.biografia = biografia;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idUsuario != null ? idUsuario.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Usuario)) {
      return false;
    }
    Usuario other = (Usuario) object;
    if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entities.Usuario[ idUsuario=" + idUsuario + " ]";
  }

}

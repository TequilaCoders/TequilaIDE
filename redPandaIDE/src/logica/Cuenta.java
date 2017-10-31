package logica;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class Cuenta {
    private Integer idUsuario;
    private String nombres;
    private String apPaterno;
    private String apMaterno;
    private String correo;
    private String alias;
    private String clave;
    private String biografia;

    public Cuenta() {
    }

    public Cuenta(String alias, String clave) {
        this.alias = alias;
        this.clave = clave;
    }
    
    

    public Cuenta(Integer idUsuario, String nombres, String apPaterno, String apMaterno, String correo, String alias, String clave, String biografia) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.correo = correo;
        this.alias = alias;
        this.clave = clave;
        this.biografia = biografia;
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

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
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
    
    
}

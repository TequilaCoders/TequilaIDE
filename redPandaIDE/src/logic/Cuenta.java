package logic;

/**
 *
 * @author Alan Yoset García Cruz
 */
public class Cuenta {

    private String alias;
    private String clave;

    public Cuenta() {
    }

    public Cuenta(String alias, String clave) {
        this.alias = alias;
        this.clave = clave;
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
}

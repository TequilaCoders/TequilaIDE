package servidor;

import logica.Cuenta;
import controladoresJPA.UsuarioJpaController;
import entidades.Usuario;
import java.util.List;

/**
 *
 * @author Alan Yoset García Cruz 
 */
public class Servidor {
    public boolean logIn(Cuenta cuenta) {
        UsuarioJpaController controller = new UsuarioJpaController();
        List<Usuario> consulta = controller.findUsuarioEntities();
        Usuario usuario = null; 
        
        for (int i = 0; i < consulta.size(); i++) {
            if (consulta.get(i).getAlias().equals(cuenta.getAlias())) {
                usuario = consulta.get(i); 
            }
        }

        if (usuario != null) {
            if (usuario.getClave().equals(cuenta.getClave())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

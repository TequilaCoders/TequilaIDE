import logic.Cuenta;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.Servidor;

/**
 *
 * @author alanc
 */
public class TestLogin {
    
    public TestLogin() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void pruebaAccesoCorrecto(){
        boolean resultado = true; 
        Servidor server = new Servidor();
        boolean obtenido = server.logIn(new Cuenta("Alex Cámara","clave"));
        assertEquals(resultado, obtenido);
    }
    
    @Test
    public void pruebaAccesoUsuario(){
        boolean resultado = false; 
        Servidor server = new Servidor();
        boolean obtenido = server.logIn(new Cuenta("AlexCámara","clave"));
        assertEquals(resultado, obtenido);
    }
    
    @Test
    public void pruebaAccesoPassword(){
        boolean resultado = false; 
        Servidor server = new Servidor();
        boolean obtenido = server.logIn(new Cuenta("Alex Cámara","almejandro"));
        assertEquals(resultado, obtenido);
    }
}

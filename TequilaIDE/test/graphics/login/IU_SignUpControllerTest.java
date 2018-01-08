package graphics.login;

import java.util.ResourceBundle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class IU_SignUpControllerTest {


  public IU_SignUpControllerTest() {
  }
  
  /**
   * Test of areThereEmptyFields method, of class GUISignUpController.
   */
  @Test
  public void testAreThereEmptyFields() {
    System.out.println("areThereEmptyFields");
    String name = "";
    String alias = "";
    String email = "";
    String password = "";
    String confirmedPassword = "";
    GUISignUpController instance = new GUISignUpController();
    
    boolean expResult = true;
    boolean result = instance.areThereEmptyFields(name, alias, email, password, confirmedPassword);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of areThereEmptyFields method, of class GUISignUpController.
   */
  @Test
  public void testAreThereEmptyFieldsFail() {
    System.out.println("areThereEmptyFields");
    String name = "Miguel";
    String alias = "alex";
    String email = "arcam";
    String password = "patito";
    String confirmedPassword = "patito";
    GUISignUpController instance = new GUISignUpController();
    
    boolean expResult = false;
    boolean result = instance.areThereEmptyFields(name, alias, email, password, confirmedPassword);
    assertEquals(expResult, result);
  }
  
  @Test
  public void testCheckName() {
    System.out.println("checkName");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");

    String name = "Miguel Alejandro Camara Arciga";
    GUISignUpController instance = new GUISignUpController();
    
    String expResult = "noError";
    String result = instance.checkName(name);
    
    assertEquals(expResult, result);
  }
  
  @Test
  public void testCheckNameFail() {
    System.out.println("checkName");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");

    String name = "Miguel @lejanndr0";
    GUISignUpController instance = new GUISignUpController();
    
    String expResult = "hasSpecialCharacters";
    String result = instance.checkName(name);
    
    assertEquals(expResult, result);
  }
  
  /**
   * Test of checkAlias method, of class GUISignUpController.
   */
  @Test
  public void testCheckAlias() {
    System.out.println("checkAlias");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String alias = "alexcamara";
    GUISignUpController instance = new GUISignUpController();
    String expResult = "noError";
    String result = instance.checkAlias(alias);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of checkAlias method, of class GUISignUpController.
   */
  @Test
  public void testCheckAliasFail() {
    System.out.println("checkAlias");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String alias = "alex camara&";
    GUISignUpController instance = new GUISignUpController();
    String expResult = "hasSpecialCharacters";
    String result = instance.checkAlias(alias);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of checkEmail method, of class GUISignUpController.
   */
  @Test
  public void testCheckEmail() {
    System.out.println("checkEmail");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String email = "arcam_22@hotmail.com";
    GUISignUpController instance = new GUISignUpController();
    String expResult = "noError";
    String result = instance.checkEmail(email);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of checkEmail method, of class GUISignUpController.
   */
  @Test
  public void testCheckEmailFail() {
    System.out.println("checkEmail");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String email = "arcam_hotmail.com";
    GUISignUpController instance = new GUISignUpController();
    String expResult = "invalidFormat";
    String result = instance.checkEmail(email);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of checkPassword method, of class GUISignUpController.
   */
  @Test
  public void testCheckPassword() {
    System.out.println("checkPassword");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String password = "m1guelCamara";
    GUISignUpController instance = new GUISignUpController();
    String expResult = "noError";
    String result = instance.checkPassword(password);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of checkPassword method, of class GUISignUpController.
   */
  @Test
  public void testCheckPasswordFail() {
    System.out.println("checkPassword");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String password = "miguelCamara";
    GUISignUpController instance = new GUISignUpController();
    String expResult = "hasNotNumber";
    String result = instance.checkPassword(password);
    assertEquals(expResult, result);
  }
 
  /**
   * Test of IsCheckPasswordConfirmation method, of class GUISignUpController.
   */
  @Test
  public void testIsCheckPasswordConfirmation() {
    System.out.println("isPasswordConfirmationCorrect");
    String password = "confirmacion";
    String confirmedPassword = "confirmacion";
    GUISignUpController instance = new GUISignUpController();
    String expResult = "noError";
    String result = instance.checkPasswordConfirmation(password, confirmedPassword);
    assertEquals(expResult, result);
    
  }
  
  /**
   * Test of validateEmailFormat method, of class GUISignUpController.
   */
  @Test
  public void testValidateEmailFormat() {
    System.out.println("validateEmailFormat");
    String emailField = "arcam_2112@hotmail.com";
    GUISignUpController instance = new GUISignUpController();
    boolean expResult = true;
    boolean result = instance.validateEmailFormat(emailField);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of validateEmailFormat method, of class GUISignUpController.
   */
  @Test
  public void testValidateEmailFormatFail() {
    System.out.println("validateEmailFormat");
    String emailField = "arcam_hotmail.com";
    GUISignUpController instance = new GUISignUpController();
    boolean expResult = false;
    boolean result = instance.validateEmailFormat(emailField);
    assertEquals(expResult, result);
  }
  
}

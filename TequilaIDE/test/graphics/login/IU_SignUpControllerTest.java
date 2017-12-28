/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.login;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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
   * Test of areThereEmptyFields method, of class IU_SignUpController.
   */
  @Test
  public void testAreThereEmptyFields() {
    System.out.println("areThereEmptyFields");
    String name = "";
    String alias = "";
    String email = "";
    String password = "";
    String confirmedPassword = "";
    IU_SignUpController instance = new IU_SignUpController();
    
    boolean expResult = true;
    boolean result = instance.areThereEmptyFields(name, alias, email, password, confirmedPassword);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of areThereEmptyFields method, of class IU_SignUpController.
   */
  @Test
  public void testAreThereEmptyFieldsFail() {
    System.out.println("areThereEmptyFields");
    String name = "Miguel";
    String alias = "alex";
    String email = "arcam";
    String password = "patito";
    String confirmedPassword = "patito";
    IU_SignUpController instance = new IU_SignUpController();
    
    boolean expResult = false;
    boolean result = instance.areThereEmptyFields(name, alias, email, password, confirmedPassword);
    assertEquals(expResult, result);
  }
//_------------------------FALTAN DE PROBAR Y HACER FUNCIONAR----------------------------------------
  /**
   * Test of checkName method, of class IU_SignUpController.
   */
  /*@Test
  public void testCheckName() {
    System.out.println("checkName");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/login/IU_SignUp.fxml"), rb);
    IU_SignUpController controller = new IU_SignUpController();
    loader.setController(controller);
    controller = loader.getController();
    
    String name = "Miguel Alejandro Camara Arciga";
    
    boolean expResult = true;
    boolean result = controller.checkName(name, rb);
    assertEquals(expResult, result);
    
  }

  /**
   * Test of checkAlias method, of class IU_SignUpController.
   */
  /*@Test
  public void testCheckAlias() {
    System.out.println("checkAlias");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String alias = "";
    IU_SignUpController instance = new IU_SignUpController();
    boolean expResult = false;
    boolean result = instance.checkAlias(alias, rb);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of checkEmail method, of class IU_SignUpController.
   */
  /*@Test
  public void testCheckEmail() {
    System.out.println("checkEmail");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String email = "";
    IU_SignUpController instance = new IU_SignUpController();
    boolean expResult = false;
    boolean result = instance.checkEmail(email, rb);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of checkPassword method, of class IU_SignUpController.
   */
  /*@Test
  public void testCheckPassword() {
    System.out.println("checkPassword");
    ResourceBundle rb = ResourceBundle.getBundle("resources/languages.language");
    String password = "";
    IU_SignUpController instance = new IU_SignUpController();
    boolean expResult = false;
    boolean result = instance.checkPassword(password, rb);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isPasswordConfirmationCorrect method, of class IU_SignUpController.
   */
  /*@Test
  public void testIsPasswordConfirmationCorrect() {
    System.out.println("isPasswordConfirmationCorrect");
    String password = "";
    String confirmedPassword = "";
    IU_SignUpController instance = new IU_SignUpController();
    boolean expResult = false;
    boolean result = instance.isPasswordConfirmationCorrect(password, confirmedPassword);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }*/
  
  //----------------AQUI TERMINAN LAS PRUEBAS SIN FUNCIONAR----------------------------------------

  /**
   * Test of validateEmailFormat method, of class IU_SignUpController.
   */
  @Test
  public void testValidateEmailFormat() {
    System.out.println("validateEmailFormat");
    String emailField = "arcam_2112@hotmail.com";
    IU_SignUpController instance = new IU_SignUpController();
    boolean expResult = true;
    boolean result = instance.validateEmailFormat(emailField);
    assertEquals(expResult, result);
  }
  
  /**
   * Test of validateEmailFormat method, of class IU_SignUpController.
   */
  @Test
  public void testValidateEmailFormatFail() {
    System.out.println("validateEmailFormat");
    String emailField = "arcam_hotmail.com";
    IU_SignUpController instance = new IU_SignUpController();
    boolean expResult = false;
    boolean result = instance.validateEmailFormat(emailField);
    assertEquals(expResult, result);
  }
  
}

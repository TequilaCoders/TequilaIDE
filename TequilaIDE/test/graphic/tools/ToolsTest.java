package graphic.tools;

import graphics.tools.Tools;
import java.util.ArrayList;
import java.util.List;
import logic.domain.Project;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class ToolsTest {

  public ToolsTest() {
  }

  @Test
  public void testGetHashedPassword() {
    String testWord = "pino";
    String result = Tools.getHashedPassword(testWord);
    String expected = "A55C93846CEC6F8780E3F00B112D6B897E8E74B02B52DDCE0280B067A3A294CF";
    assertEquals(expected, result);
  }

  @Test
  public void testApplyRegularExpression() {
    String testRegex = "[^A-Za-z0-9]";
    String testField = "alexCamara";
    boolean expected = false;
    boolean result = Tools.applyRegularExpression(testField, testRegex);
    assertEquals(expected, result);
  }

  @Test
  public void testApplyRegularExpression2() {
    String testRegex = "^[\\p{L} .'-]+$";
    String testField = "Miguel Alejandro Camara Arciga";
    boolean expected = true;
    boolean result = Tools.applyRegularExpression(testField, testRegex);
    assertEquals(expected, result);
  }

  @Test
  public void testApplyRegularExpressionPassword() {
    String testRegex = "(?=.*[0-9])";
    String testField = "pezespada1";
    boolean expected = true;
    boolean result = Tools.applyRegularExpression(testField, testRegex);
    assertEquals(expected, result);
  }

  @Test
  public void testApplyRegularExpressionPasswordWrong() {
    String testRegex = "(?=.*[0-9])";
    String testField = "pezespada";
    boolean expected = false;
    boolean result = Tools.applyRegularExpression(testField, testRegex);
    assertEquals(expected, result);
  }

  @Test
  public void testCheckLenght() {
    int minimum = 5;
    int maximum = 16;
    String testField = "longitudDelCampo";
    boolean expected = true;
    boolean result = Tools.checkLenght(testField, minimum, maximum);
    assertEquals(expected, result);
  }

  @Test
  public void testCheckLenghtMaximumFail() {
    int minimum = 5;
    int maximum = 16;
    String testField = "longitudDelCampoDos";
    boolean expected = false;
    boolean result = Tools.checkLenght(testField, minimum, maximum);
    assertEquals(expected, result);
  }
  
  @Test
  public void testCheckLenghtMinimumFail() {
    int minimum = 5;
    int maximum = 16;
    String testField = "long";
    boolean expected = false;
    boolean result = Tools.checkLenght(testField, minimum, maximum);
    assertEquals(expected, result);
  }
  
  @Test
  public void testCheckProjectByName() {
    List<Project> projectList = new ArrayList<>();
    
    Project testProject1 = new Project(1, 1, "Primero", "cpp");
    Project testProject2 = new Project(1, 1, "Segundo", "py");
    Project testProject3 = new Project(1, 1, "Tercero", "java");
    Project testProject4 = new Project(1, 1, "Cuarto", "cpp");
    Project testProject5 = new Project(1, 1, "Quinto", "py");
    
    projectList.add(testProject1);
    projectList.add(testProject2);
    projectList.add(testProject3);
    projectList.add(testProject4);
    projectList.add(testProject5);
    
    String testName = "Cuarto";
    
    Project expected = testProject4;
    Project result = Tools.searchProjectByName(testName, projectList);
    assertEquals(expected, result);
  }

}

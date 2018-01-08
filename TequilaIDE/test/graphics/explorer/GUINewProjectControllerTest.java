package graphics.explorer;

import java.util.ArrayList;
import java.util.List;
import logic.domain.Project;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alan Yoset García C
 */
public class GUINewProjectControllerTest {
  
  public GUINewProjectControllerTest() {
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

  @Test
  public void testProjectExist(){
	GUINewProjectController instance = new GUINewProjectController();
	String projectName = "proyectoPrueba";
	
	List<Project> projectsList = new ArrayList<>();
	projectsList.add(new Project(0, 0, "proyectoPrueba", "java"));
	
	boolean expResult = true; 
	boolean result = instance.projectExist(projectName, projectsList);
	assertEquals(expResult, result);
  }
  
  @Test
  public void testProjectNotExist(){
	GUINewProjectController instance = new GUINewProjectController();
	String projectName = "proyectoPrueba";
	
	List<Project> projectsList = new ArrayList<>();
	projectsList.add(new Project(0, 0, "proyectoTest", "java"));
	
	boolean expResult = false; 
	boolean result = instance.projectExist(projectName, projectsList);
	assertEquals(expResult, result);
  }
}

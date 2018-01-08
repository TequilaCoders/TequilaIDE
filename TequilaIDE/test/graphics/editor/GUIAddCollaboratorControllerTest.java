/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.editor;

import java.util.ArrayList;
import java.util.List;
import logic.domain.Collaborator;
import logic.domain.User;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class GUIAddCollaboratorControllerTest {

  
  /**
   * Test of searchCollaboratorInList method, of class GUIAddCollaboratorController.
   */
  @Test
  public void testSearchCollaboratorInList() {
    System.out.println("testSearchCollaboratorInList");
    
    List<Collaborator> collaboratorListTest = new ArrayList<>();
    
    Collaborator testCollaborator1 = new Collaborator(1,1, false);
    Collaborator testCollaborator2 = new Collaborator(2,1, false);
    Collaborator testCollaborator3 = new Collaborator(3,1, false);
    Collaborator testCollaborator4 = new Collaborator(4,1, false);
    Collaborator testCollaborator5 = new Collaborator(5,1, false);
    
    collaboratorListTest.add(testCollaborator1);
    collaboratorListTest.add(testCollaborator2);
    collaboratorListTest.add(testCollaborator3);
    collaboratorListTest.add(testCollaborator4);
    collaboratorListTest.add(testCollaborator5);
    
    int collaboratorID = 0;
    GUIAddCollaboratorController instance = new GUIAddCollaboratorController();
    boolean expResult = false;
    boolean result = instance.searchCollaboratorInList(collaboratorID, collaboratorListTest);
    assertEquals(expResult, result);
    
  }

  /**
   * Test of isCollaboratorAndUserTheSame method, of class GUIAddCollaboratorController.
   */
  @Test
  public void testIsCollaboratorAndUserTheSame() {
    System.out.println("isCollaboratorAndUserTheSame");
    int collaboratorID = 0;
    User testUser = new User(1, "testAlias");

    GUIAddCollaboratorController instance = new GUIAddCollaboratorController();
    boolean expResult = false;
    instance.setUser(testUser);
    boolean result = instance.isCollaboratorAndUserTheSame(collaboratorID);
    assertEquals(expResult, result);
  }

  /**
   * Test of checkCollaboratorLimits method, of class GUIAddCollaboratorController.
   */
  @Test
  public void testCheckCollaboratorLimits() {
    System.out.println("checkCollaboratorLimits");
    
    List<Collaborator> collaboratorListTest = new ArrayList<>();
    
    Collaborator testCollaborator1 = new Collaborator(1,1, false);
    Collaborator testCollaborator2 = new Collaborator(2,1, false);
    Collaborator testCollaborator3 = new Collaborator(3,1, false);
    Collaborator testCollaborator4 = new Collaborator(4,1, false);
    Collaborator testCollaborator5 = new Collaborator(5,1, false);
    
    collaboratorListTest.add(testCollaborator1);
    collaboratorListTest.add(testCollaborator2);
    collaboratorListTest.add(testCollaborator3);
    collaboratorListTest.add(testCollaborator4);
    collaboratorListTest.add(testCollaborator5);
    
    GUIAddCollaboratorController instance = new GUIAddCollaboratorController();
    instance.setCollaboratorsList(collaboratorListTest);
    
    boolean expResult = true;
    boolean result = instance.checkCollaboratorLimits();
    assertEquals(expResult, result);
  }
  
}

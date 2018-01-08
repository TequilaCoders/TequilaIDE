/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.editor;

import java.util.ArrayList;
import java.util.List;
import logic.domain.Collaborator;
import logic.domain.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class GUIEditorControllerTest {

  /**
   * Test of searchFileByName method, of class GUIEditorController.
   */
  @Test
  public void testSearchFileByName() {
    System.out.println("searchFileByName");
    
    List<File> fileListTest = new ArrayList<>();
    
    File testFile1 = new File(1,1, "file1");
    File testFile2 = new File(2,1, "file2");
    File testFile3 = new File(3,1, "file3");
    File testFile4 = new File(4,1, "file4");
    File testFile5 = new File(5,1, "file5");
    
    fileListTest.add(testFile1);
    fileListTest.add(testFile2);
    fileListTest.add(testFile3);
    fileListTest.add(testFile4);
    fileListTest.add(testFile5);
    
    String name = "file2";
    GUIEditorController instance = new GUIEditorController();
    
    File expResult = testFile2;
    File result = instance.searchFileByName(name, fileListTest);
    assertEquals(expResult, result);
  }

  /**
   * Test of markCollaboratorsAsConnected method, of class GUIEditorController.
   */
  @Test
  public void testMarkCollaboratorsAsConnected() {
    System.out.println("markCollaboratorsAsConnected");
    List<Collaborator> collaboratorsConnected = new ArrayList<>();
    
    Collaborator testCollaboratorConnected1 = new Collaborator(1, 2, true);
    Collaborator testCollaboratorConnected2 = new Collaborator(2, 2, true);
    Collaborator testCollaboratorConnected3 = new Collaborator(3, 2, true);
    
    collaboratorsConnected.add(testCollaboratorConnected1);
    collaboratorsConnected.add(testCollaboratorConnected2);
    collaboratorsConnected.add(testCollaboratorConnected3);
    
    List<Collaborator> collaboratorsList = new ArrayList<>();
    
    Collaborator testCollaborator1 = new Collaborator(1, 2, false);
    Collaborator testCollaborator2 = new Collaborator(2, 2, false);
    Collaborator testCollaborator3 = new Collaborator(3, 2, false);
    Collaborator testCollaborator4 = new Collaborator(4, 2, false);
    Collaborator testCollaborator5 = new Collaborator(5, 2, false);
    
    collaboratorsList.add(testCollaborator1);
    collaboratorsList.add(testCollaborator2);
    collaboratorsList.add(testCollaborator3);
    collaboratorsList.add(testCollaborator4);
    collaboratorsList.add(testCollaborator5);
    
    List<Collaborator> collaboratorsListExpected = new ArrayList<>();
    
    testCollaborator1.setConnected(true);
    testCollaborator2.setConnected(true);
    testCollaborator3.setConnected(true);
    
    collaboratorsListExpected.add(testCollaborator1);
    collaboratorsListExpected.add(testCollaborator2);
    collaboratorsListExpected.add(testCollaborator3);
    collaboratorsListExpected.add(testCollaborator4);
    collaboratorsListExpected.add(testCollaborator5);
    
    GUIEditorController instance = new GUIEditorController();
    List<Collaborator> result = instance.markCollaboratorsAsConnected(collaboratorsConnected, collaboratorsList);
    assertEquals(collaboratorsListExpected, result);
  }

}

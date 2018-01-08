/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.editor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import logic.domain.File;
import logic.domain.Project;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Miguel Alejandro Cámara Árciga
 */
public class GUIFileTreeControllerTest {
  
  @Test
  public void testTabIsOpened() {
  List<String> currentTabs = new ArrayList<>();
  
  String tab1 = "tab1";
  String tab2 = "tab2";
  String tab3 = "tab3";
  
  currentTabs.add(tab1);
  currentTabs.add(tab2);
  currentTabs.add(tab3);
  
  String name = "tab1";
  GUIFileTreeController instance = new GUIFileTreeController();
  instance.setCurrentTabs(currentTabs);
  
  boolean expResult = true;
  boolean result = instance.tabIsOpened(name);
  assertEquals(expResult, result);
  }
}

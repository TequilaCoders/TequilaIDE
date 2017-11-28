package graphics.textEditor;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 *
 * @author alanc
 */
public class Compiler {

  public int compile(String file) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    int result = compiler.run(null, null, null,file);
    return result; 
  }
}

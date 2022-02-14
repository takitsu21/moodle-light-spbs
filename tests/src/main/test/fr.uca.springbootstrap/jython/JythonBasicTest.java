
package fr.uca.springbootstrap.jython;

import org.junit.Test;
import org.python.util.PythonInterpreter;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JythonBasicTest {
    @Test
    public void helloWorld() {
        PythonInterpreter pyInterp = new PythonInterpreter();
        StringWriter output = new StringWriter();
        pyInterp.setOut(output);
        pyInterp.exec("print('Hello World!')\nprint('Bye')");
        assertEquals("Hello World!\nBye", output.toString().trim());
    }
}
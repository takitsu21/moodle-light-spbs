package fr.uca.api.util;

import fr.uca.api.models.questions.CodeRunner;
import fr.uca.api.repository.question.CodeRunnerRepository;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class CodeRunnerExec {
    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    public CodeRunnerExec() {
    }

    public Map<String, Object> execPy(String code, CodeRunner question) {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            Map<String, Object> success = new HashMap<>();


            StringWriter output = new StringWriter();
            pyInterp.setOut(output);
            pyInterp.exec(code + "\n" + question.getTest());
            boolean isValid = output.toString().trim().equals(question.getAnwser().getAnswer());
            success.put("success", isValid);
            return success;
        } catch (Exception e) {
            Map<String, Object> ret = new HashMap<>();
            ret.put("success", false);
            ret.put("Error", e.toString());
            e.printStackTrace();
            return ret;
        }
    }
}

package coderunner.executor;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import fr.uca.api.models.questions.CodeRunner;
import fr.uca.api.repository.question.CodeRunnerRepository;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class CodeRunnerExec {

    public CodeRunnerExec() {
    }

    public Map<String, Object> execPy(String code, String testCode, String answer) {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            Map<String, Object> success = new HashMap<>();
            StringWriter output = new StringWriter();
            pyInterp.setOut(output);
            pyInterp.exec(code + "\n" + testCode);
            boolean isValid = output.toString().trim().equals(answer);
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

//    private void execDockerContainer() {
//        DockerClientConfig standard = DefaultDockerClientConfig
//                .createDefaultConfigBuilder()
//                .withDockerHost("tcp://localhost:2376")
//                .build();
//        standard.
//    }
}

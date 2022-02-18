package executor;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.DockerConfigFile;
import com.github.dockerjava.core.dockerfile.Dockerfile;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.StringWriter;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CodeRunnerExec {

    public CodeRunnerExec() {
    }

    public static void main(String[] args) {
        CodeRunnerExec codeRunnerExec = new CodeRunnerExec();
//        boolean status = (boolean) codeRunnerExec.execPy(args[0], args[1], args[2]).get("success");
        codeRunnerExec.execDockerContainer("print(1)", "print(1)", "1");
//        if (status) {
//            System.exit(0);
//        } else {
//            System.exit(1);
//        }
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

    private void execDockerContainer(String code, String testCode, String answer) {
        DockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
        .dockerHost(config.getDockerHost())
        .maxConnections(100)
        .connectionTimeout(Duration.ofSeconds(30))
        .responseTimeout(Duration.ofSeconds(45))
        .build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);

        CreateContainerResponse container = dockerClient.createContainerCmd("openjdk:11")
                .withCmd("groupadd -r worker && useradd -r worker -g worker")

                .withCmd("cp core/final-jar/*.jar /app.jar")
                .withName("worker")
                .withHostName("worker")
                .withEntrypoint("java", "-jar", "\"" + code + "\"",
                        "\"" + code + "\"",
                        "\"" + answer + "\"").exec();
        dockerClient.startContainerCmd(container.getId()).exec();
//            dockerClient.buildImageCmd()

//        dockerClient.buildImageCmd(new File("./code-runner-service/worker-docker/Dockerfile"));
//        dockerClient.buildImageCmd(new File("./code-runner-service/worker-docker/Dockerfile"));
        //        dockerClient.attachContainerCmd("java ")
    }
}

package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.CodeRunner;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.CodeRunnerRequest;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.CodeRunnerRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubmitQuestionnaireStepdefs {
    private final SpringIntegration springIntegration = SpringIntegration.getInstance();

    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    AuthController authController;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    PasswordEncoder encoder;


    @Et("un module {string} qui a un enseignant {string} et un étudiant {string} et qui a la question numéro {int} {string} avec description {string} la réponse est {string} avec le test {string} dans le {string} sq")
    public void unModuleQuiAUnEnseignantEtUnÉtudiantEtQuiALaQuestionNuméroAvecDescriptionLaRéponseEstAvecLeTestDansLeSq(
            String moduleName,
            String teacherName,
            String studentName,
            int num,
            String questionName,
            String descriptionName,
            String answerStr,
            String test,
            String questionnaireName) {
        Module module = moduleRepository.findByName(moduleName).orElse(new Module(moduleName));
        User teacher = userRepository.findByUsername(teacherName).get();
        User student = userRepository.findByUsername(studentName).
                orElse(new User(studentName, studentName + "@test.fr", encoder.encode(PASSWORD)));
        student.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        module.setParticipants(new HashSet<>() {{
            add(teacher);
            add(student);
        }});

        userRepository.save(student);
        Answer answer = new Answer(answerStr);
        answerRepository.save(answer);
        CodeRunner codeRunner = new CodeRunner(
                num,
                questionName,
                descriptionName,
                test,
                answer
        );
        codeRunnerRepository.save(codeRunner);

        Questionnaire questionnaire = new Questionnaire(
                questionnaireName,
                "Test d'un code runner",
                1
        );
        questionnaire.getQuestions().add(codeRunner);
        questionnaireRepository.save(questionnaire);

        module.getRessources().add(questionnaire);

        moduleRepository.save(module);
    }

    @Et("{string} écrit son code python dans le fichier {string} et soumet sont code au module {string} de la question numéro {int} dans le {string} sq")
    public void écritSonCodePythonDansLeFichierEtSoumetSontCodeAuModuleDeLaQuestionNuméroDansLeSq(String arg0, String arg1, String arg2, int arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = null;
        for (Ressource ressource : module.getRessources()) {
            if (arg4.equalsIgnoreCase(ressource.getName())) {
                questionnaire = (Questionnaire) ressource;
                break;
            }
        }

        CodeRunner codeRunner = null;
        for (Question question : questionnaire.getQuestions()) {
            if (question.getNumber() == arg3) {
                codeRunner = (CodeRunner) question;
                break;
            }
        }
        String jwtStudent = authController.generateJwt(arg0, PASSWORD);
        InputStream is = getClass().getClassLoader().getResourceAsStream(arg1);

        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = is.read()) != -1; ) {
            sb.append((char) ch);
        }
        springIntegration.executePost(String.format(
                        "http://localhost:8080/api/modules/%d/questionnaire/%d/code_runner/%d",
                        module.getId(),
                        questionnaire.getId(),
                        codeRunner.getId()),

                new CodeRunnerRequest(sb.toString()),
                jwtStudent
        );
    }

    @Quand("{string} soumet le questionnaire {string} du module {string} sq")
    public void soumetLeQuestionnaireDuModuleSq(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();

        String jwtStudent = authController.generateJwt(arg0, PASSWORD);
        Questionnaire questionnaire = null;
        for (Ressource ressource : module.getRessources()) {
            if (arg1.equalsIgnoreCase(ressource.getName())) {
                questionnaire = (Questionnaire) ressource;
                break;
            }
        }
        springIntegration.executePost(String.format(
                "http://localhost:8080/api/modules/%d/questionnaire/%d",
                module.getId(),
                questionnaire.getId()), jwtStudent);
    }

    @Et("la note est {int} sur {int} sq")
    public void laNoteEstSurSq(int arg0, int arg1) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Integer> map = gson.fromJson(jsonString, Map.class);

        assertEquals(Double.valueOf(arg0), map.get("note"));
        assertEquals(Double.valueOf(arg1), map.get("nbQuestion"));
    }


}

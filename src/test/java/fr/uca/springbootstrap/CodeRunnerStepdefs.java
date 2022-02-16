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
import fr.uca.springbootstrap.repository.question.AnswerCodeRunnerRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.CodeRunnerRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CodeRunnerStepdefs {
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
    AnswerCodeRunnerRepository answerCodeRunnerRepository;

    @Autowired
    PasswordEncoder encoder;

      //TODO voir si je peux encore plus factoriser
    @Et("la question numero {int} {string} avec description {string} la reponse est {string} avec le test {string} dans le {string} du module {string}")
    public void laQuestionNumeroAvecDescriptionLaReponseEstAvecLeTestDansLeDuModule(
            int num,
            String questionName,
            String descriptionName,
            String answerStr,
            String test,
            String questionnaireName,
            String moduleName) {

        Module module = moduleRepository.findByName(moduleName).get();

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

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(questionnaireName);
        questionnaire.getQuestions().add(codeRunner);
        questionnaireRepository.save(questionnaire);
    }

    @Quand("{string} veut ajouter la question {string} avec description {string} la réponse est {string} avec le test {string} au module {string} dans le {string} crn")
    public void veutAjouterLaQuestionAvecDescriptionLaRéponseEstAvecLeTestCrn(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws IOException {

        Module module = moduleRepository.findByName(arg5).get();
        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        Questionnaire questionnaire = new Questionnaire(
                arg6,
                "Test d'un code runner",
                2
        );
        questionnaireRepository.save(questionnaire);
        module.addRessource(questionnaire);
        moduleRepository.save(module);
        springIntegration.executePost(String.format(
                        "http://localhost:8080/api/modules/%d/questionnaire/%d/code_runner/",
                        module.getId(),
                        questionnaire.getId()),
                new CodeRunnerRequest(1, arg1, arg2, arg3, arg4),
                jwtTeacher);
    }

    @Et("la question {string} est créer dans le questionnaire {string} dans le module {string}")
    public void laQuestionEstCréer(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = null;
        for (Ressource ressource : module.getRessources()) {
            if (arg1.equalsIgnoreCase(ressource.getName())) {
                questionnaire = (Questionnaire) ressource;
                break;
            }
        }

        CodeRunner codeRunner = null;
        for (Question question : questionnaire.getQuestions()) {
            if (question.getName().equals(arg0)) {
                codeRunner = (CodeRunner) question;
                break;
            }
        }
        assertTrue(questionnaire.getQuestions().contains(codeRunner));
    }


    @Quand("{string} écrit son code python dans le fichier {string} et soumet sont code au module {string} de la question numéro {int} dans le {string}")
    public void écritSonCodePythonEtSoumetSontCodeAuModuleDeLaQuestionNuméro(String arg0, String arg1, String arg2, int arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        User user = userRepository.findByUsername(arg0).get();
        user.addRole(roleRepository.findByName(ERole.ROLE_ADMIN).
                orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        userRepository.save(user);
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg4);
        CodeRunner codeRunner = (CodeRunner) questionnaire.findQuestionByNum(arg3);

        String jwtStudent = authController.generateJwt(arg0, PASSWORD);
        InputStream is = getClass().getClassLoader().getResourceAsStream(arg1);

        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = is.read()) != -1; ) {
            sb.append((char) ch);
        }
        springIntegration.executePost(String.format(
                        "http://localhost:8080/api/modules/%d/questionnaire/%d/code_runner/%d/test",
                        module.getId(),
                        questionnaire.getId(),
                        codeRunner.getId()),

                new CodeRunnerRequest(sb.toString()),
                jwtStudent
        );
    }

    @Et("la réponse est vrai {int} crn")
    public void laRéponseEstVraiCrn(int arg0) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Boolean> map = gson.fromJson(jsonString, Map.class);
        Boolean b = arg0 == 1;
        assertEquals(b, map.get("success"));
    }

    @Et("la réponse est faux {int} crn")
    public void laRéponseEstFauxCrn(int arg0) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Boolean> map = gson.fromJson(jsonString, Map.class);
        Boolean b = arg0 != 0;
        assertEquals(b, map.get("success"));
    }
}

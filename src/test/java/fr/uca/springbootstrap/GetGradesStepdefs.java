package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.CodeRunner;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.CodeRunnerRequest;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
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
public class GetGradesStepdefs {
    private final SpringIntegration springIntegration = SpringIntegration.getInstance();

    private static final String PASSWORD = "password";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;


//    @Et("un questionnaire {string} appartenant à un module {string} gg")
//    public void unQuestionnaireAppartenantAUnModuleGg(String arg0, String arg1) {
////        Module module = moduleRepository.findByName(arg1).get();
////        Questionnaire questionnaire = new Questionnaire(arg0, "le questionnaire code runner", 1);
////        questionnaire.setVisibility(true);
////        questionnaireRepository.save(questionnaire);
////        System.out.println("Questionnaire " + questionnaire.getId() + " créé");
////        module.addRessource(questionnaire);
////        moduleRepository.save(module);
//    }

    @Et("la question numéro {int} {string} avec description {string} la réponse est {string} avec le test {string} dans le {string} du module {string} gg")
    public void laQuestionNumeroAvecDescriptionLaReponseEstAvecLeTestDansLeDuModuleGg(int arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) {
//        Answer answer = new Answer(arg3);
//        answerRepository.save(answer);
//        CodeRunner codeRunner = new CodeRunner(arg0, arg1, arg2, arg4, answer);
//        codeRunnerRepository.save(codeRunner);
//        Questionnaire questionnaire = (Questionnaire) moduleRepository.findByName(arg6).get().findRessourceByName(arg5);
//        questionnaire.addQuestion(codeRunner);
//        System.out.println("Question ajoutée au questionnaire :" + questionnaire.getId());
//        questionnaireRepository.save(questionnaire);
    }

    @Et("{string} soumet son code dans le fichier {string} à la question {int} du questionnaire {string} du module {string}")
    public void soumetSonCodeDansLeFichierÀLaQuestionDuQuestionnaireDuModule(String arg0, String arg1, int arg2, String arg3, String arg4) throws IOException {
//        Module module = moduleRepository.findByName(arg4).get();
//        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
//        System.out.println("Questions du questionnaire " + questionnaire.getId());
//        for (Question question : questionnaire.getQuestions()) {
//            System.out.println(question.getNumber());
//        }
//        System.out.println("On cherche " + arg2);
//        CodeRunner codeRunner = (CodeRunner) questionnaire.findQuestionByNum(arg2);
//        String jwtStudent = authController.generateJwt(arg0, PASSWORD);
//        InputStream is = getClass().getClassLoader().getResourceAsStream(arg1);
//
//        StringBuilder sb = new StringBuilder();
//        for (int ch; (ch = is.read()) != -1; ) {
//            sb.append((char) ch);
//        }
//        springIntegration.executePost(String.format(
//                        "http://localhost:8080/api/module/%d/questionnaire/%d/code_runner/%d",
//                        module.getId(),
//                        questionnaire.getId(),
//                        codeRunner.getId()),
//
//                new CodeRunnerRequest(sb.toString()),
//                jwtStudent
//        );
//
//        assertEquals(200, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("{string} soumet le questionnaire {string} du module {string} gg")
    public void soumetLeQuestionnaireDuModuleGg(String arg0, String arg1, String arg2) throws IOException {
//        Module module = moduleRepository.findByName(arg2).get();
//        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
//
//        String jwtStudent = authController.generateJwt(arg0, PASSWORD);
//        springIntegration.executePost(String.format(
//                        "http://localhost:8080/api/module/%d/questionnaire/%d",
//                        module.getId(),
//                        questionnaire.getId()),
//                jwtStudent
//        );
    }

    @Quand("{string} récupères les notes du questionnaire {string} du module {string} gg")
    public void recuperesLesNotesDuQuestionnaireDuModuleGg(String arg0, String arg1, String arg2) throws IOException {
//        Module module = moduleRepository.findByName(arg2).get();
//        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
//        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
//        springIntegration.executeGet(String.format(
//                        "http://localhost:8080/api/module/%d/questionnaire/%d/grades",
//                        module.getId(),
//                        questionnaire.getId()),
//                jwtTeacher
//        );
    }

    //Je le laisse pour le moment pour eviter qu'il s'execute
    @Alors("le dernier status de request est {int} gg")
    public void leDernierStatusDeRequestEstGg(int arg0) {
//        assertEquals(arg0, springIntegration.getLattestHttpResponse().getStatusLine().getStatusCode());
    }

    @Et("les notes de {string} sont affichées et avec un résultat de {string}")
    public void lesNotesDeSontAfficheesEtAvecUnResultatDeSur(String arg0, String arg1) throws IOException {
//        String json_grades = EntityUtils.toString(latestHttpResponse.getEntity());
//        GsonBuilder builder = new GsonBuilder();
//        builder.setPrettyPrinting();
//
//        Gson gson = builder.create();
//        Map<String, String> grades = gson.fromJson(json_grades, Map.class);
//
//        assertTrue(grades.containsKey(arg0));
//        assertTrue(grades.get(arg0).equalsIgnoreCase(arg1));
    }
}

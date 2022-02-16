package java.fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetQuestionStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;


    @Et("la question {string} numéro {int} dans le questionnaire {string} du module {string} auq")
    public void laQuestionNuméroDansLeQuestionnaireDuModuleAuq(String arg0, int arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire =  (Questionnaire) module.findRessourceByName(arg2);
        Question question = new QCM(arg1 ,arg0, "Question de test");
        questionRepository.save(question);
        questionnaire.addQuestion(question);
        questionnaireRepository.save(questionnaire);
    }

    @Quand("l'étudiant {string} récupère la question {string} du questionnaire {string} du module {string} auq")
    public void lÉtudiantRécupèreLaQuestionDuQuestionnaireDuModuleAuq(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire =  (Questionnaire) module.findRessourceByName(arg2);
        Question question = questionnaire.findQuestionByName(arg1);
        assert(question != null);

        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeGet("http://localhost:8080/api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/questions/" + question.getId(),
                jwt);
    }

    @Quand("l'etudiant {string} recupere les questions du questionnaire {string} du module {string} auq")
    public void lEtudiantRecupereLesQuestionsDuQuestionnaireDuModuleAuq(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire =  (Questionnaire) module.findRessourceByName(arg1);

        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeGet("http://localhost:8080/api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/questions/",
                jwt);
    }

    @Alors("la réponse est {int} auq")
    public void laRéponseEstAuq(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question {string} en renvoyée auq")
    public void laQuestionEnRenvoyéeAuq(String arg0) throws IOException {
        String json_question = EntityUtils.toString(latestHttpResponse.getEntity());
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        QCM question = gson.fromJson(json_question, QCM.class);

        assertEquals(arg0, question.getName());
    }

    @Et("les questions {string} et {string} du questionnaire {string} du module {string} sont renvoyees")
    public void lesQuestionsEtDuQuestionnaireDuModuleSontRenvoyees(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);
        Question question1 = questionnaire.findQuestionByName(arg0);
        Question question2 = questionnaire.findQuestionByName(arg1);

        String json_questions = EntityUtils.toString(latestHttpResponse.getEntity());
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> questions = gson.fromJson(json_questions, Map.class);

        assertTrue(questions.containsKey(String.valueOf(question1.getId())));
        assertTrue(questions.containsKey(String.valueOf(question2.getId())));
    }
}

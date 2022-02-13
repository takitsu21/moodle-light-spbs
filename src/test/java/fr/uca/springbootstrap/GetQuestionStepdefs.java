package fr.uca.springbootstrap;

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
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetQuestionStepdefs extends SpringIntegration {
    private static final String PASSWORD = "securedPassword";

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

    @Etantdonné("le questionnaire {string} dans le module {string} auq")
    public void leQuestionnaireDansLeModuleAuq(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        Questionnaire questionnaire = new Questionnaire(arg0, "Questionnaire", 1);
        questionnaireRepository.save(questionnaire);
        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }


    @Et("l'étudiant {string} dans le module {string} auq")
    public void lÉtudiantDansLeModuleAuq(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0)
                .orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
        Module module = moduleRepository.findByName(arg1).get();
        module.getParticipants().add(user);
        moduleRepository.save(module);
    }

    @Et("l'étudiant {string} sans module auq")
    public void lÉtudiantSansModuleAuq(String arg0) {
        User user = userRepository.findByUsername(arg0)
                .orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

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
        executeGet("http://localhost:8080/api/module/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/question/" + question.getId(),
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
}

package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.CodeRunner;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.CodeRunnerRequest;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.CodeRunnerRepository;
import fr.uca.springbootstrap.repository.question.QCMRepository;
import io.cucumber.java.en_scouse.An;
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
    QCMRepository qcmRepository;

    @Autowired
    PasswordEncoder encoder;

    @Et("le QCM numero {int} {string} avec pour description {string} et pour bonne response {string} dans le {string} du module {string}")
    public void leQCMNumeroAvecPourDescriptionDansLeDuModule(
            int num,
            String nameQCM,
            String descriptionQCM,
            String correctAnswer,
            String questionnaireName,
            String moduleName) {

        Module module = moduleRepository.findByName(moduleName).get();

        Answer answer = new Answer(correctAnswer);
        Answer wrongAnswer1 = new Answer("Wrong answer");
        Answer wrongAnswer2 = new Answer("Another wrong answer");
        Answer wrongAnswer3 = new Answer("Still a wrong answer");
        answerRepository.save(answer);
        answerRepository.save(wrongAnswer1);
        answerRepository.save(wrongAnswer2);
        answerRepository.save(wrongAnswer3);
        QCM qcm = new QCM(
                num,
                nameQCM,
                descriptionQCM
        );
        qcmRepository.save(qcm);

        qcm.setPossibleAnswers(new HashSet<>() {{
            add(wrongAnswer1);
            add(answer);
            add(wrongAnswer2);
            add(wrongAnswer3);
        }});

        qcm.setAnswer(answer);
        qcmRepository.save(qcm);

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(questionnaireName);
        questionnaire.getQuestions().add(qcm);
        questionnaireRepository.save(questionnaire);
    }

    @Quand("{string} soumet le questionnaire {string} du module {string}")
    public void soumetLeQuestionnaireDuModule(String arg0, String arg1, String arg2) throws IOException {
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

    @Et("la note est {int} sur {int}")
    public void laNoteEstSur(int arg0, int arg1) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Integer> map = gson.fromJson(jsonString, Map.class);

        assertEquals(Double.valueOf(arg0), map.get("note"));
        assertEquals(Double.valueOf(arg1), map.get("nbQuestion"));
    }

}

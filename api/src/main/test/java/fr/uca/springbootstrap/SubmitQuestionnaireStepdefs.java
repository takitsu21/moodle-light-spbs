package fr.uca.springbootstrap;

import auth.models.ERole;
import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.Ressource;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.*;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.CodeRunnerRepository;
import fr.uca.api.models.Module;
import fr.uca.api.repository.question.OpenQuestionRepository;
import fr.uca.api.repository.question.QCMRepository;
import io.cucumber.java.bs.A;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.CodeRunnerRequest;
import payload.request.LoginRequest;
import payload.request.SignupRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitQuestionnaireStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

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
    QCMRepository qcmRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;


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
            String questionnaireName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).orElse(new Module(moduleName));
        UserRef teacher = userRefRepository.findByUsername(teacherName).get();

        executePost("http://localhost:8080/api/auth/signup",
                new SignupRequest(studentName, studentName + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_STUDENT));
                }}));
        executePost("http://localhost:8080/api/auth/signin",
                new LoginRequest(studentName, PASSWORD));
        UserRef student = userRefRepository.findByUsername(studentName).get();
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(student.getUsername(), (String) map.get("accessToken"));

        module.setParticipants(new HashSet<>() {{
            add(teacher);
            add(student);
        }});

        userRefRepository.save(student);
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

    @Et("les bonnes reponses de la question {string} du questionnaire {string} du module {string} sont {string} et {string}")
    public void laBonneReponseDeLaQuestionDuQuestionnaireDuModuleEst(String arg0, String arg1, String arg2, String arg3, String arg4){
        Module module = moduleRepository.findByName(arg2).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion openQuestion = (OpenQuestion) questionnaire.findQuestionByName(arg0);

        Answer answer = new Answer(arg3);
        Answer answer1 = new Answer(arg4);

        answerRepository.save(answer);
        answerRepository.save(answer1);

        openQuestion.setAnswers(new HashSet<>() {{
            add(answer);
            add(answer1);
        }});

        openQuestionRepository.save(openQuestion);
    }

    @Quand("{string} soumet le questionnaire {string} du module {string} sq")
    public void soumetLeQuestionnaireDuModuleSq(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        executePost(String.format(
                "http://localhost:8080/api/modules/%d/questionnaire/%d",
                module.getId(),
                questionnaire.getId()), jwt);
    }

    @Et("la note est {int} sur {int} sq")
    public void laNoteEstSurSq(int arg0, int arg1) throws IOException {
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Integer> map = gson.fromJson(jsonString, Map.class);

        assertEquals(Double.valueOf(arg0), map.get("note"));
        assertEquals(Double.valueOf(arg1), map.get("nbQuestion"));
    }


}

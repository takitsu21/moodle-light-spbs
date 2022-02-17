package fr.uca.springbootstrap;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Module;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.CodeRunner;
import fr.uca.api.models.questions.Question;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.CodeRunnerRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.CodeRunnerRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetGradesStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AuthController authController;



    @Et("la question numéro {int} {string} avec description {string} la réponse est {string} avec le test {string} dans le {string} du module {string} gg")
    public void laQuestionNumeroAvecDescriptionLaReponseEstAvecLeTestDansLeDuModuleGg(int arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) {
        Answer answer = new Answer(arg3);
        answerRepository.save(answer);
        CodeRunner codeRunner = new CodeRunner(arg0, arg1, arg2, arg4, answer);
        codeRunnerRepository.save(codeRunner);
        Questionnaire questionnaire = (Questionnaire) moduleRepository.findByName(arg6).get().findRessourceByName(arg5);
        questionnaire.addQuestion(codeRunner);
        System.out.println("Question ajoutée au questionnaire :" + questionnaire.getId());
        questionnaireRepository.save(questionnaire);
    }

    @Et("{string} soumet son code dans le fichier {string} à la question {int} du questionnaire {string} du module {string}")
    public void soumetSonCodeDansLeFichierÀLaQuestionDuQuestionnaireDuModule(String arg0, String arg1, int arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        System.out.println("Questions du questionnaire " + questionnaire.getId());
        for (Question question : questionnaire.getQuestions()) {
            System.out.println(question.getNumber());
        }
        System.out.println("On cherche " + arg2);
        CodeRunner codeRunner = (CodeRunner) questionnaire.findQuestionByNum(arg2);
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        InputStream is = getClass().getClassLoader().getResourceAsStream(arg1);

        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = is.read()) != -1; ) {
            sb.append((char) ch);
        }
        executePost(String.format(
                        "http://localhost:8080/api/modules/%d/questionnaire/%d/code_runner/%d",
                        module.getId(),
                        questionnaire.getId(),
                        codeRunner.getId()),

                new CodeRunnerRequest(sb.toString()),
                jwt
        );

        assertEquals(200, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("{string} soumet le questionnaire {string} du module {string} gg")
    public void soumetLeQuestionnaireDuModuleGg(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);

        UserRef user = userRefRepository.findByUsername(arg0).get();
        String jwt = userToken.get(user.getUsername());
        executePost(String.format(
                        "http://localhost:8080/api/modules/%d/questionnaire/%d",
                        module.getId(),
                        questionnaire.getId()),
                jwt
        );
    }

    @Quand("{string} récupères les notes du questionnaire {string} du module {string} gg")
    public void recuperesLesNotesDuQuestionnaireDuModuleGg(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        UserRef user = userRefRepository.findByUsername(arg0).get();
        String jwt = userToken.get(user.getUsername());

        executeGet(String.format(
                        "http://localhost:8080/api/modules/%d/questionnaire/%d/grades",
                        module.getId(),
                        questionnaire.getId()),
                jwt
        );
    }

    @Alors("le dernier status de request est {int} gg")
    public void leDernierStatusDeRequestEstGg(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("les notes de {string} sont affichées et avec un résultat de {string}")
    public void lesNotesDeSontAfficheesEtAvecUnResultatDeSur(String arg0, String arg1) throws IOException {
        String json_grades = EntityUtils.toString(latestHttpResponse.getEntity());
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        System.out.println(gson.fromJson(json_grades, Map.class));
        Map<String, String> grades = gson.fromJson(json_grades, Map.class);

        assertTrue(grades.containsKey(arg0));
        assertTrue(grades.get(arg0).equalsIgnoreCase(arg1));
    }
}

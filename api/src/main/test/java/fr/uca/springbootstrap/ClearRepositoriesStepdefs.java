package fr.uca.springbootstrap;

import auth.models.ERole;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.cours.TextRepository;
import fr.uca.api.repository.question.*;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import payload.request.LoginRequest;
import payload.request.SignupRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ClearRepositoriesStepdefs extends SpringIntegration {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    TextRepository textRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    GradesQuestionnaireRepository gradesQuestionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    AnswerCodeRunnerRepository answerCodeRunnerRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    AnswerQCMRepository answerQCMRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @Autowired
    AnswerOpenQuestionRepository answerOpenQuestionRepository;


    @Etantdonnéque("les tables sont videes")
    public void lesTablesSontVidees() throws IOException {
        executePost("http://localhost:8080/api/auth/signup",
                new SignupRequest("superUser", "superUser@test.fr", "password", new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_ADMIN));
                }}));
        executePost("http://localhost:8080/api/auth/signin",
                new LoginRequest("superUser", "password"));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);
        executePost("http://localhost:8081/api/auth/clear",
                (String) map.get("accessToken"));
        System.out.println("VIDE LES TABLES " + latestHttpResponse.getStatusLine().getStatusCode());

        moduleRepository.deleteAll();
        ressourceRepository.deleteAll();
        coursRepository.deleteAll();
        textRepository.deleteAll();
        questionnaireRepository.deleteAll();
        gradesQuestionnaireRepository.deleteAll();
        questionRepository.deleteAll();
        qcmRepository.deleteAll();
        answerQCMRepository.deleteAll();
        openQuestionRepository.deleteAll();
        answerOpenQuestionRepository.deleteAll();
        codeRunnerRepository.deleteAll();
        answerCodeRunnerRepository.deleteAll();
        answerRepository.deleteAll();
        userRefRepository.deleteAll();

//        userToken = new HashMap<>();
    }
}

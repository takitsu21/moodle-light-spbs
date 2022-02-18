package fr.uca.springbootstrap;

import fr.uca.api.models.ERole;
import fr.uca.api.models.Module;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.cours.TextRepository;
import fr.uca.api.repository.question.*;
import fr.uca.api.util.VerifyAuthorizations;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Etantdonnéque;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import payload.request.LoginRequest;
import payload.request.SignupRequest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsStepdefs extends SpringIntegration {
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



    @Etantdonné("le professeur {string}")
    public void leProfesseur(String arg0) throws IOException {
        executePost(VerifyAuthorizations.apiHost + "api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_TEACHER));
                }}));


        UserRef user = userRefRepository.findByUsername(arg0).get();

        executePost(VerifyAuthorizations.apiHost + "api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));
    }

    @Et("l'etudiant {string}")
    public void lEtudiant(String arg0) throws IOException {
        executePost(VerifyAuthorizations.apiHost + "api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_STUDENT));
                }}));

        UserRef user = userRefRepository.findByUsername(arg0).get();

        executePost(VerifyAuthorizations.apiHost + "api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));
    }


    @Et("le professeur {string} assigne au module {string}")
    public void leProfesseurAssigneAuModule(String arg0, String arg1) throws IOException {
        executePost(VerifyAuthorizations.apiHost + "api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_TEACHER));
                }}));
        UserRef user = userRefRepository.findByUsername(arg0).get();
        executePost(VerifyAuthorizations.apiHost + "api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));


        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);
    }

    @Et("l'etudiant {string} assigne au module {string}")
    public void lEtudiantAssigneAuModule(String arg0, String arg1) throws IOException {
        executePost(VerifyAuthorizations.apiHost + "api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_STUDENT));
                }}));

        UserRef user = userRefRepository.findByUsername(arg0).get();

        executePost(VerifyAuthorizations.apiHost + "api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));

        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);
    }

    @Etantdonné("le questionnaire {string} dans le module {string}")
    public void leQuestionnaireDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg0);
        if (questionnaire == null){
            questionnaire = new Questionnaire(arg0, "Questionnaire", 1);
        }
        questionnaireRepository.save(questionnaire);

        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }

    @Etantdonné("le code de retour est {int}")
    public void leCodeDeRetourEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Etantdonnéque("les tables sont videes")
    public void lesTablesSontVidees() throws IOException {
        executePost(VerifyAuthorizations.apiHost + "api/auth/signup",
                new SignupRequest("superUser", "superUser@test.fr", "password", new HashSet<>() {{
                    add(String.valueOf(auth.models.ERole.ROLE_ADMIN));
                }}));
        executePost(VerifyAuthorizations.apiHost + "api/auth/signin",
                new LoginRequest("superUser", "password"));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);
        executePost(VerifyAuthorizations.authHost+ "api/auth/clear",
                (String) map.get("accessToken"));

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

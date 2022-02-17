package fr.uca.springbootstrap;

import auth.models.ERole;
import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.Ressource;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.CodeRunner;
import fr.uca.api.models.questions.Question;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.CodeRunnerRepository;
import fr.uca.api.models.Module;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.CodeRunnerRequest;
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
    QuestionnaireRepository questionnaireRepository;


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

        UserRef student = userRefRepository.findByUsername(studentName).get();

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
    }

    @Quand("{string} soumet le questionnaire {string} du module {string} sq")
    public void soumetLeQuestionnaireDuModuleSq(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        Questionnaire questionnaire = null;
        for (Ressource ressource : module.getRessources()) {
            if (arg1.equalsIgnoreCase(ressource.getName())) {
                questionnaire = (Questionnaire) ressource;
                break;
            }
        }
        executePost(String.format(
                "http://localhost:8080/api/modules/%d/questionnaire/%d",
                module.getId(),
                questionnaire.getId()), jwt);
    }

    @Alors("le dernier status de réponse est {int} sq")
    public void leDernierStatusDeRéponseEstSq(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
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

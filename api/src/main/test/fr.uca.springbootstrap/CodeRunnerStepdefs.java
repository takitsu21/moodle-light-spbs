package fr.uca.springbootstrap;

import fr.uca.auth.controllers.AuthController;
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
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeRunnerStepdefs extends SpringIntegration {
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

    PasswordEncoder encoder;

    @Etantdonné("Un enseignant avec le nom de connexion {string} crn")
    public void unEnseignantAvecLeNomDeConnexionCrn(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("un module {string} qui a un enseignant {string} et un étudiant {string} crn")
    public void unModuleQuiAUnEnseignantEtUnÉtudiantCrn(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        User teacher = userRepository.findByUsername(arg1).get();
        User student = userRepository.findByUsername(arg2).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        student.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(teacher);
        userRepository.save(student);
        module.setParticipants(new HashSet<>() {{
            add(teacher);
            add(student);
        }});
        moduleRepository.save(module);
    }

    @Et("un module {string} qui a un enseignant {string} et un étudiant {string} et qui a la question numéro {int} {string} avec description {string} la réponse est {string} avec le test {string} dans le {string} crn")
    public void unModuleQuiAUnEnseignantEtUnÉtudiantEtQuiALaQuestionNuméroAvecDescriptionLaRéponseEstAvecLeTestQuestionNuméroCrn(
            String moduleName,
            String teacherName,
            String studentName,
            int num,
            String questionName,
            String descriptionName,
            String answerStr,
            String test,
            String questionnaireName) {

        Module module = moduleRepository.findByName(moduleName).orElse(new Module(moduleName));
        User teacher = userRepository.findByUsername(teacherName).get();
        User student = userRepository.findByUsername(studentName).
                orElse(new User(studentName, studentName + "@test.fr", encoder.encode(PASSWORD)));
        student.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
            add(roleRepository.findByName(ERole.ROLE_ADMIN).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        module.setParticipants(new HashSet<>() {{
            add(teacher);
            add(student);
        }});

        userRepository.save(student);
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
        executePost(String.format(
                        "http://localhost:8080/api/modules/%d/questionnaire/%d/code_runner/",
                        module.getId(),
                        questionnaire.getId()),
                new CodeRunnerRequest(1, arg1, arg2, arg3, arg4),
                jwtTeacher);
    }

    @Alors("le dernier status de réponse est {int} crn")
    public void leDernierStatusDeRéponseEstCrn(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
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
        User user = userRepository.findByUsername(arg0).get();
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
        String jwtStudent = authController.generateJwt(arg0, PASSWORD);
        InputStream is = getClass().getClassLoader().getResourceAsStream(arg1);

        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = is.read()) != -1; ) {
            sb.append((char) ch);
        }
        executePost(String.format(
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
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Boolean> map = gson.fromJson(jsonString, Map.class);
        Boolean b = arg0 == 1;
        assertEquals(b, map.get("success"));
    }

    @Et("la réponse est faux {int} crn")
    public void laRéponseEstFauxCrn(int arg0) throws IOException {
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Boolean> map = gson.fromJson(jsonString, Map.class);
        Boolean b = arg0 != 0;
        assertEquals(b, map.get("success"));
    }
}

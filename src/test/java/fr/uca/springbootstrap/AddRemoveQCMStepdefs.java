package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.ERole;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.QCMRequest;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import net.minidev.json.JSONUtil;
import org.python.antlr.op.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AddRemoveQCMStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Etantdonné("le professeur {string} assigné au module de {string} arqqq")
    public void leProfesseurAssignéAuModuleDeArqqq(String arg0, String arg1) {
        questionnaireRepository.deleteAll();
        questionRepository.deleteAll();
        User teacher = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        teacher.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(teacher);

        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(teacher);
        moduleRepository.save(module);
    }

    @Et("le professeur {string} sans module arqqq")
    public void leProfesseurSansModuleArqqq(String arg0) {
        User teacher = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        teacher.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(teacher);
    }


    @Et("le questionnaire {string} dans le module {string} arqqq")
    public void leQuestionnaireDansLeModuleArqqq(String arg0, String arg1) {
        Questionnaire questionnaire = questionnaireRepository.findByName(arg0)
                .orElse(new Questionnaire(arg0, "Controle intermediaire", 8));
        questionnaireRepository.save(questionnaire);
        Module module = moduleRepository.findByName(arg1).get();
        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }


    @Et("la question {string} dans le questionnaire {string} arqqq")
    public void laQuestionDansLeQuestionnaireArqqq(String arg0, String arg1) {
        Question question = questionRepository.findByName(arg0)
                .orElse(new QCM(1, arg0, "Quelle est la source d'energie d'une cellule?"));
        questionRepository.save(question);
        Questionnaire questionnaire = questionnaireRepository.findByName(arg1).get();
        questionnaire.addQuestion(question);
        questionnaireRepository.save(questionnaire);
    }

    @Quand("Le professeur {string} veut ajouter la question {string} au questionnaire {string} dans le module {string} arqqq")
    public void leProfesseurVeutAjouterLaQuestionAuQuestionnaireDansLeModuleArqqq(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Questionnaire questionnaire = questionnaireRepository.findByName(arg2).get();
        Module module = moduleRepository.findByName(arg3).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executePostWithBody("http://localhost:8080/api/module/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/qcm",
                new QCMRequest(2, arg1, "Deuxieme question"),
                jwtTeacher);
    }


    @Quand("Le professeur {string} veut supprimer la question {string} du questionnaire {string} dans le module {string} arqqq")
    public void leProfesseurVeutSupprimerLaQuestionDuQuestionnaireDansLeModuleArqqq(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Questionnaire questionnaire = questionnaireRepository.findByName(arg2).get();
        Module module = moduleRepository.findByName(arg3).get();
        Question question = questionRepository.findByName(arg1).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/module/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/question/" + question.getId(), jwtTeacher);
    }

    @Alors("la réponse est {int} arqqq")
    public void laRéponseEstArqqq(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question {string} n'existe pas dans le questionnaire {string} arqqq")
    public void laQuestionNExistePasDansLeQuestionnaireArqqq(String arg0, String arg1) {
        Questionnaire questionnaire = questionnaireRepository.findByName(arg1).get();
        boolean hasQuestion = false;
        for (Question question : questionnaire.getQuestions()) {
            if (question.getName().equals(arg0)) {
                hasQuestion = true;
            }
        }
        assertFalse(hasQuestion);
    }

    @Et("la question {string} existe dans le questionnaire {string} arqqq")
    public void laQuestionExisteDansLeQuestionnaireArqqq(String arg0, String arg1) {
        Question question = questionRepository.findByName(arg0).get();
        Questionnaire questionnaire = questionnaireRepository.findByName(arg1).get();
        boolean hasQuestion = false;
        for (Question questionnaireQuestion : questionnaire.getQuestions()) {
            if (questionnaireQuestion.getName().equals(question.getName())) {
                hasQuestion = true;
            }
        }

        assertTrue(hasQuestion);
    }
}

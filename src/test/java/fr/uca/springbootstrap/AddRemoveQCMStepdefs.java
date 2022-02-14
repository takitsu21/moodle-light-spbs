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
        Module module = moduleRepository.findByName(arg1).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg0);
        if (questionnaire==null){
            questionnaire=new Questionnaire(arg0, "Controle intermediaire", 8);
        }
        questionnaireRepository.save(questionnaire);

        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }
    @Et("la question {string} dans le questionnaire {string} du module {string} arqqq")
    public void laQuestionDansLeQuestionnaireDuModuleArqqq(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);

        Question question = questionnaire.findQuestionByName(arg0);

        if (question==null){
            question=new QCM(1, arg0, "Quelle est la source d'energie d'une cellule?");
        }

        questionRepository.save(question);

        questionnaire.addQuestion(question);
        questionnaireRepository.save(questionnaire);
    }

    @Quand("Le professeur {string} veut ajouter la question {string} au questionnaire {string} dans le module {string} arqqq")
    public void leProfesseurVeutAjouterLaQuestionAuQuestionnaireDansLeModuleArqqq(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/qcm",
                new QCMRequest(2, arg1, "Deuxieme question"),
                jwtTeacher);
    }


    @Quand("Le professeur {string} veut supprimer la question {string} du questionnaire {string} dans le module {string} arqqq")
    public void leProfesseurVeutSupprimerLaQuestionDuQuestionnaireDansLeModuleArqqq(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);
        Question question = questionnaire.findQuestionByName(arg1);

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/questions/" + question.getId(), jwtTeacher);
    }

    @Alors("la réponse est {int} arqqq")
    public void laRéponseEstArqqq(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question {string} existe dans le questionnaire {string} du module {string} arqqq")
    public void laQuestionExisteDansLeQuestionnaireDuModuleArqqq(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);

        assertTrue(questionnaire.containsQuestion(arg0));
    }


    @Et("la question {string} n'existe pas dans le questionnaire {string} du module {string} arqqq")
    public void laQuestionExisteDansLeQuestionnaireArqqq(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);

        assertFalse(questionnaire.containsQuestion(arg0));
    }


}

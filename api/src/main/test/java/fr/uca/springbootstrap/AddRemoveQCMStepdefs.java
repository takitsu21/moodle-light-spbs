package fr.uca.springbootstrap;

import auth.service.repository.RoleRepository;
import fr.uca.api.controllers.AuthController;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.QuestionRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    UserRefRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Et("la question {string} dans le questionnaire {string} du module {string} arqqq")
    public void laQuestionDansLeQuestionnaireDuModuleArqqq(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);

        Question question = questionnaire.findQuestionByName(arg0);

        if (question == null) {
            question = new QCM(1, arg0, "Quelle est la source d'energie d'une cellule?");
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

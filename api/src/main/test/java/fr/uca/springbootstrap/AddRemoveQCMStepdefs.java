package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.QCM;
import fr.uca.api.models.Module;
import fr.uca.api.models.questions.Question;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.QuestionRepository;
import fr.uca.api.util.VerifyAuthorizations;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.QCMRequest;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AddRemoveQCMStepdefs extends SpringIntegration {
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;


    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRefRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    UserRefRepository userRefRepository;


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

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executePost(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/qcm",
                new QCMRequest(2, arg1, "Deuxieme question"),
                jwt);
    }


    @Quand("Le professeur {string} veut supprimer la question {string} du questionnaire {string} dans le module {string} arqqq")
    public void leProfesseurVeutSupprimerLaQuestionDuQuestionnaireDansLeModuleArqqq(String arg0, String arg1, String arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);
        Question question = questionnaire.findQuestionByName(arg1);

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executeDelete(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId() + "/questions/" + question.getId(), jwt);
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

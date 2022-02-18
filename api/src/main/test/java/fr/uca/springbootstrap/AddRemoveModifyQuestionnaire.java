package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Module;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.util.VerifyAuthorizations;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import payload.request.QuestionnaireRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AddRemoveModifyQuestionnaire extends SpringIntegration {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    UserRefRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    UserRefRepository userRefRepository;


    @Et("un module {string}")
    public void unModuleSansLEnseignant(String arg0) {
        Module module = moduleRepository.findByName(arg0).
                orElse(new Module(arg0));
        moduleRepository.save(module);
    }

    @Quand("l'enseignant {string} veut ajouter le questionnaire {string} au module {string}")
    public void lEnseignantVeutAjouterLeQuestionnaireAuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executePost(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/questionnaire",
                new QuestionnaireRequest(arg1, "Plein de questions", 5),
                jwt);
    }


    @Et("le questionnaire {string} est dans le module {string}")
    public void leQuestionnaireEstDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();

        assertTrue(module.containsRessource(arg0));
    }


    @Et("le questionnaire {string} n'est pas dans le module {string}")
    public void leQuestionnaireNEstPasDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();

        assertFalse(module.containsRessource(arg0));
    }


    @Quand("l'enseignant {string} veut supprimer le questionnaire {string} du module {string}")
    public void lEnseignantVeutSupprimerLeQuestionnaireDuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executeDelete(VerifyAuthorizations.apiHost + "api/modules/"
                        + module.getId() + "/questionnaire/" + questionnaire.getId(),
                jwt);
    }

}
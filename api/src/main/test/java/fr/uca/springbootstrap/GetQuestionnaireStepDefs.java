package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.Module;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.util.VerifyAuthorizations;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetQuestionnaireStepDefs extends SpringIntegration {
    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    AuthController authController;


    @Et("le questionnaire {string} du module {string} est visible")
    public void leQuestionnaireDuModuleEstVisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg0);
        questionnaire.setVisibility(true);
        questionnaireRepository.save(questionnaire);
    }

    @Quand("l'utilisateur {string} récupère le questionnaire {string} du module {string}")
    public void lUtilisateurRécupèreLeQuestionnaireDuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire =  (Questionnaire) module.findRessourceByName(arg1);

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executeGet(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId(),
                jwt);
    }

    @Et("le questionnaire {string} est renvoyé")
    public void leQuestionnaireEstRenvoyé(String arg0) throws IOException {
        String json_question = EntityUtils.toString(latestHttpResponse.getEntity());
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Questionnaire questionnaire = gson.fromJson(json_question, Questionnaire.class);

        assertEquals(arg0, questionnaire.getName());
    }
}

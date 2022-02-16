package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.ERole;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
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
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetQuestionnaireStepDefs extends SpringIntegration {
    private static final String PASSWORD = "securedPassword";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Etantdonné("le professeur {string} assigné au module d'{string} aaq")
    public void leProfesseurAssignéAuModuleDAaq(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0)
                .orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);
    }

    @Et("le module {string} a un questionnaire {string} invisible aaq")
    public void leModuleAUnQuestionnaireInvisibleAaq(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).get();
        Questionnaire questionnaire = new Questionnaire(arg1, "Questionnaire", 1);
        questionnaireRepository.save(questionnaire);
        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }

    @Et("le module {string} a un questionnaire {string} visible aaq")
    public void leModuleAUnQuestionnaireVisibleAaq(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).get();
        Questionnaire questionnaire = new Questionnaire(arg1, "Another Questionnaire", 1);
        questionnaire.setVisibility(true);
        questionnaireRepository.save(questionnaire);
        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }

    @Et("le professeur {string} qui n'a aucun module aaq")
    public void leProfesseurQuiNAAucunModuleAaq(String arg0) {
        User user = userRepository.findByUsername(arg0)
                .orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("l'élève {string} est assigné au module {string} aaq")
    public void lÉlèveEstAssignéAuModuleAaq(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0)
                .orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
        Module module = moduleRepository.findByName(arg1).get();
        module.getParticipants().add(user);
        moduleRepository.save(module);
    }

    @Et("l'élève {string} assigné a aucun module aaq")
    public void lÉlèveAssignéAAucunModuleAaq(String arg0) {
        User user = userRepository.findByUsername(arg0)
                .orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Quand("l'utilisateur {string} récupère le questionnaire {string} du module {string}")
    public void lUtilisateurRécupèreLeQuestionnaireDuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire =  (Questionnaire) module.findRessourceByName(arg1);

        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeGet("http://localhost:8080/api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId(),
                jwt);
    }

    @Alors("la réponse recue est {int}")
    public void laRéponseRecueEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
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

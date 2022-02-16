package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.payload.request.QuestionnaireRequest;
import fr.uca.springbootstrap.repository.*;
import io.cucumber.java.fr.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AddRemoveModifyQuestionnaire extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Et("un module {string}")
    public void unModuleSansLEnseignant(String arg0) {
        Module module = moduleRepository.findByName(arg0).
                orElse(new Module(arg0));
        moduleRepository.save(module);
    }

    @Quand("l'enseignant {string} veut ajouter le questionnaire {string} au module {string}")
    public void lEnseignantVeutAjouterLeQuestionnaireAuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/questionnaire/",
                new QuestionnaireRequest(arg1, "Plein de questions", 5),
                jwtTeacher);
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

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/questionnaire/" + questionnaire.getId(), jwtTeacher);
    }

    @Alors("la réponse est {int}")
    public void laRéponseEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

}
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
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.fr.Étantdonné;

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

    @Étantdonné("L'enseignant avec le nom de connexion {string}")
    public void lEnseignantAvecLeNomDeConnexion(String arg0) {
        moduleRepository.deleteAll();
        questionnaireRepository.deleteAll();
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("le module {string}")
    public void leModule(String arg0) {
        Module module = moduleRepository.findByName(arg0)
                .orElse(new Module(arg0));
        moduleRepository.save(module);
    }

    @Et("le questionnaire {string}")
    public void leQuestionnaire(String arg0) {
        Questionnaire questionnaire = questionnaireRepository.findByName(arg0).
                orElse(new Questionnaire(arg0, "Questionnaire", 1));
        questionnaireRepository.save(questionnaire);
    }

    @Et("le questionnaire {string} dans le module {string}")
    public void leQuestionnaireDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire = questionnaireRepository.findByName(arg0).get();
//        Questionnaire questionnaire = null;
//        for (Ressource ressource : module.getRessources()) {
//            if (ressource.getName().equalsIgnoreCase(arg0)) {
//                questionnaire = (Questionnaire) ressource;
//            }
//        }

        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }

    @Et("un module {string} avec un enseignant {string}")
    public void unModuleAvecUnEnseignantEtUnQuestionnaire(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).get();
        User teacher = userRepository.findByUsername(arg1).get();

//        teacher.getModules().add(module);
//        userRepository.save(teacher);
        module.getParticipants().add(teacher);
        moduleRepository.save(module);
    }

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
        executePostWithBody("http://localhost:8080/api/module/" + module.getId() + "/questionnaire",
                new QuestionnaireRequest(arg1, "Plein de questions", 5),
                jwtTeacher);
    }

    @Et("le questionnaire {string} est dans le module {string}")
    public void leQuestionnaireEstDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire = null;
        for (Ressource ressource : module.getRessources()) {
            if (ressource.getName().equalsIgnoreCase(arg0)) {
                questionnaire = (Questionnaire) ressource;
            }
        }
        assertTrue(module.containsRessource(questionnaire));
    }

//    @Et("le module {string} est connu du questionnaire {string}")
//    public void leModuleEstConnuDuQuestionnaire(String arg0, String arg1) {
//        Questionnaire questionnaire = questionnaireRepository.findByName(arg1).get();
//        Module module = moduleRepository.findByName(arg0).get();
//
//        assertTrue(questionnaire.getModules().contains(module));
//    }

    @Et("le questionnaire {string} n'est pas dans le module {string}")
    public void leQuestionnaireNEstPasDansLeModule(String arg0, String arg1) {
//        Questionnaire questionnaire = questionnaireRepository.findByName(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire = null;
        for (Ressource ressource : module.getRessources()) {
            if (ressource.getName().equalsIgnoreCase(arg0)) {
                questionnaire = (Questionnaire) ressource;
            }
        }
        assertFalse(module.containsRessource(questionnaire));
    }

//    @Et("le module {string} n'est pas connu du questionnaire {string}")
//    public void leModuleNEstPasConnuDuQuestionnaire(String arg0, String arg1) {
//        Module module = moduleRepository.findByName(arg0).get();
//        Questionnaire questionnaire = questionnaireRepository.findByName(arg1).get();
//
//        assertFalse(questionnaire.getModules().contains(module));
//    }

    @Quand("l'enseignant {string} veut supprimer le questionnaire {string} du module {string}")
    public void lEnseignantVeutSupprimerLeQuestionnaireDuModule(String arg0, String arg1, String arg2) throws IOException {
        Questionnaire questionnaire = questionnaireRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/module/" + module.getId() + "/questionnaire/" + questionnaire.getId(), jwtTeacher);
    }


    @Etantdonnéque("le questionnaire {string} soit dans le module {string}")
    public void leQuestionnaireSoitDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire = questionnaireRepository.findByName(arg0).get();
//        for (Ressource ressource : module.getRessources()) {
//            if (ressource.getName().equalsIgnoreCase(arg0)) {
//                questionnaire = (Questionnaire) ressource;
//            }
//        }
        module.getRessources().add(questionnaire);
        moduleRepository.save(module);
    }

    @Etantdonnéque("l'enseignant {string} soit dans le module {string}")
    public void lEnseignantSoitDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User teacher = userRepository.findByUsername(arg0).get();
        module.getParticipants().add(teacher);
//        teacher.getModules().add(module);
        moduleRepository.save(module);
    }

    @Alors("la réponse est {int}")
    public void laRéponseEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }
}
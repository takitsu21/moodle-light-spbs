package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.Module;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.UserRefRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class TeacherAssignToAModuleTest extends SpringIntegration {
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    AuthController authController;

    @Et("un module {string} qui n'a pas de professeur")
    public void unModuleQuiNAPasDeProfesseur(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }

    @Quand("le professeur {string} essaie de s'assigner au module {string}")
    public void leProfesseurEssaieDeSAssignerAuModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + user.getId(), jwt);
    }

    @Alors("le professeur {string} est assigner à {string}")
    public void leProfesseurEstAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Alors("le professeur {string} n'est pas assigner à {string}")
    public void leProfesseurNEstPasAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}

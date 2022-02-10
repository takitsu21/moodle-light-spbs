package fr.uca.springbootstrap;

import fr.uca.springbootstrap.SpringIntegration;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import moodle.users.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeVisibilityOfRessourceTest extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Etantdonné("le professeur {string} assigné au module de {string} qui a une ressource {string}")
    public void leProfesseurAssignéAuModuleDeQuiAUneRessource(String arg0, String arg1, String arg2) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);

        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);
        
        assertTrue(module.getParticipants().contains(user));
    }

    @Et("le module {string} a une ressource {string} invisible")
    public void leModuleAUneRessourceInvisible(String arg0, String arg1) {
        Ressource ressource = ressourceRepository.findByName(arg1).
                orElse(new Cours(arg1, "null"));
        ressource.setVisibility(false);
        ressourceRepository.save(ressource);

        Module module = moduleRepository.findByName(arg0).get();
        module.getRessources().add(ressource);
        moduleRepository.save(module);

        assertTrue(module.getRessources().contains(ressource));
    }

    @Et("le module {string} a une ressource {string} visible")
    public void leModuleAUneRessourceVisible(String arg0, String arg1) {
        Ressource ressource = ressourceRepository.findByName(arg1).
                orElse(new Cours(arg1, "null"));
        ressource.setVisibility(true);
        ressourceRepository.save(ressource);

        Module module = moduleRepository.findByName(arg0).get();
        module.getRessources().add(ressource);
        moduleRepository.save(module);

        assertTrue(module.getRessources().contains(ressource));
    }

    @Et("le professeur {string} qui n'a aucun module")
    public void leProfesseurQuiNAAucunModule(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);
    }

    @Quand("le professeur {string} essaie de rendre la ressource {string} du module {string} visible")
    public void leProfesseurEssaieDeRendreLaRessourceDuModuleVisible(String arg0, String arg1, String arg2) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Ressource ressource = ressourceRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/module/"+module.getId()+"/ressourceVisible/"+ressource.getId(), jwt);
    }

    @Quand("le professeur {string} essaie de rendre la ressource {string} du module {string} invisible")
    public void leProfesseurEssaieDeRendreLaRessourceDuModuleInvisible(String arg0, String arg1, String arg2) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Ressource ressource = ressourceRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/module/"+module.getId()+"/ressourceInvisible/"+ressource.getId(), jwt);
    }

    @Et("le dernier status de request est {int} cv")
    public void leDernierStatusDeRequestEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Alors("la ressource {string} du module {string} est visible")
    public void laRessourceDuModuleEstVisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Ressource ressource = ressourceRepository.findByName(arg0).get();
        assertTrue(ressource.isVisibility());
    }

    @Alors("la ressource {string} du module {string} est invisible")
    public void laRessourceDuModuleEstInvisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Ressource ressource = ressourceRepository.findByName(arg0).get();
        assertFalse(ressource.isVisibility());
    }
}

package java.fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.payload.request.VisibilityRequest;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

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


    @Et("le module {string} a une ressource {string} invisible")
    public void leModuleAUneRessourceInvisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).get();

        Ressource ressource = module.findRessourceByName(arg1);
        if(ressource==null){
            ressource=new Cours(arg1, "null", 1);
        }
        ressource.setVisibility(false);
        ressourceRepository.save(ressource);

        module.getRessources().add(ressource);
        moduleRepository.save(module);

        assertTrue(module.getRessources().contains(ressource));
    }

    @Et("le module {string} a une ressource {string} visible")
    public void leModuleAUneRessourceVisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).get();

        Ressource ressource = module.findRessourceByName(arg1);
        if(ressource==null){
            ressource=new Cours(arg1, "null", 1);
        }
        ressource.setVisibility(true);
        ressourceRepository.save(ressource);

        module.getRessources().add(ressource);
        moduleRepository.save(module);

        assertTrue(module.getRessources().contains(ressource));
    }

    @Quand("le professeur {string} essaie de rendre la ressource {string} du module {string} visible")
    public void leProfesseurEssaieDeRendreLaRessourceDuModuleVisible(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Ressource ressource = module.findRessourceByName(arg1);
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/visibility/" + ressource.getId(),
                new VisibilityRequest(true),
                jwt);
    }

    @Quand("le professeur {string} essaie de rendre la ressource {string} du module {string} invisible")
    public void leProfesseurEssaieDeRendreLaRessourceDuModuleInvisible(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Ressource ressource = module.findRessourceByName(arg1);
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/visibility/" + ressource.getId(),
                new VisibilityRequest(false),
                jwt);
    }

    @Et("le dernier status de request est {int} cv")
    public void leDernierStatusDeRequestEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Alors("la ressource {string} du module {string} est visible")
    public void laRessourceDuModuleEstVisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Ressource ressource = module.findRessourceByName(arg0);
        assertTrue(ressource.isVisibility());
    }

    @Alors("la ressource {string} du module {string} est invisible")
    public void laRessourceDuModuleEstInvisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Ressource ressource = module.findRessourceByName(arg0);
        assertFalse(ressource.isVisibility());
    }
}
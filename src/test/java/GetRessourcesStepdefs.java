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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetRessourcesStepdefs  extends SpringIntegration {
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

    @Etantdonné("le professeur {string} assigné au module de {string} gr")
    public void leProfesseurAssignéAuModuleDeGr(String arg0, String arg1) {
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

    @Et("le module {string} a un cours {string} invisible gr")
    public void leModuleAUnCoursInvisibleGr(String arg0, String arg1) {
        Ressource ressource = ressourceRepository.findByName(arg1).
                orElse(new Cours(arg1, "null", 1));
        ressource.setVisibility(false);
        ressourceRepository.save(ressource);

        Module module = moduleRepository.findByName(arg0).get();
        module.getRessources().add(ressource);
        moduleRepository.save(module);

        assertTrue(module.getRessources().contains(ressource));
    }

    @Et("le module {string} a un cours {string} visible gr")
    public void leModuleAUnCoursVisibleGr(String arg0, String arg1) {
        Ressource ressource = ressourceRepository.findByName(arg1).
                orElse(new Cours(arg1, "null", 2));
        ressource.setVisibility(true);
        ressourceRepository.save(ressource);

        Module module = moduleRepository.findByName(arg0).get();
        module.getRessources().add(ressource);
        moduleRepository.save(module);

        assertTrue(module.getRessources().contains(ressource));
    }

    @Et("le professeur {string} qui n'a aucun module gr")
    public void leProfesseurQuiNAAucunModuleGr(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);
    }

    @Et("l'élève {string} est assigné au cours {string}")
    public void lÉlèveEstAssignéAuCours(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_STUDENT).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);

        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);

        assertTrue(module.getParticipants().contains(user));
    }

    @Et("l'élève {string} assigné a aucun module")
    public void lÉlèveAssignéAAucunModule(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_STUDENT).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);
    }

    @Quand("Le professeur {string} get les ressources du module {string}")
    public void leProfesseurGetLesRessourcesDuModule(String arg0, String arg1) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeGet("http://localhost:8080/api/module/"+module.getId()+"/ressources", jwt);
    }

    @Et("le dernier status de request est {int} gr")
    public void leDernierStatusDeRequestEstGr(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Alors("le cours {string} est renvoyé")
    public void leCoursEstRenvoyé(String arg0) {
        System.out.println(latestHttpResponse.getEntity());
    }

    @Alors("aucun cours n'est renvoyé")
    public void aucunCoursNEstRenvoyé() {
        System.out.println(latestHttpResponse.getEntity());
    }
}

package fr.uca.springbootstrap;

import fr.uca.auth.controllers.AuthController;
import fr.uca.api.models.ERole;
import fr.uca.api.models.Module;
import fr.uca.api.models.Role;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.RoleRepository;
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
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Etantdonné("un professeur {string}")
    public void unProfesseur(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("un module {string} qui n'a pas de professeur")
    public void unModuleQuiNAPasDeProfesseur(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }

    @Et("au module {string} qui a un professeur assigné")
    public void auModuleQuiAUnProfesseurAssigné(String arg0) {
        User user2 = userRepository.findByUsername("Teacher").
                orElse(new User("Teacher", "Teacher" + "@test.fr", encoder.encode(PASSWORD)));
        user2.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user2);

        Module module2 = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module2.getParticipants().add(user2);
        moduleRepository.save(module2);
    }


    @Quand("le professeur {string} essaie de s'assigner au module {string}")
    public void leProfesseurEssaieDeSAssignerAuModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + user.getId(), jwt);
    }

    @Et("le dernier status de request est {int}")
    public void leDernierStatusDeRequestEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Alors("le professeur {string} est assigner à {string}")
    public void leProfesseurEstAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Alors("le professeur {string} n'est pas assigner à {string}")
    public void leProfesseurNEstPasAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}

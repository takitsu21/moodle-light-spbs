package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TeacherAddAndRemoveUserFromAModuleTest {
    private final SpringIntegration springIntegration = SpringIntegration.getInstance();

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


    @Quand("le professeur {string} essaie d'assigner le professeur {string} au module {string}")
    public void leProfesseurEssaieDAssignerLeProfesseurAuModule(String arg0, String arg1, String arg2) throws IOException {
        User prof2 = userRepository.findByUsername(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        springIntegration.executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + prof2.getId(), jwt);
    }

    @Quand("le professeur {string} essaie de retirer le professeur {string} au module {string}")
    public void leProfesseurEssaieDeRetirerLeProfesseurAuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        User student = userRepository.findByUsername(arg1).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);

        springIntegration.executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + student.getId(), jwtTeacher);
    }

    @Quand("le professeur {string} essaie d assigner l élève {string} au module {string}")
    public void leProfesseurEssaieDAssignerLÉlèveAuModule(String arg0, String arg1, String arg2) throws IOException {
        User user = userRepository.findByUsername(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        springIntegration.executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + user.getId(), jwt);
    }


    @Quand("le professeur {string} essaie de retirer l élève {string} au module {string}")
    public void leProfesseurEssaieDeRetirerLÉlèveAuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        User student = userRepository.findByUsername(arg1).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);

        springIntegration.executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + student.getId(), jwtTeacher);
    }

    @Alors("{string} est assigner à {string}")
    public void estAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Alors("{string} n'est pas assigner à {string}")
    public void nEstPasAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}

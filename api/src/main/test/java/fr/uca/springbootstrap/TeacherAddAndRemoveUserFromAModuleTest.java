package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.models.Module;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherAddAndRemoveUserFromAModuleTest extends SpringIntegration {
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    AuthController authController;



    @Quand("le professeur {string} essaie d'assigner le professeur {string} au module {string}")
    public void leProfesseurEssaieDAssignerLeProfesseurAuModule(String arg0, String arg1, String arg2) throws IOException {
        UserRef prof2 = userRefRepository.findByUsername(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + prof2.getId(), jwt);
    }

    @Quand("le professeur {string} essaie de retirer le professeur {string} au module {string}")
    public void leProfesseurEssaieDeRetirerLeProfesseurAuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        UserRef student = userRefRepository.findByUsername(arg1).get();

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + student.getId(), jwt);
    }

    @Quand("le professeur {string} essaie d assigner l élève {string} au module {string}")
    public void leProfesseurEssaieDAssignerLÉlèveAuModule(String arg0, String arg1, String arg2) throws IOException {
        UserRef user = userRefRepository.findByUsername(arg1).get();
        Module module = moduleRepository.findByName(arg2).get();
        UserRef user1 = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user1.getUsername());
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + user.getId(), jwt);
    }


    @Quand("le professeur {string} essaie de retirer l élève {string} au module {string}")
    public void leProfesseurEssaieDeRetirerLÉlèveAuModule(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        UserRef student = userRefRepository.findByUsername(arg1).get();

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + student.getId(), jwt);
    }

    @Alors("{string} est assigner à {string}")
    public void estAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Alors("{string} n'est pas assigner à {string}")
    public void nEstPasAssignerÀ(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}

package java.fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleStudentAssignationStepdefs extends SpringIntegration {
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


    @Et("l enseignant inscrit l'étudiant {string} au module {string}")
    public void lEnseignantInscritLÉtudiantAuModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User student = userRepository.findByUsername(arg0).get();
        module.getParticipants().add(student);
        moduleRepository.save(module);
    }

    @Quand("l'enseignant {string} veut ajouter {string} au module de {string} arm")
    public void lEnseignantVeutAjouterAuModuleDeArm(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        User student = userRepository.findByUsername(arg1).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + student.getId(), jwtTeacher);

    }

    @Alors("le dernier status de réponse est {int} arm")
    public void leDernierStatusDeRéponseEstArm(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("l'étudiant {string} est ajouter au module {string} arm")
    public void lÉtudiantEstAjouterAuModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Quand("l'enseignant {string} veut enlever {string} du module de {string} arm")
    public void lEnseignantVeutEnleverDuModuleDeArm(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        User student = userRepository.findByUsername(arg1).get();

        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);

        executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + student.getId(), jwtTeacher);
    }

    @Et("{string} est enlever du module {string} arm")
    public void estEnleverDuModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }

    @Et("{string} est déjà dans le module {string} arm")
    public void estDéjàDansLeModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Et("{string} n'est pas dans le module {string} arm")
    public void nEstPasDansLeModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }


}

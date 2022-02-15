package fr.uca.springbootstrap;

import fr.uca.auth.controllers.AuthController;
import fr.uca.api.models.ERole;
import fr.uca.api.models.Module;
import fr.uca.api.models.Role;
import fr.uca.api.models.User;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.RoleRepository;
import fr.uca.api.repository.UserRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

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

    @Etantdonné("Un enseignant avec le nom de connexion {string}")
    public void unEnseignantAvecLeNomDeConnexion(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("un étudiant {string}")
    public void unEtudiant(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("un module {string} qui a un enseignant {string}")
    public void unModuleQuiAUnEnseignant(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        User teacher = userRepository.findByUsername(arg1).get();
        module.setParticipants(new HashSet<>() {{
            add(teacher);
        }});
        moduleRepository.save(module);
    }

    @Et("un module {string} qui a un enseignant {string} et un étudiant {string}")
    public void unModuleQuiAUnEnseignantEtUnÉtudiant(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        User teacher = userRepository.findByUsername(arg1).get();
        User student = userRepository.findByUsername(arg2).get();

        module.setParticipants(new HashSet<>() {{
            add(teacher);
            add(student);
        }});
        moduleRepository.save(module);
    }

    @Et("l enseignant inscrit l'étudiant {string} au module {string}")
    public void lEnseignantInscritLÉtudiantAuModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        User student = userRepository.findByUsername(arg0).get();
        module.getParticipants().add(student);
        moduleRepository.save(module);
    }

    @Quand("l'enseignant {string} veut ajouter {string} au module de {string} arm")
    public void lEnseignantVeutAjouterAuModuleDeArm(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        User teacher = userRepository.findByUsername(arg0).get();
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
        User teacher = userRepository.findByUsername(arg0).get();
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

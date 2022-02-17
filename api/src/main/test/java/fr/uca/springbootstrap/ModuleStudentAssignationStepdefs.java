package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.models.Module;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleStudentAssignationStepdefs extends SpringIntegration {
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    AuthController authController;


    @Et("l enseignant inscrit l'étudiant {string} au module {string}")
    public void lEnseignantInscritLÉtudiantAuModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef student = userRefRepository.findByUsername(arg0).get();
        module.getParticipants().add(student);
        moduleRepository.save(module);
    }

    @Quand("l'enseignant {string} veut ajouter {string} au module de {string} arm")
    public void lEnseignantVeutAjouterAuModuleDeArm(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        UserRef student = userRefRepository.findByUsername(arg1).get();

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executePost("http://localhost:8080/api/modules/"
                + module.getId() + "/participants/" + student.getId(), jwt);

    }

    @Et("l'étudiant {string} est ajouter au module {string} arm")
    public void lÉtudiantEstAjouterAuModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Quand("l'enseignant {string} veut enlever {string} du module de {string} arm")
    public void lEnseignantVeutEnleverDuModuleDeArm(String arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        UserRef student = userRefRepository.findByUsername(arg1).get();

        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + student.getId(), jwt);
    }

    @Et("{string} est enlever du module {string} arm")
    public void estEnleverDuModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }

    @Et("{string} est déjà dans le module {string} arm")
    public void estDéjàDansLeModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @Et("{string} n'est pas dans le module {string} arm")
    public void nEstPasDansLeModuleArm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }


}

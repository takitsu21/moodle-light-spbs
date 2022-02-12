package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.payload.request.MyText;
import fr.uca.springbootstrap.payload.request.TextRequest;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.cours.TextRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AddTextToCourseStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    TextRepository textRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Etantdonné("le professeur {string} assigné au module de {string} atc")
    public void leProfesseurAssignéAuModuleDeAtc(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);

        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);

        assertTrue(module.getParticipants().contains(user));
    }

    @Et("le module de {string} a une ressource {string} qui a un texte {int} de contenu {string}")
    public void leModuleDeAUneRessourceQuiAUnTexteDeContenu(String arg0, String arg1, int arg2, String arg3) {
        Module module = moduleRepository.findByName(arg0).get();

        Cours ressource = (Cours) module.findRessourceByName(arg1);
        if (ressource==null){
            ressource=new Cours(arg1, "null", 1);
            coursRepository.save(ressource);
        }

        Text text;
        if (!ressource.containsText(arg2)) {
            text = new Text(arg2, arg3);
            textRepository.save(text);
            ressource.getTexts().add(text);
        }
        coursRepository.save(ressource);


        module.getRessources().add(ressource);
        moduleRepository.save(module);

        assertTrue(module.getRessources().contains(ressource));
    }

    @Et("le professeur {string} qui n'a aucun module atc")
    public void leProfesseurQuiNAAucunModuleAtc(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Quand("Le professeur {string} retire un text {int} a la ressource {string} du module {string}")
    public void leProfesseurRetireUnTextALaRessourceDuModule(String arg0, int arg1, String arg2, String arg3) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Cours cours = coursRepository.findByName(arg2).get();
        Text text = cours.getText(arg1);
        Module module = moduleRepository.findByName(arg3).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/module/" + module.getId() + "/cours/" + cours.getId() + "/texts/" + text.getId(), jwt);
    }

    @Quand("Le professeur {string} ajoute un text {int} de contenu {string} a la ressource {string} du module {string}")
    public void leProfesseurAjouteUnTextDeContenuALaRessourceDuModule(String arg0, int arg1, String arg2, String arg3, String arg4) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg4).get();
        Cours cours = coursRepository.findByName(arg3).get();

        MyText text = new MyText(arg1, arg2);
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/module/" + module.getId() + "/cours/" + cours.getId(),
                new TextRequest(text), jwt);
    }

    @Et("le dernier status de request est {int} at")
    public void leDernierStatusDeRequestEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Alors("le text {int} de la ressource {string} n'est pas dans le module {string}")
    public void leTextDeLaRessourceNEstPasDansLeModule(int arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Cours cours = coursRepository.findByName(arg1).get();
        assertFalse(cours.containsText(arg0));
    }

    @Alors("le text {int} de la ressource {string} est dans le module {string}")
    public void leTextDeLaRessourceEstDansLeModule(int arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();
        Cours cours = coursRepository.findByName(arg1).get();
        assertTrue(cours.containsText(arg0));
    }
}

package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Cours;
import fr.uca.api.models.Text;
import fr.uca.api.models.Module;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.cours.TextRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.MyText;
import payload.request.TextRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AddTextToCourseStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    TextRepository textRepository;

    @Autowired
    AuthController authController;



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

    @Quand("Le professeur {string} retire un text {int} a la ressource {string} du module {string}")
    public void leProfesseurRetireUnTextALaRessourceDuModule(String arg0, int arg1, String arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        Cours cours = (Cours) module.findRessourceByName(arg2);
        Text text = cours.getText(arg1);
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executeDelete("http://localhost:8080/api/modules/" + module.getId() + "/cours/" + cours.getId() + "/texts/" + text.getId(), jwt);
    }

    @Quand("Le professeur {string} ajoute un text {int} de contenu {string} a la ressource {string} du module {string}")
    public void leProfesseurAjouteUnTextDeContenuALaRessourceDuModule(String arg0, int arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        Cours cours = (Cours) module.findRessourceByName(arg3);

        MyText text = new MyText(arg1, arg2);
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/cours/" + cours.getId() + "/texts/",
                new TextRequest(text), jwt);
    }

    @Alors("le text {int} de la ressource {string} n'est pas dans le module {string}")
    public void leTextDeLaRessourceNEstPasDansLeModule(int arg0, String arg1, String arg2) throws IOException {
        Module module = moduleRepository.findByName(arg2).get();
        Cours cours = (Cours) module.findRessourceByName(arg1);
        assertFalse(cours.containsText(arg0));
    }

    @Alors("le text {int} de la ressource {string} est dans le module {string}")
    public void leTextDeLaRessourceEstDansLeModule(int arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();
        Cours cours = (Cours) module.findRessourceByName(arg1);
        assertTrue(cours.containsText(arg0));
    }
}

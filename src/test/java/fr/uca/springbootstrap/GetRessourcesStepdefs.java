package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GetRessourcesStepdefs {
    private final SpringIntegration springIntegration = SpringIntegration.getInstance();

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

    @Et("le module {string} a un cours {string} invisible gr")
    public void leModuleAUnCoursInvisibleGr(String arg0, String arg1) {
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

    @Et("le module {string} a un cours {string} visible gr")
    public void leModuleAUnCoursVisibleGr(String arg0, String arg1) {
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

    @Quand("Le professeur {string} get les ressources du module {string}")
    public void leProfesseurGetLesRessourcesDuModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        springIntegration.executeGet("http://localhost:8080/api/modules/" + module.getId() + "/ressources", jwt);
    }

    @Alors("le cours {string} est renvoyé")
    public void leCoursEstRenvoyé(String arg0) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.containsValue(arg0));
    }

    @Alors("aucun cours n'est renvoyé")
    public void aucunCoursNEstRenvoyé() throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.containsKey("message"));
    }

    @Alors("les cours {string} et {string} sont renvoyé")
    public void lesCoursEtSontRenvoyé(String arg0, String arg1) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.containsValue(arg0));
        assertTrue(map.containsValue(arg1));
    }
}

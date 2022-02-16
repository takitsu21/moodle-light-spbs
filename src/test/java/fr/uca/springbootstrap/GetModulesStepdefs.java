package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
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
public class GetModulesStepdefs {
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


    @Quand("L'utilisateur {string} get ses modules")
    public void lUtilisateurGetSesModules(String arg0) throws IOException {
        String jwt = authController.generateJwt(arg0, PASSWORD);
        springIntegration.executeGet("http://localhost:8080/api/modules/", jwt);
    }

    @Alors("les modules sont {string} et {string}")
    public void lesModulesSontEt(String arg0, String arg1) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.containsValue(arg0));
        assertTrue(map.containsValue(arg1));
    }

    @Alors("il n'y a pas de module")
    public void ilNYAPasDeModule() throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.keySet().isEmpty());
    }

    @Alors("le module est {string}")
    public void leModuleEst(String arg0) throws IOException {
        String jsonString = EntityUtils.toString(springIntegration.getLatestHttpResponse().getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.containsValue(arg0));
    }

}

package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.models.Module;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetParticipantsStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Quand("L'utilisateur {string} get les participants du module {string}")
    public void lUtilisateurGetLesParticipantsDuModule(String arg0, String arg1) throws IOException {
        Module module = moduleRepository.findByName(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeGet("http://localhost:8080/api/modules/" + module.getId() + "/participants", jwt);
    }

    @Et("le dernier status de request est {int} gp")
    public void leDernierStatusDeRequestEstGp(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Alors("les participants sont {string} et {string}")
    public void lesParticipantsSontEt(String arg0, String arg1) throws IOException {
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.containsValue(arg0));
        assertTrue(map.containsValue(arg1));
    }

    @Alors("il n'y a pas de participant")
    public void ilNYAPasDeParticipant() throws IOException {
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(jsonString, Map.class);

        assertTrue(map.containsKey("message"));
    }

}

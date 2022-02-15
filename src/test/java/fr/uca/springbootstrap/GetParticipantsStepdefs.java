package fr.uca.springbootstrap;

import fr.uca.springbootstrap.models.ERole;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetParticipantsStepdefs extends SpringIntegration {
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

    @Etantdonné("le professeur {string} assigné au module de {string} gp")
    public void leProfesseurAssignéAuModuleDeGp(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);

        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);

        assertTrue(module.getParticipants().contains(user));
    }

    @Et("le professeur {string} qui n'a aucun module gp")
    public void leProfesseurQuiNAAucunModuleGp(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("l'élève {string} est assigné au cours {string} gp")
    public void lÉlèveEstAssignéAuCoursGp(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);

        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);

        assertTrue(module.getParticipants().contains(user));
    }

    @Et("l'élève {string} assigné a aucun module gp")
    public void lÉlèveAssignéAAucunModuleGp(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Quand("L'utilisateur {string} get les participants du module {string}")
    public void lUtilisateurGetLesParticipantsDuModule(String arg0, String arg1) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeGet("http://localhost:8081/api/modules/" + module.getId() + "/participants", jwt);
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

//        jsonString = gson.toJson(map);
//        System.out.println(jsonString);
        assertTrue(map.containsKey("message"));
    }

}

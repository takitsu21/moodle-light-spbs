package fr.uca.springbootstrap;

import auth.models.ERole;
import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.models.Module;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTeacherStepDefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    @Given("a teacher with login {string}")
    public void aTeacherWithLogin(String arg0) throws IOException {
        executePost("http://localhost:8080/api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_TEACHER));
                }}));
    }

    @And("a module named {string}")
    public void aModuleNamed(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }

    @WithUserDetails("stevyy")
    @When("{string} registers to module {string}")
    public void registersToModule(String arg0, String arg1) throws Exception {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

//        executePost("http://localhost:8080/api/test/mod", jwt);
//        executePost("http://localhost:8080/api/modules/1/participants/7", jwt);
        executePost("http://localhost:8080/api/modules/" + module.getId() + "/participants/" + user.getId(), jwt);
    }

    @Then("last request status is {int}")
    public void isRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("{string} is registered to module {string}")
    public void isRegisteredToModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertTrue(module.getParticipants().contains(user));
    }

    @And("{string} is not registered to module {string}")
    public void isNotRegisteredToModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}

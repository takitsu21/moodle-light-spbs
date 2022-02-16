package fr.uca.springbootstrap;

import io.cucumber.java.fr.Etantdonné;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckResponseStepdefs {
    private SpringIntegration springIntegration = SpringIntegration.getInstance();

    @Etantdonné("le code de retour est {int}")
    public void leCodeDeRetourEst(int arg0) {
        assertEquals(arg0, springIntegration.getLatestHttpResponse().getStatusLine().getStatusCode());
    }
}

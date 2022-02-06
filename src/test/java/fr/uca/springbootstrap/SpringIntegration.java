package fr.uca.springbootstrap;

import java.io.IOException;

import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.http.client.methods.HttpPost;


@CucumberContextConfiguration
@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringIntegration {
    static ResponseResults latestResponse = null;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    protected HttpResponse latestHttpResponse;

    void executeGet(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        latestHttpResponse = httpClient.execute(request);
    }

    void executePost(String url, String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity("{}"));
        latestHttpResponse = httpClient.execute(request);
    }
}
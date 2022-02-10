package fr.uca.springbootstrap;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.http.client.methods.HttpPost;


@CucumberContextConfiguration
@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringIntegration {
    static ResponseResults latestResponse = null;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    protected HttpResponse latestHttpResponse;

    public void executeGet(String url, String jwt) throws IOException {
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        latestHttpResponse = httpClient.execute(request);
    }

    public void executePost(String url, String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        request.setEntity(new StringEntity("{}"));
        latestHttpResponse = httpClient.execute(request);
    }

    public void executePostWithBody(String url, Object entity, String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        ObjectMapper ObjMapper = new ObjectMapper();
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        latestHttpResponse = httpClient.execute(request);
    }

    public void executeDelete(String url, String jwt) throws IOException {
        HttpDelete request = new HttpDelete(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        latestHttpResponse = httpClient.execute(request);
    }

    public void executePostWithBody(String url, Object entity, String jwt) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        ObjectMapper ObjMapper = new ObjectMapper();
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        latestHttpResponse = httpClient.execute(request);
    }
}
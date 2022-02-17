package fr.uca.springbootstrap;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uca.api.SpringBootSecurityPostgresqlApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.test.context.SpringBootTest;


@CucumberContextConfiguration
@SpringBootTest(classes = SpringBootSecurityPostgresqlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringIntegration {
    protected static Map<String, String> userToken = new HashMap<>();
    static ResponseResults latestResponse = null;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    protected HttpResponse latestHttpResponse;

    public void executeGet(String url, String jwt) throws IOException {
        consume();
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        latestHttpResponse = httpClient.execute(request);
    }

    public void executePost(String url, String jwt) throws IOException {
        consume();
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        request.setEntity(new StringEntity("{}"));
        latestHttpResponse = httpClient.execute(request);
    }

    public void executePost(String url, Object entity, String jwt) throws IOException {
        consume();
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        ObjectMapper ObjMapper = new ObjectMapper();
        ObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        latestHttpResponse = httpClient.execute(request);
    }

    public void executePost(String url, Object entity) throws IOException {
        consume();
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        ObjectMapper ObjMapper = new ObjectMapper();
        ObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        latestHttpResponse = httpClient.execute(request);
        request.completed();
    }


    public void executePut(String url, String jwt) throws IOException {
        HttpPut request = new HttpPut(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity("{}"));
        latestHttpResponse = httpClient.execute(request);
    }

    public void executePut(String url, Object entity, String jwt) throws IOException {
        consume();
        HttpPut request = new HttpPut(url);
        request.addHeader("content-type", "application/json");
        ObjectMapper ObjMapper = new ObjectMapper();
        ObjMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        latestHttpResponse = httpClient.execute(request);
    }

    public void executeDelete(String url, String jwt) throws IOException {
        consume();
        HttpDelete request = new HttpDelete(url);
        request.addHeader("content-type", "application/json");
        if (jwt != null) {
            request.addHeader("Authorization", "Bearer " + jwt);
        }

        latestHttpResponse = httpClient.execute(request);
    }
}
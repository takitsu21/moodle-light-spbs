package fr.uca.springbootstrap;

import fr.uca.api.models.ERole;
import fr.uca.api.models.Module;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.UserRefRepository;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import payload.request.LoginRequest;
import payload.request.SignupRequest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

public class CreateAddUserToModuleStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";


    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    ModuleRepository moduleRepository;


    @Etantdonné("le professeur {string}")
    public void leProfesseur(String arg0) throws IOException {
        System.out.println("tout premier");
        executePost("http://localhost:8080/api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_TEACHER));
                }}));

        System.out.println(EntityUtils.toString(latestHttpResponse.getEntity()));

        UserRef user = userRefRepository.findByUsername(arg0).get();

        executePost("http://localhost:8080/api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());
        System.out.println(jsonString);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));


    }

    @Et("l'etudiant {string}")
    public void lEtudiant(String arg0) throws IOException {
        executePost("http://localhost:8080/api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_STUDENT));
                }}));

        UserRef user = userRefRepository.findByUsername(arg0).get();

        executePost("http://localhost:8080/api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));
    }


    @Et("le professeur {string} assigne au module {string}")
    public void leProfesseurAssigneAuModule(String arg0, String arg1) throws IOException {
        executePost("http://localhost:8080/api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_TEACHER));
                }}));

        UserRef user = userRefRepository.findByUsername(arg0).get();

        executePost("http://localhost:8080/api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());
        System.out.println(jsonString);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));


        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);
        System.out.println("ares requet");
    }

    @Et("l'etudiant {string} assigne au module {string}")
    public void lEtudiantAssigneAuModule(String arg0, String arg1) throws IOException {
        executePost("http://localhost:8080/api/auth/signup",
                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
                    add(String.valueOf(ERole.ROLE_STUDENT));
                }}));

        UserRef user = userRefRepository.findByUsername(arg0).get();

        executePost("http://localhost:8080/api/auth/signin",
                new LoginRequest(arg0, PASSWORD));

        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());
        System.out.println(jsonString);

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        userToken.put(user.getUsername(), (String) map.get("accessToken"));

        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);
    }
}

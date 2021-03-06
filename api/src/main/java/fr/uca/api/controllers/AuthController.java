package fr.uca.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.util.VerifyAuthorizations;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payload.request.LoginRequest;
import payload.request.SignupRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static HttpResponse latestHttpResponse;

    @Autowired
    UserRefRepository userRepository;

    public static void executePost(String url, Object entity) throws IOException {
        if (latestHttpResponse != null) {
            EntityUtils.consume(latestHttpResponse.getEntity());
        }
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        ObjectMapper ObjMapper = new ObjectMapper();
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));

        latestHttpResponse = httpClient.execute(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws IOException {
        executePost(VerifyAuthorizations.authHost+ "api/auth/signin", loginRequest);
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        map.put("username", loginRequest.getUsername());
        EntityUtils.consume(latestHttpResponse.getEntity());

        return ResponseEntity.ok(map);
    }

    public UserRef createUser(Integer id, String userName) {
        //        Set<RoleCourses> roleCourses = new HashSet<>();

//        if (strRoles == null) {
//            RoleCourses userRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_STUDENT)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roleCourses.add(userRoleCourses);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role.toLowerCase()) {
//                    case "admin":
//                        RoleCourses adminRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roleCourses.add(adminRoleCourses);
//
//                        break;
//                    case "teacher":
//                        RoleCourses modRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_TEACHER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roleCourses.add(modRoleCourses);
//
//                        break;
//                    default:
//                        RoleCourses userRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_STUDENT)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roleCourses.add(userRoleCourses);
//                }
//            });
//        }
//        user.setRoles(roleCourses);
        return new UserRef(id, userName);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException {
        executePost(
                VerifyAuthorizations.authHost+ "api/auth/signup",
                signUpRequest);
        if (latestHttpResponse.getStatusLine().getStatusCode() == 400) {
            return ResponseEntity.status(latestHttpResponse.getStatusLine().getStatusCode()).body(latestHttpResponse.getEntity());
        }
        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);

        // Create new user's account
        UserRef user = createUser(
                ((Double) map.get("id")).intValue(),
                signUpRequest.getUsername());
        userRepository.save(user);
        return ResponseEntity.ok(map);
    }
}

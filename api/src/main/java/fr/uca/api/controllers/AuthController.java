package fr.uca.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.uca.api.models.ERole;
import fr.uca.api.models.RoleCourses;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.RoleCoursesRepository;
import fr.uca.api.repository.UserRefRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import payload.request.LoginRequest;
import payload.request.SignupRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRefRepository userRepository;
    @Autowired
    RoleCoursesRepository roleCoursesRepository;


    public CloseableHttpResponse executePost(String url, Object entity) throws IOException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", "application/json");
        ObjectMapper ObjMapper = new ObjectMapper();
        request.setEntity(new StringEntity(ObjMapper.writeValueAsString(entity)));
        return httpClient.execute(request);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws IOException {
//        String jwt = generateJwt(loginRequest.getUsername(), loginRequest.getPassword());
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());

        CloseableHttpResponse resp = executePost("http://localhost:8081/auth/signin", loginRequest);
        String jsonString = EntityUtils.toString(resp.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);


        return ResponseEntity.ok(map);
    }

    public UserRef createUser(Integer id, String userName, Set<String> strRoles) {
        UserRef user = new UserRef(id, userName);
        Set<RoleCourses> roleCourses = new HashSet<>();

        if (strRoles == null) {
            RoleCourses userRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roleCourses.add(userRoleCourses);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RoleCourses adminRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roleCourses.add(adminRoleCourses);

                        break;
                    case "teacher":
                        RoleCourses modRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roleCourses.add(modRoleCourses);

                        break;
                    default:
                        RoleCourses userRoleCourses = roleCoursesRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roleCourses.add(userRoleCourses);
                }
            });
        }
        user.setRoles(roleCourses);
        return user;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException {
        CloseableHttpResponse resp = executePost(
                "http://localhost:8081/auth/signin",
                signUpRequest);
        String jsonString = EntityUtils.toString(resp.getEntity());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(jsonString, Map.class);
        System.out.println(map);

        // Create new user's account
        UserRef user = createUser((Integer) map.get("id"),
                signUpRequest.getUsername(),
                signUpRequest.getRole());
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}

package fr.uca.springbootstrap;

import fr.uca.springbootstrap.models.ERole;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

public class CreateAddUserToModuleStepdefs {
    private static final String PASSWORD = "password";


    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    ModuleRepository moduleRepository;


    @Etantdonné("le professeur {string}")
    public void leProfesseur(String arg0) throws IOException {
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
    public void lEtudiant(String arg0) {
        User student = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        student.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(student);
    }


    @Et("le professeur {string} assigne au module {string}")
    public void leProfesseurAssigneAuModule(String arg0, String arg1) {
        User teacher = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        teacher.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(teacher);

        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);
    }

    @Et("l'etudiant {string} assigne au module {string}")
    public void lEtudiantAssigneAuModule(String arg0, String arg1) {
        User student = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        student.setRoles(new HashSet<>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(student);

        Module module = moduleRepository.findByName(arg1)
                .orElse(new Module(arg1));
        module.getParticipants().add(student);
        moduleRepository.save(module);
    }
}

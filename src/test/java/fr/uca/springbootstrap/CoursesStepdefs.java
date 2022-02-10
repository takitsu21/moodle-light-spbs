package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.payload.request.CoursRequest;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class CoursesStepdefs extends SpringIntegration {
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
    CoursRepository coursRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    private Cours findCoursByNameInModule(Module module, String name) {
        for (Ressource cours : module.getRessources()) {
            if (cours.getName().equalsIgnoreCase(name)) {
                return (Cours) cours;
            }
        }
        return null;
    }

    @Etantdonné("Un enseignant avec le nom de connexion {string} crs")
    public void unEnseignantAvecLeNomDeConnexionCrs(String arg0) {
        coursRepository.deleteAll();
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_TEACHER).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);
    }

    @Et("un module {string} qui a un enseignant {string} crs")
    public void unModuleQuiAUnEnseignantCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        User teacher = userRepository.findByUsername(arg1).get();
        module.setParticipants(new HashSet<>() {{
            add(teacher);
        }});
        moduleRepository.save(module);
    }

    @Et("un module {string} qui a un cours {string} et numéro {int} et qui a un enseignant {string} crs")
    public void unModuleQuiAUnCoursEtNuméroEtQuiAUnEnseignantCrs(String arg0, String arg1, int arg2, String arg3) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        Cours cours = coursRepository.findByName(arg1).orElse(new Cours(arg1, "description", arg2));
//        Cours cours = findCoursByNameInModule(module, arg1);
        User teacher = userRepository.findByUsername(arg3).get();

        coursRepository.save(cours);
        module.setParticipants(new HashSet<>() {{
            add(teacher);
        }});

        module.getRessources().add(cours);
        moduleRepository.save(module);
    }

    @Quand("{string} veut ajouter le cours {string} qui a pour description {string} et numéro {int} au module {string} crs")
    public void veutAjouterLeCoursQuiAPourDescriptionEtNuméroAuModuleCrs(String arg0, String arg1, String arg2, int arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);

        executePostWithBody("http://localhost:8080/api/module/" + module.getId() + "/cours",
                new CoursRequest(arg1, arg2, arg3),
                jwtTeacher);
    }

    @Alors("le dernier status de réponse est {int} crs")
    public void leDernierStatusDeRéponseEstCrs(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("{string} est ajouté au module {string} crs")
    public void estAjoutéAuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
//        Cours cours = coursRepository.findByName(arg0).get();

        Cours cours = findCoursByNameInModule(module, arg0);
        System.out.println(module.getRessources());

        assertTrue(module.getRessources().contains(cours));
    }

    @Quand("{string} veut supprimer le cours {string} et numéro {int} du module {string} crs")
    public void veutSupprimerLeCoursEtNuméroDuModuleCrs(String arg0, String arg1, int arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
//        Cours cours = coursRepository.findByName(arg1).get();
        Cours cours = findCoursByNameInModule(module, arg1);
        System.out.println(module.getRessources());


        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);
        if (cours == null) {
            executeDelete("http://localhost:8080/api/module/" + module.getId() + "/cours/-1",
                    jwtTeacher);
        } else {
            executeDelete("http://localhost:8080/api/module/" + module.getId() + "/cours/" + cours.getId(),
                    jwtTeacher);
        }


    }

    @Et("{string} est supprimé du module {string} crs")
    public void estSuppriméDuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
//        Cours cours = coursRepository.findByName(arg0).get();
        Cours cours = findCoursByNameInModule(module, arg0);


        assertFalse(module.getRessources().contains(cours));
    }

    @Quand("{string} veut ajouter le cours {string} et numéro {int} au module {string} crs")
    public void veutAjouterLeCoursEtNuméroAuModuleCrs(String arg0, String arg1, int arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        String jwtTeacher = authController.generateJwt(arg0, PASSWORD);

        executePostWithBody("http://localhost:8080/api/module/" + module.getId() + "/cours",
                new CoursRequest(arg1, "descript", arg2),
                jwtTeacher);
    }

    @Et("{string} n'est pas ajouté au module {string} crs")
    public void nEstPasAjoutéAuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
//        Cours cours = coursRepository.findByName(arg0).get();
        Cours cours = findCoursByNameInModule(module, arg0);
        System.out.println(module.getRessources());
        assertTrue(module.getRessources().contains(cours));
    }

    @Et("{string} n'est pas supprimé du module {string} crs")
    public void nEstPasSuppriméDuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
//        Cours cours = coursRepository.findByName(arg0).get();
        Cours cours = findCoursByNameInModule(module, arg0);


        assertFalse(module.getRessources().contains(cours));
    }
}

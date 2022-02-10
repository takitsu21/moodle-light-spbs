package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

public class CoursesStepdefs {
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
    @Etantdonné("Un enseignant avec le nom de connexion {string} crs")
    public void unEnseignantAvecLeNomDeConnexionCrs(String arg0) {
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

    @Et("un module {string} qui a un cours {string} et qui a un enseignant {string} crs")
    public void unModuleQuiAUnCoursEtQuiAUnEnseignantCrs(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        Cours cours = coursRepository.findByName(arg1).orElse(new Cours(arg1, "description", 1));
        User teacher = userRepository.findByUsername(arg2).get();
    }

    @Quand("{string} veut ajouter le cours {string} qui a pour description {string} au module {string} crs")
    public void veutAjouterLeCoursQuiAPourDescriptionAuModuleCrs(String arg0, String arg1, String arg2, String arg3) {
    }

    @Alors("le dernier status de réponse est {int} crs")
    public void leDernierStatusDeRéponseEstCrs(int arg0) {
    }

    @Et("{string} est ajouté au module {string} crs")
    public void estAjoutéAuModuleCrs(String arg0, String arg1) {
    }

    @Quand("{string} veut supprimer le cours {string} du module {string} crs")
    public void veutSupprimerLeCoursDuModuleCrs(String arg0, String arg1, String arg2) {
    }

    @Et("{string} est supprimé du module {string} crs")
    public void estSuppriméDuModuleCrs(String arg0, String arg1) {
    }

    @Quand("{string} veut ajouter le cours {string} au module {string} crs")
    public void veutAjouterLeCoursAuModuleCrs(String arg0, String arg1, String arg2) {
    }

    @Et("{string} n'est pas ajouté au module {string} crs")
    public void nEstPasAjoutéAuModuleCrs(String arg0, String arg1) {
    }

    @Et("{string} n'est pas supprimé du module {string} crs")
    public void nEstPasSuppriméDuModuleCrs(String arg0, String arg1) {
    }
}

package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Cours;
import fr.uca.api.models.Module;
import fr.uca.api.models.UserRef;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.util.VerifyAuthorizations;
import io.cucumber.java.bs.I;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.CoursRequest;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class CoursesStepdefs extends SpringIntegration {
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    AuthController authController;


    @Et("un module {string} qui a un cours {string} et numéro {int} et qui a un enseignant {string} crs")
    public void unModuleQuiAUnCoursEtNuméroEtQuiAUnEnseignantCrs(String arg0, String arg1, int arg2, String arg3) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        Cours cours = coursRepository.findByName(arg1).orElse(new Cours(arg1, "description", arg2));
        UserRef teacher = userRefRepository.findByUsername(arg3).get();

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
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        executePost(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/cours",
                new CoursRequest(arg1, arg2, arg3),
                jwt);
    }

    @Et("{string} est ajouté au module {string} crs")
    public void estAjoutéAuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Cours cours = (Cours) module.findRessourceByName(arg0);

        assertTrue(module.getRessources().contains(cours));
    }

    @Quand("{string} veut supprimer le cours {string} et numéro {int} du module {string} crs")
    public void veutSupprimerLeCoursEtNuméroDuModuleCrs(String arg0, String arg1, int arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        Cours cours = (Cours) module.findRessourceByName(arg1);


        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());
        if (cours == null) {
            executeDelete(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/cours/-1",
                    jwt);
        } else {
            executeDelete(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/cours/" + cours.getId(),
                    jwt);
        }


    }

    @Et("{string} est supprimé du module {string} crs")
    public void estSuppriméDuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Cours cours = (Cours) module.findRessourceByName(arg0);

        assertFalse(module.getRessources().contains(cours));
    }

    @Quand("{string} veut ajouter le cours {string} et numéro {int} au module {string} crs")
    public void veutAjouterLeCoursEtNuméroAuModuleCrs(String arg0, String arg1, int arg2, String arg3) throws IOException {
        Module module = moduleRepository.findByName(arg3).get();
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executePost(VerifyAuthorizations.apiHost + "api/modules/" + module.getId() + "/cours",
                new CoursRequest(arg1, "descript", arg2),
                jwt);
    }

    @Et("{string} n'est pas ajouté au module {string} crs")
    public void nEstPasAjoutéAuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Cours cours = (Cours) module.findRessourceByName(arg0);
        assertTrue(module.getRessources().contains(cours));
    }

    @Et("{string} n'est pas supprimé du module {string} crs")
    public void nEstPasSuppriméDuModuleCrs(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Cours cours = (Cours) module.findRessourceByName(arg0);

        assertFalse(module.getRessources().contains(cours));
    }
}

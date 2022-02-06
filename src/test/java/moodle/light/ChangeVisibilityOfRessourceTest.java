package moodle.light;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import moodle.Module;
import moodle.Ressource.Ressource;
import moodle.users.Teacher;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeVisibilityOfRessourceTest {
    private final Map<String, Teacher> teachers = new HashMap<>();
    private final Map<String, Module> modules = new HashMap<>();
    private boolean success;

    @Etantdonné("le professeur {string} assigné au module de {string} qui a une ressource {string}")
    public void leProfesseurAssignéAuModuleDeQuiAUneRessource(String arg0, String arg1, String arg2) {
        if (!teachers.containsKey(arg0)) {
            teachers.put(arg0, new Teacher(arg0, "123"));
        }
        if (!modules.containsKey(arg1)) {
            Map<String, Ressource> map=new HashMap<>();
            map.put(arg2, new Ressource(arg2, null));
            modules.put(arg1, new Module(arg1, map));
        }
        modules.get(arg1).getTeachers().add(teachers.get(arg0));
    }

    @Et("le professeur {string} qui n'a aucun module")
    public void leProfesseurQuiNAAucunModule(String arg0) {
        if (!teachers.containsKey(arg0)) {
            teachers.put(arg0, new Teacher(arg0, "123"));
        }
    }

    @Quand("le professeur {string} essaie de changer la visibilité de la ressource {string} du module {string}")
    public void leProfesseurEssaieDeChangerLaVisibilitéDeLaRessourceDuModule(String arg0, String arg1, String arg2) {
        success=modules.get(arg2).changeVisibilityOfRessource(teachers.get(arg0), arg1, true);
    }

    @Alors("le professeur reussi à changer la visibilité")
    public void leProfesseurReussiÀChangerLaVisibilité() {
        assertTrue(success);
    }
}

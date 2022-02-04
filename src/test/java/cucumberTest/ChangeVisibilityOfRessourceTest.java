package cucumberTest;

import io.cucumber.java.fr.Etantdonné;
import moodle.Module;
import moodle.Ressource.Ressource;
import moodle.users.Teacher;

import java.util.HashMap;
import java.util.Map;

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
}

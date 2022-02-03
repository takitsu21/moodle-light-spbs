package cucumberTest;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Etque;
import io.cucumber.java.fr.Quand;
import moodle.Module;
import moodle.users.Teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ProfesseurAssignToAModuleTest {
    private Teacher teacher;
    private final Map<String, Module> modules= new HashMap<>();
    private boolean success;

    @Etantdonné("un professeur {string} avec le mot de passe {string} voulant s assigner au module {string} qui n'a pas de professeur et au module {string} qui a un professeur assigné")
    public void unProfesseurAvecLeMotDePasseVoulantSAssignerAuModuleQuiNAPasDeProfesseurEtAuModuleQuiAUnProfesseurAssigné(String arg0, String arg1, String arg2, String arg3) {
        teacher=new Teacher(arg0, arg1);
        modules.put(arg2, new Module(arg2, null));
        modules.put(arg3, new Module(arg3, null));
        modules.get(arg3).setTeachers(List.of(new Teacher("Teacher", "123")));
    }


    @Quand("le professeur essaie de s'assigner au module {string}")
    public void leProfesseurEssaieDeSAssignerAuModule(String arg0) {
        assertEquals(arg0, modules.get(arg0).getName());
        success=teacher.assignAModule(modules.get(arg0));
    }

    @Alors("le professeu reussi à s'assigner")
    public void leProfesseuReussiÀSAssigner() {
        assertTrue(success);
    }

    @Alors("le professeur ne reussi pas à s'assigner")
    public void leProfesseurNeReussiPasÀSAssigner() {
        assertFalse(success);
    }

}

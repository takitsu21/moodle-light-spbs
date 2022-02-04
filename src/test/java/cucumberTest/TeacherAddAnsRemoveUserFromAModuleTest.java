package cucumberTest;

import io.cucumber.java.fr.*;
import moodle.Module;
import moodle.users.Student;
import moodle.users.Teacher;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeacherAddAnsRemoveUserFromAModuleTest {
    private final Map<String, Teacher> teachers = new HashMap<>();
    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, Module> modules = new HashMap<>();
    private boolean success;


    @Etantdonné("le professeur {string} assigné au module de {string}")
    public void leProfesseurAssignéAuModuleDe(String arg0, String arg1) {
        if (!teachers.containsKey(arg0)) {
            teachers.put(arg0, new Teacher(arg0, "123"));
        }
        if (!modules.containsKey(arg1)) {
            modules.put(arg1, new Module(arg1, null));
        }
        modules.get(arg1).getTeachers().add(teachers.get(arg0));
    }

    @Et("l'élève {string} assigné au cours de {string}")
    public void lÉlèveAssignéAuCoursDe(String arg0, String arg1) {
        students.put(arg0, new Student(arg0, "123"));
        if (!modules.containsKey(arg1)) {
            modules.put(arg1, new Module(arg1, null));
        }
        modules.get(arg1).getStudents().add(students.get(arg0));
    }

    @Et("le professeur {string} qui n'est assigné a aucun module")
    public void leProfesseurQuiNEstAssignéAAucunModule(String arg0) {
        if (!teachers.containsKey(arg0)) {
            teachers.put(arg0, new Teacher(arg0, "123"));
        }
    }

    @Quand("le professeur {string} essaie d'assigner le professeur {string} au module {string}")
    public void leProfesseurEssaieDAssignerLeProfesseurAuModule(String arg0, String arg1, String arg2) {
        success=modules.get(arg2).assignTeacher(teachers.get(arg0), teachers.get(arg1));
    }

    @Quand("le professeur {string} essaie de retirer le professeur {string} au module {string}")
    public void leProfesseurEssaieDeRetirerLeProfesseurAuModule(String arg0, String arg1, String arg2) {
        success=modules.get(arg2).deleteTeacher(teachers.get(arg0), teachers.get(arg1));
    }

    @Alors("le professeur reussi à l'assigner")
    public void leProfesseurReussiÀLAssigner() {
        assertTrue(success);
    }

    @Alors("le professeur ne reussi pas à l'assigner")
    public void leProfesseurNeReussiPasÀLAssigner() {
        assertFalse(success);
    }

}

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import moodle.Module;
import moodle.users.Student;
import moodle.users.Teacher;

import static org.junit.jupiter.api.Assertions.*;

public class AddRemoveStudentModuleStepdefs {
    private Module moduleMath;
    private Module moduleFr;
    private Teacher teacher;
    private Student student;

    @Etantdonné("Un enseignant {string} avec le mot de passe {string}")
    public void unEnseignantAvecLeMotDePasse(String arg0, String arg1) {
        teacher = new Teacher(arg0, arg1);

    }

    @Et("un étudiant {string}")
    public void unÉtudiant(String arg0) {
        student = new Student(arg0, null);
    }

    @Et("un module {string} et {string} de ressource vide")
    public void unModuleEtDeRessourceVide(String arg0, String arg1) {
        moduleMath = new Module(arg0);
        moduleFr = new Module(arg1);
    }
    @Et("l'étudiant est assigné aux cours de {string}")
    public void lÉtudiantEstAssignéAuxCoursDe(String arg0) {
        moduleMath.getStudents().add(student);
    }

    @Et("l'enseignant est assigné aux cours de {string} et de {string}")
    public void lEnseignantEstAssignéAuxCoursDeEtDe(String arg0, String arg1) {
        moduleFr.getTeacher().add(teacher);
        moduleMath.getTeacher().add(teacher);
    }

    @Quand("L'enseignant ajoute {string} au module de {string}")
    public void lEnseignantAjouteAuModuleDe(String arg0, String arg1) {
        assertEquals(arg0, student.getName());
        assertEquals(arg1, moduleFr.getName());
        assertTrue(moduleFr.assignUser(teacher, student));
    }

    @Alors("L'élève est ajouter au module")
    public void lÉlèveEstAjouterAuModule() {
        assertTrue(moduleFr.getStudents().contains(student));

    }

    @Quand("L'enseignant veut enlever {string} du module de {string}")
    public void lEnseignantVeutEnleverDuModuleDe(String arg0, String arg1) {
        assertEquals(arg0, student.getName());
        assertEquals(arg1, moduleMath.getName());
        assertTrue(moduleMath.removeUser(teacher, student));
    }

    @Alors("L'élève est enlever du module")
    public void lÉlèveEstEnleverDuModule() {
        assertFalse(moduleMath.getStudents().contains(student));
    }


}
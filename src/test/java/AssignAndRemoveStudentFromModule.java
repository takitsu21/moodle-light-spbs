import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import moodle.Module;
import moodle.users.Student;

import java.util.ArrayList;


public class AssignAndRemoveStudentFromModule {
    private Module module;
    private Student student;

    @Etantdonné("un module {string}")
    public void unModule(String arg0) {
        module = new Module(arg0, new ArrayList<>());
    }

    @Et("un étudiant {string}")
    public void unÉtudiant(String arg0) {
        student = new Student(arg0, "test");
    }

    @Quand("{string} n'est pas inscrit dans le module {string}")
    public void nEstPasInscritDansLeModule(String arg0, String arg1) {

    }

    @Alors("{string} est ajouté au module {string}")
    public void estAjoutéAuModule(String arg0, String arg1) {
    }

    @Quand("{string} est déjà inscrit dans le module {string}")
    public void estDéjàInscritDansLeModule(String arg0, String arg1) {
    }

    @Alors("{string} est enlever du module {string}")
    public void estEnleverDuModule(String arg0, String arg1) {
    }
}

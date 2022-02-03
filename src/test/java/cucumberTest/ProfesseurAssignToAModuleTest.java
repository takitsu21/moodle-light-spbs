package cucumberTest;

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Etque;
import io.cucumber.java.fr.Quand;
import moodle.Module;
import moodle.users.Teacher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ProfesseurAssignToAModuleTest {
    private Teacher teacher;
    private Module module1;
    private Module module2;


    @Etantdonné("un professeur {string} avec le mot de passe {string} voulant s{string}a pas de professeur et au module {string} qui a un professeur assigné")
    public void unProfesseurAvecLeMotDePasseVoulantSAssignerAuModuleQuiNAPasDeProfesseurEtAuModuleQuiAUnProfesseurAssigné(String arg0, String arg1, String arg2, String arg3) {
        teacher=new Teacher(arg0, arg1);
        module1=new Module(arg2, null);
        module2=new Module(arg3, null);
        module2.setTeachers(List.of(new Teacher("Teacher", "123")));
    }

}

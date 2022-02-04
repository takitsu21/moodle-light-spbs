import io.cucumber.java.en_scouse.An;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import moodle.Ressource.Exercices.QCM;
import moodle.Ressource.Exercices.Question;
import moodle.Ressource.Exercices.Questionnaire;
import moodle.users.Student;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class validQuestionnaireTest {
    private Student Antoine;
    private Questionnaire Test1;
    private QCM q1;

    @Etantdonné("un élève {string}")
    public void unÉlève(String arg0) {
        Antoine = new Student(arg0, "1234");
    }

    @Et("un questionnaire rempli {string}")
    public void unQuestionnaireRempli(String arg0) {
        Test1 = new Questionnaire(arg0,"Test numéro 1", new ArrayList<Question>(), new ArrayList<Student>());
        Test1.addStudent(Antoine);
        q1 = new QCM(1,"Question1", "Répond", new String[]{"R1","R2"}, 1);
        q1.setStudentAnswer(Antoine,1);
        Test1.addQuestion((Question) q1);
    }


    @Quand("l'élève {string} valide un questionnaire")
    public void lÉlèveValideUnQuestionnaire(String arg0) {
        Test1.validateQuestionnaire(Antoine);
    }

    @Alors("le questionnaire est validé")
    public void leQuestionnaireEstValidé() {
        assertTrue(Test1.isValidateBy(Antoine));
    }


}

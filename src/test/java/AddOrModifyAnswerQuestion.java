import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import moodle.Ressource.Exercices.OpenQuestion;
import moodle.Ressource.Exercices.QCM;
import moodle.Ressource.Exercices.Question;
import moodle.Ressource.Exercices.Questionnaire;
import moodle.users.Student;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AddOrModifyAnswerQuestion {
    private Student Antoine;
    private Questionnaire Test1;
    private QCM Q1;
    private OpenQuestion Q2;

    @Etantdonné("Un élève {string}")
    public void unÉlève(String arg0) {
        this.Antoine = new Student("Antoine", "1234");
    }

    @Et("un questionnaire {string}")
    public void unQuestionnaire(String arg0) {
        Test1 = new Questionnaire(arg0, "Test numéro 1", new ArrayList<Question>(), new ArrayList<Student>());
    }

    @Et("une question QCM {string} appartenant au questionnaire {string}")
    public void uneQuestionQCMAppartenantAuQuestionnaire(String arg0, String arg1) {
        Q1 = new QCM(1, arg0, "Choisissez la bonne réponse",
                new String[]{"Réponse A", "Réponse B", "Réponse C", "Réponse D"}, 3);
        Test1.addQuestion((Question) Q1);
    }

    @Et("une question open {string} appartenant au questionnaire {string}, avec la réponse {int} choisie")
    public void uneQuestionOpenAppartenantAuQuestionnaireAvecLaRéponseChoisie(String arg0, String arg1, int arg2) {
        Q2 = new OpenQuestion(1, arg0, "Choisissez la ou les bonnes réponses",
                new String[]{"Réponse A", "Réponse B", "Réponse C", "Réponse D"}, new int[]{1, 2});
        Q2.addStudentAnswer(Antoine, 2);
        Test1.addQuestion(Q2);

    }

    @Quand("{string} choisis d'ajouter la réponse {int} à la question {string} du questionnaire {string}")
    public void choisisDAjouterLaRéponseÀLaQuestionDuQuestionnaire(String arg0, int arg1, String arg2, String arg3) {
        Q2.addStudentAnswer(Antoine, 1);
    }

    @Alors("La réponse est ajouté aux réponses donnée par l'élève dans la question")
    public void laRéponseEstAjoutéAuxRéponsesDonnéeParLÉlèveDansLaQuestion() {
        assertTrue(Q2.getStudentAnswers(Antoine).contains((Integer) 1));
    }

    @Quand("{string} choisis d'enlever la réponse {int} à la question {string} du questionnaire {string}")
    public void choisisDEnleverLaRéponseÀLaQuestionDuQuestionnaire(String arg0, int arg1, String arg2, String arg3) {
        Q2.removeStudentAnswer(Antoine, 2);
    }

    @Alors("La réponse est enlevé des réponses donnée par l'élève à la question")
    public void laRéponseEstEnlevéDesRéponsesDonnéeParLÉlèveÀLaQuestion() {
        assertFalse(Q2.getStudentAnswers(Antoine).contains((Integer) 2));
    }

    @Quand("{string} choisi la réponse {int} à la question {string} du questinnaire {string}")
    public void choisiLaRéponseÀLaQuestionDuQuestinnaire(String arg0, int arg1, String arg2, String arg3) {
        Q1.setStudentAnswer(Antoine, 1);
    }

    @Alors("La réponse choisi est la réponse {int}")
    public void laRéponseChoisiEstLaRéponse(int arg0) {
        assertEquals(1, Q1.getStudentsAnswer(Antoine));
    }
}

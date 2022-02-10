import fr.uca.springbootstrap.SpringIntegration;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class QCMStepdefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    private Module module;

    @Etantdonné("le professeur {string} assigné au module de {string} avec un questionnaire {string} visible qcm")
    public void leProfesseurAssignéAuModuleDeAvecUnQuestionnaireQcm(String arg0, String arg1, String arg2) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);

        Questionnaire questionnaire = questionnaireRepository.findByName(arg1).
                orElse(new Questionnaire(arg1, "null", 1));
        questionnaire.setVisibility(true);
        questionnaireRepository.save(questionnaire);

        module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);

        module.getRessources().add(questionnaire);
        moduleRepository.save(module);

        assertTrue(module.getParticipants().contains(user));
        assertTrue(module.getRessources().contains(questionnaire));
    }

    @Et("le questionnaire {string} a un QCM {string} qcm")
    public void leQuestionnaireAUnQCMQcm(String arg0, String arg1) {
        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg0);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg1);
        if (qcm==null) {
            questionnaire.getQuestions().add(new QCM(1, arg1, null));
        }
        questionnaireRepository.save(questionnaire);
        moduleRepository.save(module);
    }

    @Et("le QCM {string} du questionnaire {string} a les reponses possible {string} et {string} qcm")
    public void leQCMDuQuestionnaireALesReponsesPossibleEtQcm(String arg0, String arg1, String arg2, String arg3) {
        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg1);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg0);
        qcm.setPossibleAnswers(new HashSet<>(){{add(new Answer(arg2)); add(new Answer(arg3));}});

        questionnaireRepository.save(questionnaire);
        moduleRepository.save(module);
    }

    @Et("l'élève {string} assigné au module de {string} qcm")
    public void lÉlèveAssignéAuModuleDeQcm(String arg0, String arg1) {
    }

    @Quand("le professeur {string} essaie d'ajouter la reponse possible {string} au QCM {string} du questionnaire {string} du module {string}")
    public void leProfesseurEssaieDAjouterLaReponsePossibleAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }

    @Et("le dernier status de request est {int} qcm")
    public void leDernierStatusDeRequestEstQcm(int arg0) {
    }

    @Alors("la reponse {string} est dans le QCM {string} du questionnaire {string} du module {string}")
    public void laReponseEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
    }

    @Quand("le professeur {string} essaie d'ajouter la bonne reponse {string} au QCM {string} du questionnaire {string} du module {string}")
    public void leProfesseurEssaieDAjouterLaBonneReponseAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }

    @Alors("la bonne reponse {string} est dans le QCM {string} du questionnaire {string} du module {string}")
    public void laBonneReponseEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
    }

    @Quand("L{string}ajouter ça reponse {string} au QCM {string} du questionnaire {string} du module {string}")
    public void lÉlèveEssaieDAjouterÇaReponseAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }

    @Alors("la reponse de l'étudiant {string} est {string} est dans le QCM {string} du questionnaire {string} du module {string}")
    public void laReponseDeLÉtudiantEstEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }
}

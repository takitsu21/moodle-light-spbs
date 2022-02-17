package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.Module;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.QCM;
import fr.uca.api.models.questions.Question;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.QCMRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.AnswersRequest;
import payload.request.MyAnswer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QCMStepdefs extends SpringIntegration {
    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    AuthController authController;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Et("le questionnaire {string} dans le module {string} est visible")
    public void leQuestionnaireDansLeModuleEstVisible(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg0);
        questionnaire.setVisibility(true);
        questionnaireRepository.save(questionnaire);
    }

    @Et("le questionnaire {string} du module {string} a un QCM {string} qcm")
    public void leQuestionnaireDuModuleAUnQCMQcm(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg0);
        QCM qcm = (QCM) questionnaire.findQuestionByName(arg2);
        if (qcm == null) {
            qcm = new QCM(1, arg2, "null");
            qcmRepository.save(qcm);
            questionnaire.getQuestions().add(qcm);
            questionnaireRepository.save(questionnaire);
            moduleRepository.save(module);
        }
    }

    @Et("le QCM {string} du questionnaire {string} du module {string} a les reponses possible {string} et {string} qcm")
    public void leQCMDuQuestionnaireDuModuleALesReponsesPossibleEtQcm(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg2).get();

        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg1);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg0);

        Answer answer=new Answer(arg3);
        answerRepository.save(answer);

        Answer answer1=new Answer(arg4);
        answerRepository.save(answer1);


        Set<Answer> answers=new HashSet<>(){{add(answer); add(answer1);}};

        qcm.setPossibleAnswers(answers);
        qcmRepository.save(qcm);
    }



    @Quand("le professeur {string} essaie d'ajouter la reponse possible {string} au QCM {string} du questionnaire {string} du module {string}")
    public void leProfesseurEssaieDAjouterLaReponsePossibleAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
        QCM qcm = (QCM) ressource.findQuestionByName(arg2);
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executePost("http://localhost:8080/api/qcm/" + module.getId() + "/questionnaire/" + ressource.getId()+"/qcm/"+qcm.getId()+"/possible_answer", new AnswersRequest(new HashSet<>(){{add(new MyAnswer(arg1));}}), jwt);
    }

    @Alors("la reponse {string} est dans le QCM {string} du questionnaire {string} du module {string}")
    public void laReponseEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg3).get();

        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg2);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg1);

        assertTrue(qcm.possibleAnswerContains(arg0));
    }

    @Quand("le professeur {string} essaie d'ajouter la bonne reponse {string} au QCM {string} du questionnaire {string} du module {string}")
    public void leProfesseurEssaieDAjouterLaBonneReponseAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
        QCM qcm = (QCM) ressource.findQuestionByName(arg2);
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executePost("http://localhost:8080/api/qcm/" + module.getId() + "/questionnaire/" + ressource.getId()+"/qcm/"+qcm.getId()+"/good_answer", new MyAnswer(arg1), jwt);
    }

    @Alors("la bonne reponse {string} est dans le QCM {string} du questionnaire {string} du module {string}")
    public void laBonneReponseEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg3).get();

        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg2);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg1);

        assertEquals(qcm.getAnswer().getAnswer(), arg0);
    }

    @Quand("L élève {string} essaie d'ajouter sa reponse {string} au QCM {string} du questionnaire {string} du module {string}")
    public void lÉlèveEssaieDAjouterSaReponseAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
        QCM qcm = (QCM) ressource.findQuestionByName(arg2);
        UserRef user = userRefRepository.findByUsername(arg0).get();

        String jwt = userToken.get(user.getUsername());

        executePost("http://localhost:8080/api/qcm/" + module.getId() + "/questionnaire/" + ressource.getId()+"/qcm/"+qcm.getId()+"/student_answer", new MyAnswer(arg1), jwt);

    }

    @Alors("la reponse de l'étudiant {string} est {string} est dans le QCM {string} du questionnaire {string} du module {string}")
    public void laReponseDeLÉtudiantEstEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg4).get();

        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg3);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg2);

        assertEquals(qcm.getStudentAnswerOf(arg0).getAnswer().getAnswer(), arg1);
    }

    @Alors("{string} n a pas de reponse de l'étudiant au QCM {string} du questionnaire {string} du module {string}")
    public void nAPasDeReponseDeLÉtudiantAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg3).get();

        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg2);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg1);

        assertNull(qcm.getStudentAnswerOf(arg0));
    }
}

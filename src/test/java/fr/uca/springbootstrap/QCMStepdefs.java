package fr.uca.springbootstrap;

import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.payload.request.AnswersRequest;
import fr.uca.springbootstrap.payload.request.MyAnswer;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.QCMRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Etantdonné("le professeur {string} assigné au module de {string} avec un questionnaire {string} visible qcm")
    public void leProfesseurAssignéAuModuleDeAvecUnQuestionnaireQcm(String arg0, String arg1, String arg2) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);


        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);
        if (questionnaire==null){
            questionnaire=new Questionnaire(arg2, "null", 1);
        }
        questionnaire.setVisibility(true);
        questionnaireRepository.save(questionnaire);

        module.getRessources().add(questionnaire);
        moduleRepository.save(module);

        assertTrue(module.getParticipants().contains(user));
        assertTrue(module.getRessources().contains(questionnaire));
    }

    @Et("le questionnaire {string} du module {string} a un QCM {string} qcm")
    public void leQuestionnaireDuModuleAUnQCMQcm(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg1).get();
        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg0);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg2);
        if (qcm==null) {
            qcm=new QCM(1, arg2, "null");
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
//        questionnaireRepository.save(questionnaire);
//        moduleRepository.save(module);
    }



    @Et("l'élève {string} assigné au module de {string} qcm")
    public void lÉlèveAssignéAuModuleDeQcm(String arg0, String arg1) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>() {{
            add(roleRepository.findByName(ERole.ROLE_STUDENT).
                    orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }});
        userRepository.save(user);

        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
        module.getParticipants().add(user);
        moduleRepository.save(module);

        assertTrue(module.getParticipants().contains(user));
    }

    @Quand("le professeur {string} essaie d'ajouter la reponse possible {string} au QCM {string} du questionnaire {string} du module {string}")
    public void leProfesseurEssaieDAjouterLaReponsePossibleAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
        QCM qcm = (QCM) ressource.findQuestionByName(arg2);
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/qcm/" + module.getId() + "/questionnaire/" + ressource.getId()+"/qcm/"+qcm.getId()+"/possible_answer", new AnswersRequest(new HashSet<>(){{add(new MyAnswer(arg1));}}), jwt);
    }

    @Et("le dernier status de request est {int} qcm")
    public void leDernierStatusDeRequestEstQcm(int arg0) throws IOException {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
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
        User prof = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
        QCM qcm = (QCM) ressource.findQuestionByName(arg2);
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/qcm/" + module.getId() + "/questionnaire/" + ressource.getId()+"/qcm/"+qcm.getId()+"/good_answer", new MyAnswer(arg1), jwt);
    }

    @Alors("la bonne reponse {string} est dans le QCM {string} du questionnaire {string} du module {string}")
    public void laBonneReponseEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg3).get();

        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg2);
        QCM qcm= (QCM) questionnaire.findQuestionByName(arg1);

        assertEquals(qcm.getAnswer().getAnswer(), arg0);
    }

    @Quand("L élève {string} essaie d'ajouter ça reponse {string} au QCM {string} du questionnaire {string} du module {string}")
    public void lÉlèveEssaieDAjouterÇaReponseAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        User prof = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
        QCM qcm = (QCM) ressource.findQuestionByName(arg2);
        String jwt = authController.generateJwt(arg0, PASSWORD);

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

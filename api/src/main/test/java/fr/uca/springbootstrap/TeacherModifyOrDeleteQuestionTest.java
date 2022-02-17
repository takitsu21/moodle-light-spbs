package fr.uca.springbootstrap;

import fr.uca.api.controllers.AuthController;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.Module;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.QCM;
import fr.uca.api.models.questions.Question;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.QCMRepository;
import fr.uca.api.repository.question.QuestionRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import payload.request.QuestionRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherModifyOrDeleteQuestionTest extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    AuthController authController;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    UserRefRepository userRefRepository;


    @Et("une question QCM de nom {string} et de description {string} et de numéro {int} appartenant au questionnaire {string} du module {string} tmdqd")
    public void uneQuestionQCMDeNomEtDeDescriptionEtDeNuméroAppartenantAuQuestionnaireDuModuleTmdqd(String arg0, String arg1, int arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg4).
                orElse(new Module(arg1));

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);

        if (questionnaire==null){
            questionnaire=new Questionnaire(arg3, "Description "+arg3,1);
        }


        QCM question = (QCM) questionnaire.findQuestionByName(arg0);
        if (question==null){
            question=new QCM(arg2, arg0, arg1);
        }
        questionRepository.save(question);



        questionnaire.getQuestions().add(question);
        questionnaireRepository.save(questionnaire);
    }

    @Quand("Le professeur {string} veut modifier le nom de la question de numéro {int} du questionnaire {string} du module {string} par {string} tmdqk")
    public void leProfesseurVeutModifierLeNomDeLaQuestionDeNuméroDuQuestionnaireDuModuleParTmdqk(String arg0, int arg1, String arg2, String arg3, String arg4) throws IOException {
        Question question = questionRepository.findByNumber(arg1).get();
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);

        String jwTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/"+module.getId()
                        +"/questionnaire/"+questionnaire.getId()
                        +"/questions/"+question.getId()+"/name",
                new QuestionRequest(arg4, question.getDescription(),
                        question.getNumber()), jwTeacher);
    }


    @Alors("le status de la dernière requète est {int} tmdqql")
    public void leStatusDeLaDernièreRequèteEstTmdqql(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question de numéro {int} a pour nom {string} tmdqm")
    public void laQuestionDeNuméroAPourNomTmdqm(int arg0, String arg1) {
        Question question = questionRepository.findByNumber(arg0).get();
        assertEquals(arg1, question.getName());
    }

    @Quand("Le professeur {string} veut modifier le nom de la question de numéro {int} du questionnaire {string} du module {string} par {string} tmdqn")
    public void leProfesseurVeutModifierLeNomDeLaQuestionDeNuméroDuQuestionnaireDuModuleParTmdqn(String arg0, int arg1, String arg2, String arg3, String arg4) throws IOException {
        Question question = questionRepository.findByNumber(arg1).get();
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);

        String jwTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/"+module.getId()
                        +"/questionnaire/"+questionnaire.getId()
                        +"/questions/"+question.getId()+"/name",
                new QuestionRequest(arg4, question.getDescription(),
                        question.getNumber()), jwTeacher);
    }

    @Alors("le dernier status de réponse est {int} tmdqqo")
    public void leDernierStatusDeRéponseEstTmdqqo(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question de numéro {int} s'appelle toujours {string} tmdqp")
    public void laQuestionDeNuméroSAppelleToujoursTmdqp(int arg0, String arg1) {
        Question question = questionRepository.findByNumber(arg0).get();
        assertEquals(arg1, question.getName());
    }

    @Quand("Le professeur {string} veut modifier la description de la question {string} du questionnaire {string} du module {string} par {string} tmdqq")
    public void leProfesseurVeutModifierLaDescriptionDeLaQuestionDuQuestionnaireDuModuleParTmdqq(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Question question = questionRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);

        String jwTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/"+module.getId()
                +"/questionnaire/"+questionnaire.getId()
                +"/questions/"+question.getId()+"/description", new QuestionRequest(question.getName() , arg4, question.getNumber()), jwTeacher);

    }

    @Alors("le status de la dernière requète est {int} tmdqr")
    public void leStatusDeLaDernièreRequèteEstTmdqr(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question {string} possède la description {string} tmdqs")
    public void laQuestionPossèdeLaDescriptionTmdqs(String arg0, String arg1) {
        Question question = questionRepository.findByName(arg0).get();
        assertEquals(arg1, question.getDescription());
    }

    @Quand("Le professeur {string} veut modifier la description de la question {string} du questionnaire {string} du module {string} par {string} tmdqt")
    public void leProfesseurVeutModifierLaDescriptionDeLaQuestionDuQuestionnaireDuModuleParTmdqt(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Question question = questionRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);

        String jwTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/"+module.getId()
                +"/questionnaire/"+questionnaire.getId()
                +"/questions/"+question.getId()+"/description", new QuestionRequest(question.getName() , arg4, question.getNumber()), jwTeacher);
    }

    @Alors("le dernier status de réponse est {int} tmdqu")
    public void leDernierStatusDeRéponseEstTmdqu(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question {string} possède toujours la description {string} tmdqv")
    public void laQuestionPossèdeToujoursLaDescriptionTmdqv(String arg0, String arg1) {
        Question question = questionRepository.findByName(arg0).get();
        assertEquals(arg1, question.getDescription());
    }

    @Quand("le professeur {string} veut modifier le numéro de la question de nom {string} du questionnaire {string} du module {string} par {int} tmdqw")
    public void leProfesseurVeutModifierLeNuméroDeLaQuestionDeNomDuQuestionnaireDuModuleParTmdqw(String arg0, String arg1, String arg2, String arg3, int arg4) throws IOException {
        Question question = questionRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);

        String jwTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/"+module.getId()
                +"/questionnaire/"+questionnaire.getId()
                +"/questions/"+question.getId()+"/number", new QuestionRequest(question.getName() , question.getDescription(), arg4), jwTeacher);
    }

    @Alors("le dernier status de réponse est {int} tmdqx")
    public void leDernierStatusDeRéponseEstTmdqx(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("le question {string} possède le numéro {int} tmdqy")
    public void leQuestionPossèdeLeNuméroTmdqy(String arg0, int arg1) {
        Question question = questionRepository.findByName(arg0).get();
        assertEquals(arg1, question.getNumber());
    }

    @Quand("le professeur {string} veut modifier le numéro de la question {string} du questionnaire {string} du module {string} par {int} tmdqz")
    public void leProfesseurVeutModifierLeNuméroDeLaQuestionDuQuestionnaireDuModuleParTmdqz(String arg0, String arg1, String arg2, String arg3, int arg4) throws IOException {
        Question question = questionRepository.findByName(arg1).get();
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);

        String jwTeacher = authController.generateJwt(arg0, PASSWORD);
        executePost("http://localhost:8080/api/modules/"+module.getId()
                +"/questionnaire/"+questionnaire.getId()
                +"/questions/"+question.getId()+"/number", new QuestionRequest(question.getName() , question.getDescription(), arg4), jwTeacher);
    }

    @Alors("le dernier status de réponse et {int} tmdqaa")
    public void leDernierStatusDeRéponseEtTmdqaa(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question {string} possède le numéro {int} tmdqab")
    public void laQuestionPossèdeLeNuméroTmdqab(String arg0, int arg1) {
        Question question = questionRepository.findByName(arg0).get();
        assertEquals(arg1, question.getNumber());
    }


}

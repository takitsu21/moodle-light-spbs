import fr.uca.springbootstrap.SpringIntegration;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.Ressource;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.AnswerOpenQuestion;
import fr.uca.springbootstrap.models.questions.OpenQuestion;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.AnswerRequest;
import fr.uca.springbootstrap.payload.request.MyAnswer;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.AnswerOpenQuestionRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import io.cucumber.java.en_scouse.An;
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


public class OpenQuestionModificationStepDef extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerOpenQuestionRepository answerOpenQuestionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthController authController;

    @Etantdonné("Un module {string}")
    public void unModule(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        moduleRepository.save(module);
    }

    @Autowired
    PasswordEncoder encoder;

    @Et("un questionnaire {string} contenu dans le module {string}")
    public void unQuestionnaireContenuDansLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        Ressource questionnaire = module.findRessourceByName(arg0);
        if (questionnaire == null){
            questionnaire =(Ressource) new Questionnaire(arg0, "Description "+arg0, 1);
        }
        ressourceRepository.save(questionnaire);
        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }

    @Et("une question ouverte {string} contenue dans le le questionnaire {string} du module {string}")
    public void uneQuestionOuverteContenueDansLeLeQuestionnaire(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        Question question = questionnaire.findQuestionByName(arg0);
        if (question == null){
            question = (Question) new OpenQuestion(new HashSet<>(), new HashSet<>(), new HashSet<>(),
                                                    arg0, "Description "+arg0, 1);
        }
        questionRepository.save(question);
        questionnaire.addQuestion(question);
        questionnaireRepository.save(questionnaire);
    }

    @Et("la {string} du questionnaire {string} du module {string} a comme réponses possibles {string}, {string}, {string}")
    public void laDuQuestionnaireDuModuleACommeRéponsesPossibles(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);

        Answer answer1 = new Answer("A");
        Answer answer2 = new Answer("B");
        Answer answer3 = new Answer("C");
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        answerRepository.save(answer3);

        question.addPossibleAnswer(answer1);
        question.addPossibleAnswer(answer2);
        question.addPossibleAnswer(answer3);
        questionRepository.save(question);
    }

    @Et("la {string} du questionnaire {string} du module {string} a comme réponses {string} et {string}")
    public void laDuQuestionnaireDuModuleACommeRéponsesEt(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);

        Answer answer1 = question.getPossibleAnswerByContent(arg3);
        Answer answer2 = question.getPossibleAnswerByContent(arg4);

        question.addAnswer(answer1);
        question.addAnswer(answer2);

        questionRepository.save(question);
    }

    @Et("la {string} du questionnaire {string} du module {string} a commme réponses possibles {string} et comme réponse {string}")
    public void laDuQuestionnaireDuModuleACommmeRéponsesPossiblesEtCommeRéponse(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);

        if (question == null){
            question = new OpenQuestion(new HashSet<>(), new HashSet<>(), new HashSet<>(),
                                        arg0, "Descritpion "+arg0, 2);
        }

        Answer answer = new Answer("Z");
        answerRepository.save(answer);

        question.addPossibleAnswer(answer);
        question.addAnswer(answer);
        questionRepository.save(question);
    }

    @Et("un professeur {string} ayant le module {string}")
    public void unProfesseurAyantLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User teacher = module.findUserByName(arg0);
        if (teacher == null){
            teacher = new User(arg0,arg0+"@test.fr",encoder.encode(PASSWORD));
        }
        userRepository.save(teacher);

        module.addParticipant(teacher);
        moduleRepository.save(module);
    }

    @Et("un professeur {string} n'ayant pas de module")
    public void unProfesseurNAyantPasDeModule(String arg0) {
        User teacher = new User(arg0, arg0+"@test.fr",encoder.encode(PASSWORD));
        userRepository.save(teacher);
    }

    @Et("un élève {string} ayant le module {string}")
    public void unÉlèveAyantLeModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User student = module.findUserByName(arg0);
        if (student == null){
            student = new User(arg0,arg0+"@test.fr",encoder.encode(PASSWORD));
        }
        userRepository.save(student);

        module.addParticipant(student);
        moduleRepository.save(module);
    }

    @Et("{string} a répondu {string}, {string} a la question {string} du questionnaire {string} du module {string}")
    public void ARéponduALaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
        Module module = moduleRepository.findByName(arg5).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg4);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg3);
        User student = module.findUserByName(arg0);

        Answer answer1 = question.getPossibleAnswerByContent(arg1);
        Answer answer2 = question.getPossibleAnswerByContent(arg2);
        Set<Answer> answerSet = new HashSet<>();
        answerSet.add(answer1);
        answerSet.add(answer2);
        AnswerOpenQuestion studentAnswer = new AnswerOpenQuestion(answerSet, student);

        answerOpenQuestionRepository.save(studentAnswer);

        question.addStudentAnswer(studentAnswer);
        questionRepository.save(question);
    }

    @Et("{string} a répondu {string} a la question {string} du questionnaire {string} du module {string}")
    public void ARéponduALaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg2);
        User student = module.findUserByName(arg0);

        Answer answer = question.getPossibleAnswerByContent(arg1);
        Set<Answer> answerSet = new HashSet<>();
        answerSet.add(answer);
        AnswerOpenQuestion studentAnswer = new AnswerOpenQuestion(answerSet, student);

        answerOpenQuestionRepository.save(studentAnswer);

        question.addStudentAnswer(studentAnswer);
        questionRepository.save(question);
    }

    @Et("un élève {string} n'ayant pas le module {string}")
    public void unÉlèveNAyantPasLeModule(String arg0, String arg1) {
        User student = new User(arg0,arg0+"@test.fr",encoder.encode(PASSWORD));
        userRepository.save(student);
    }

    @Quand("le professeur {string} veut supprimer la réponse possible {string} de la question {string} du questionnaire {string} du module {string}")
    public void leProfesseurVeutSupprimerLaRéponsePossibleDeLaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        User teacher = module.findUserByName(arg0);
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg2);
        Answer answer = question.getPossibleAnswerByContent(arg1);

        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/module/"+module.getId()
                +"/questionnaire/"+questionnaire.getId()
                +"/open_question/"+question.getId()
                +"/possible_answer/"+answer.getId()
                ,jwt);
    }

    @Alors("le status de la dernière requète est {int}")
    public void leStatusDeLaDernièreRequèteEst(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("la question {string} du questionnaire {string} du module {string} a pas comme réponse et réponse possible {string}")
    public void laQuestionDuQuestionnaireDuModuleAPasCommeRéponseEtRéponsePossible(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        Answer possibleAnswer = question.getPossibleAnswerByContent(arg3);
        Answer answer = question.getAnswerByContent(arg3);

        assertNull(possibleAnswer);
        assertNull(answer);
    }


    @Et("la question {string} du questionnaire {string} du module {string} a comme réponses possible {string} et comme réponses {string}")
    public void laQuestionDuQuestionnaireDuModuleACommeRéponsesPossibleEtCommeRéponses(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        Answer possibleAnswer = question.getPossibleAnswerByContent(arg3);
        Answer answer = question.getAnswerByContent(arg3);

        assertNotNull(possibleAnswer);
        assertNotNull(answer);
    }


    @Quand("le professeur {string} veut supprimer la réponse {string} de la question {string} du questionnaire {string} du module {string}")
    public void leProfesseurVeutSupprimerLaRéponseDeLaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        User teacher = module.findUserByName(arg0);
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg2);
        Answer answer = question.getAnswerByContent(arg1);

        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/module/"+module.getId()
                        +"/questionnaire/"+questionnaire.getId()
                        +"/open_question/"+question.getId()
                        +"/answers/"+answer.getId()
                ,jwt);
    }

    @Et("la question {string} du questionnaire {string} du module {string} a comme réponse possible {string} et na pas comme réponses {string}")
    public void laQuestionDuQuestionnaireDuModuleACommeRéponsePossibleEtNaPasCommeRéponses(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        Answer possibleAnswer = question.getPossibleAnswerByContent(arg3);
        Answer answer = question.getAnswerByContent(arg4);

        assertNotNull(possibleAnswer);
        assertNull(answer);

    }

    @Quand("le professeur {string} ajoute la réponse possible {string} a la question {string} du questionnaire {string} du module {string}")
    public void leProfesseurAjouteLaRéponsePossibleALaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        User teacher = module.findUserByName(arg0);
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg2);
        MyAnswer myAnswer = new MyAnswer(arg1);
        Set<MyAnswer> answerSet = new HashSet<>();
        answerSet.add(myAnswer);
        AnswerRequest answerRequest = new AnswerRequest(answerSet);
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePut("http://localhost:8080/api/module/"+module.getId()
                        +"/questionnaire/"+questionnaire.getId()
                        +"/open_question/"+question.getId()
                        +"/possible_answer"
                ,answerRequest
                ,jwt);
    }

    @Quand("le professeur {string} ajoute la réponse {string} a la question {string} du questionnaire {string} du module {string}")
    public void leProfesseurAjouteLaRéponseALaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        User teacher = module.findUserByName(arg0);
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg2);
        Answer answer = question.getAnswerByContent(arg1);

        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePut("http://localhost:8080/api/module/"+module.getId()
                        +"/questionnaire/"+questionnaire.getId()
                        +"/open_question/"+question.getId()
                        +"/answer/"+answer.getId()
                ,jwt);
    }

    @Et("les réponses de la question {string} du questionnaire {string} du module {string} contiennent la réponse {string}")
    public void lesRéponsesDeLaQuestionDuQuestionnaireDuModuleContiennentLaRéponse(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        Answer answer = question.getAnswerByContent(arg3);

        assertNotNull(answer);

    }

    @Et("les réponses de la question {string} du questionnaire {string} du module {string} ne contiennent pas la réponse {string}")
    public void lesRéponsesDeLaQuestionDuQuestionnaireDuModuleNeContiennentPasLaRéponse(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        Answer answer = question.getAnswerByContent(arg3);

        assertNull(answer);
    }

    @Quand("l'élève {string} veut ajouter sa réponse {string} a la question {string} du questionnaire {string} du module {string}")
    public void lÉlèveVeutAjouterSaRéponseALaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg2);

        MyAnswer myAnswer = new MyAnswer(arg1);
        Set<MyAnswer> answerSet = new HashSet<>();
        answerSet.add(myAnswer);
        AnswerRequest answerRequest = new AnswerRequest(answerSet);
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePut("http://localhost:8080/api/module/"+module.getId()
                        +"/questionnaire/"+questionnaire.getId()
                        +"/open_question/"+question.getId()
                        +"/student_answer"
                ,answerRequest
                ,jwt);

    }

    @Et("la réponses de {string} a la question {string} du questionnaire {string} du module {string} contient la réponse {string}")
    public void laRéponsesDeALaQuestionDuQuestionnaireDuModuleContientLaRéponse(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg1);
        User student = module.findUserByName(arg0);
        Answer answer = question.getAnswerByContent(arg4);

        AnswerOpenQuestion studentAnswer = question.getStudentAnswerByStudent(student);
        assertTrue(studentAnswer.getAnswers().contains(answer));
    }

    @Quand("l'élève {string} veut supprimer sa réponse {string} a la question {string} du questionnaire {string} du module {string}")
    public void lÉlèveVeutSupprimerSaRéponseALaQuestionDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg2);
        Answer answer = question.getAnswerByContent(arg1);

        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeDelete("http://localhost:8080/api/module/"+module.getId()
                        +"/questionnaire/"+questionnaire.getId()
                        +"/open_question/"+question.getId()
                        +"/student_answer/"+answer.getId()
                ,jwt);
    }

    @Et("la réponses de {string} a la question {string} du questionnaire {string} du module {string} ne contient pas {string}")
    public void laRéponsesDeALaQuestionDuQuestionnaireDuModuleNeContientPas(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg3).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);
        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg1);
        User student = module.findUserByName(arg0);
        Answer answer = question.getAnswerByContent(arg4);

        AnswerOpenQuestion studentAnswer = question.getStudentAnswerByStudent(student);
        assertFalse(studentAnswer.getAnswers().contains(answer));
    }




}

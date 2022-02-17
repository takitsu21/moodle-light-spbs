//package fr.uca.springbootstrap;
//
//import auth.repository.UserRepository;
//import fr.uca.api.models.ERole;
//import fr.uca.api.models.Module;
//import fr.uca.api.models.Questionnaire;
//import fr.uca.api.models.UserRef;
//import fr.uca.api.models.questions.Answer;
//import fr.uca.api.models.questions.OpenQuestion;
//import fr.uca.api.repository.ModuleRepository;
//import fr.uca.api.repository.QuestionnaireRepository;
//import fr.uca.api.repository.UserRefRepository;
//import fr.uca.api.repository.question.AnswerRepository;
//import fr.uca.api.repository.question.OpenQuestionRepository;
//import io.cucumber.java.fr.Alors;
//import io.cucumber.java.fr.Et;
//import io.cucumber.java.fr.Etantdonné;
//import io.cucumber.java.fr.Quand;
//import io.cucumber.messages.internal.com.google.gson.Gson;
//import io.cucumber.messages.internal.com.google.gson.GsonBuilder;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import payload.request.LoginRequest;
//import payload.request.SignupRequest;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class OpenQuestionMyStepdefs extends SpringIntegration {
//    private static final String PASSWORD = "password";
//
//    @Autowired
//    ModuleRepository moduleRepository;
//
//    @Autowired
//    UserRefRepository userRefRepository;
//
//    @Autowired
//    QuestionnaireRepository questionnaireRepository;
//
//    @Autowired
//    OpenQuestionRepository openQuestionRepository;
//
//    @Autowired
//    AnswerRepository answerRepository;
//
//    @Etantdonné("le professeur {string} assigné au module de {string} avec un questionnaire {string} visible oq")
//    public void leProfesseurAssignéAuModuleDeAvecUnQuestionnaireQcm(String arg0, String arg1, String arg2) throws IOException{
//        executePost("http://localhost:8080/api/auth/signup",
//                new SignupRequest(arg0, arg0 + "@test.fr", PASSWORD, new HashSet<>() {{
//                    add(String.valueOf(ERole.ROLE_TEACHER));
//                }}));
//
//        UserRef user = userRefRepository.findByUsername(arg0).get();
//
//        executePost("http://localhost:8080/api/auth/signin",
//                new LoginRequest(arg0, PASSWORD));
//
//        String jsonString = EntityUtils.toString(latestHttpResponse.getEntity());
//
//        GsonBuilder builder = new GsonBuilder();
//        builder.setPrettyPrinting();
//
//        Gson gson = builder.create();
//        Map<String, Object> map = gson.fromJson(jsonString, Map.class);
//
//        userToken.put(user.getUsername(), (String) map.get("accessToken"));
//
//        Module module = moduleRepository.findByName(arg1).orElse(new Module(arg1));
//        module.getParticipants().add(user);
//
//        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg2);
//        if (questionnaire==null){
//            questionnaire=new Questionnaire(arg2, "null", 1);
//        }
//        questionnaire.setVisibility(true);
//        questionnaireRepository.save(questionnaire);
//
//        module.getRessources().add(questionnaire);
//        moduleRepository.save(module);
//
//        assertTrue(module.getParticipants().contains(user));
//        assertTrue(module.getRessources().contains(questionnaire));
//    }
//
//    @Et("le questionnaire {string} du module {string} a une question ouverte {string} oq")
//    public void leQuestionnaireDuModuleAUnQCMQcm(String arg0, String arg1, String arg2) {
//        Module module = moduleRepository.findByName(arg1).get();
//        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg0);
//        OpenQuestion openQuestion = (OpenQuestion) questionnaire.findQuestionByName(arg2);
//        if (openQuestion == null) {
//            openQuestion = new OpenQuestion(1, arg2, "null");
//            openQuestionRepository.save(openQuestion);
//            questionnaire.getQuestions().add(openQuestion);
//            questionnaireRepository.save(questionnaire);
//            moduleRepository.save(module);
//        }
//    }
//
//    @Et("la question ouverte {string} du questionnaire {string} du module {string} a les reponses possible {string} et {string} et {string} oq")
//    public void leQCMDuQuestionnaireDuModuleALesReponsesPossibleEtQcm(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
//        Module module = moduleRepository.findByName(arg2).get();
//
//        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg1);
//        OpenQuestion openQuestion= (OpenQuestion) questionnaire.findQuestionByName(arg0);
//
//        Answer answer=new Answer(arg3);
//        answerRepository.save(answer);
//
//        Answer answer1=new Answer(arg4);
//        answerRepository.save(answer1);
//
//        Answer answer2=new Answer(arg5);
//        answerRepository.save(answer2);
//
//
//        Set<Answer> answers=new HashSet<>(){{add(answer); add(answer1); add(answer2);}};
//
//        openQuestion.setPossibleAnswers(new HashSet<>());
//        openQuestionRepository.save(openQuestion);
//
//        for (Answer a: answers){
//            openQuestion.getPossibleAnswers().add(a);
//            openQuestionRepository.save(openQuestion);
//        }
//
//
//
////        questionnaireRepository.save(questionnaire);
////        moduleRepository.save(module);
//    }
//
//
//    @Quand("le professeur {string} essaie d'ajouter la reponse possible {string} a la question ouverte {string} du questionnaire {string} du module {string}")
//    public void leProfesseurEssaieDAjouterLaReponsePossibleAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
//        UserRef prof = userRefRepository.findByUsername(arg0).get();
//        Module module = moduleRepository.findByName(arg4).get();
//        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
//        OpenQuestion openQuestion = (OpenQuestion) ressource.findQuestionByName(arg2);
//        UserRef user = userRefRepository.findByUsername(arg0).get();
//
//        String jwt = userToken.get(user.getUsername());
//
//        executePost("http://localhost:8080/api/open_question/" + module.getId() + "/questionnaire/" + ressource.getId()+"/open_question/"+openQuestion.getId()+"/possible_answer", new AnswersRequest(new HashSet<>(){{add(new MyAnswer(arg1));}}), jwt);
//    }
//
//    @Et("le dernier status de request est {int} oq")
//    public void leDernierStatusDeRequestEstQcm(int arg0) throws IOException {
//        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
//    }
//
//    @Alors("la reponse {string} est dans la question ouverte {string} du questionnaire {string} du module {string} oq")
//    public void laReponseEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
//        Module module = moduleRepository.findByName(arg3).get();
//
//        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg2);
//        OpenQuestion openQuestion= (OpenQuestion) questionnaire.findQuestionByName(arg1);
//
//        assertTrue(openQuestion.possibleAnswerContains(arg0));
//    }
//
//    @Quand("le professeur {string} essaie d'ajouter les bonnes reponses {string} et {string} a la question ouverte {string} du questionnaire {string} du module {string}")
//    public void leProfesseurEssaieDAjouterLaBonneReponseAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws IOException {
//        UserRef prof = userRefRepository.findByUsername(arg0).get();
//        Module module = moduleRepository.findByName(arg5).get();
//        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg4);
//        OpenQuestion openQuestion = (OpenQuestion) ressource.findQuestionByName(arg3);
//        UserRef user = userRefRepository.findByUsername(arg0).get();
//
//        String jwt = userToken.get(user.getUsername());
//
//        executePost("http://localhost:8080/api/open_question/" + module.getId() + "/questionnaire/" + ressource.getId()+"/open_question/"+openQuestion.getId()+"/good_answer", new AnswersRequest(new HashSet<>(){{add(new MyAnswer(arg1)); add(new MyAnswer(arg2));}}), jwt);
//    }
//
//    @Alors("les bonnes reponses {string} et {string} sont dans la question ouverte {string} du questionnaire {string} du module {string} oq")
//    public void laBonneReponseEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) {
//        Module module = moduleRepository.findByName(arg4).get();
//
//        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg3);
//        OpenQuestion openQuestion= (OpenQuestion) questionnaire.findQuestionByName(arg2);
//
//        assertTrue(openQuestion.answerContains(arg0));
//        assertTrue(openQuestion.answerContains(arg1));
//    }
//
//    @Quand("L élève {string} essaie d'ajouter ça reponse {string} a la question ouverte {string} du questionnaire {string} du module {string}")
//    public void lÉlèveEssaieDAjouterÇaReponseALaQuestionOuverteDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
//        UserRef user = userRefRepository.findByUsername(arg0).get();
//        Module module = moduleRepository.findByName(arg4).get();
//        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg3);
//        OpenQuestion openQuestion = (OpenQuestion) ressource.findQuestionByName(arg2);
//
//        String jwt = userToken.get(user.getUsername());
//
//        executePost("http://localhost:8080/api/open_question/" + module.getId() + "/questionnaire/" + ressource.getId()+"/open_question/"+openQuestion.getId()+"/student_answer", new AnswersRequest(new HashSet<>(){{add(new MyAnswer(arg1));}}), jwt);
//
//    }
//
//    @Quand("L élève {string} essaie d'ajouter ces reponses {string} et {string} a la question ouverte {string} du questionnaire {string} du module {string}")
//    public void lÉlèveEssaieDAjouterÇaReponseAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) throws IOException {
//        UserRef user = userRefRepository.findByUsername(arg0).get();
//        Module module = moduleRepository.findByName(arg5).get();
//        Questionnaire ressource = (Questionnaire) module.findRessourceByName(arg4);
//        OpenQuestion openQuestion = (OpenQuestion) ressource.findQuestionByName(arg3);
//
//        String jwt = userToken.get(user.getUsername());
//
//        executePost("http://localhost:8080/api/open_question/" + module.getId() + "/questionnaire/" + ressource.getId()+"/open_question/"+openQuestion.getId()+"/student_answer", new AnswersRequest(new HashSet<>(){{add(new MyAnswer(arg1)); add(new MyAnswer(arg2));}}), jwt);
//
//    }
//
//    @Alors("les reponses de l'étudiant {string} sont {string} et {string} dans la question ouverte {string} du questionnaire {string} du module {string} oq")
//    public void laReponseDeLÉtudiantEstEstDansLeQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
//        Module module = moduleRepository.findByName(arg5).get();
//
//        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg4);
//        OpenQuestion openQuestion= (OpenQuestion) questionnaire.findQuestionByName(arg3);
//
//        UserRef user = userRefRepository.findByUsername(arg0).get();
//
//
//        assertTrue(openQuestion.studentAnswerContainsAnswer(user, arg1));
//        assertTrue(openQuestion.studentAnswerContainsAnswer(user, arg2));
//    }
//
//    @Alors("{string} n a pas de reponse de l'étudiant a la question ouverte {string} du questionnaire {string} du module {string} oq")
//    public void nAPasDeReponseDeLÉtudiantAuQCMDuQuestionnaireDuModule(String arg0, String arg1, String arg2, String arg3) {
//        Module module = moduleRepository.findByName(arg3).get();
//
//        Questionnaire questionnaire= (Questionnaire) module.findRessourceByName(arg2);
//        OpenQuestion openQuestion= (OpenQuestion) questionnaire.findQuestionByName(arg1);
//
//        assertNull(openQuestion.getStudentAnswerOf(arg0));
//    }
//
//
//}

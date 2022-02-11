package fr.uca.springbootstrap;

import fr.uca.springbootstrap.SpringIntegration;
import fr.uca.springbootstrap.controllers.AuthController;
import fr.uca.springbootstrap.models.ERole;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.OpenQuestion;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.AnswerRequest;
import fr.uca.springbootstrap.payload.request.MyAnswer;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.OpenQuestionRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenQuestionModificationTest extends SpringIntegration {
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
    AnswerRepository answerRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;



    @Etantdonné("Un module {string} oqm")
    public void unModuleOqm(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse( new Module(arg0));
        moduleRepository.save(module);
    }

    @Et("un questionnaire {string} contenu dans le module {string} oqm")
    public void unQuestionnaireContenuDansLeModuleOqm(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg0).orElse( new Module(arg0));

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg0);
        if (questionnaire==null) {
            questionnaire = new Questionnaire(arg1, "Description " + arg0, 1);
        }

        questionnaireRepository.save(questionnaire);

        module.addRessource(questionnaire);
        moduleRepository.save(module);
    }
    @Et("une question ouverte {string} contenue dans le questionnaire {string} du module {string} oqm")
    public void uneQuestionOuverteContenueDansLeQuestionnaireDuModuleOqm(String arg0, String arg1, String arg2) {
        Module module = moduleRepository.findByName(arg0).orElse( new Module(arg0));

        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        if (questionnaire==null) {
            questionnaire = new Questionnaire(arg1, "Description " + arg1, 1);
        }

        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        if (question==null) {
            question = new OpenQuestion(new HashSet<>(),new HashSet<>(), new HashSet<>(),
                    arg0, "Descritpion "+arg0, 1 );
        }

        questionRepository.save(question);

        questionnaire.addQuestion(question);
        questionnaireRepository.save(questionnaire);

    }

    @Et("{string} du questionnaire {string} du module {string} ayant comme réponses possibles {string}, {string}, {string} oqm")
    public void duQuestionnaireDuModuleAyantCommeRéponsesPossiblesOqm(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
        Module module = moduleRepository.findByName(arg0).orElse( new Module(arg2));
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);


        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        if (question==null) {
            question = new OpenQuestion(new HashSet<>(),new HashSet<>(), new HashSet<>(),
                    arg0, "Descritpion "+arg0, 1 );
        }

        Answer answer1 = new Answer(arg3);
        answerRepository.save(answer1);
        Answer answer2 = new Answer(arg4);
        answerRepository.save(answer2);
        Answer answer3 = new Answer(arg5);
        answerRepository.save(answer3);

        question.addPossibleAnswer(answer1);
        question.addPossibleAnswer(answer2);
        question.addPossibleAnswer(answer3);

        questionRepository.save(question);
    }

    @Et("{string} du questionnaire {string} du module {string} ayant comme réponses {string} et {string} oqm")
    public void duQuestionnaireDuModuleAyantCommeRéponsesEtOqm(String arg0, String arg1, String arg2, String arg3, String arg4) {
        Module module = moduleRepository.findByName(arg0).orElse( new Module(arg2));
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);


        OpenQuestion question = (OpenQuestion) questionnaire.findQuestionByName(arg0);
        if (question==null) {
            question = new OpenQuestion(new HashSet<>(),new HashSet<>(), new HashSet<>(),
                    arg0, "Descritpion "+arg0, 1 );
        }

        Answer answer1 = answerRepository.findByAnswer(arg3).get();
        Answer answer2 = answerRepository.findByAnswer(arg4).get();

        question.addAnswer(answer1);
        question.addAnswer(answer2);

        questionRepository.save(question);
    }

    @Et("un professeur {string} ayant le module {string} oqm")
    public void unProfesseurAyantLeModuleOqm(String arg0, String arg1) {
        User teacher = userRepository.findByUsername(arg0)
                .orElse( new User(arg0, arg0+"@test.fr", encoder.encode(PASSWORD)));
        teacher.setRoles(new HashSet<>(){{
            add(roleRepository.findByName(ERole.ROLE_TEACHER)
                    .orElseThrow( () -> new RuntimeException("Error: Role is not found")));
        }});
        userRepository.save(teacher);

        Module module = moduleRepository.findByName(arg1).
                orElse(new Module(arg1));
        module.setParticipants(new HashSet<>(){{
            add(teacher);
        }});
        moduleRepository.save(module);
    }

    @Et("un professeur {string} n'ayant pas de module oqm")
    public void unProfesseurNAyantPasDeModuleOqm(String arg0) {
        User teacher = userRepository.findByUsername(arg0)
                .orElse( new User(arg0, arg0+"@test.fr", encoder.encode(PASSWORD)));
        teacher.setRoles(new HashSet<>(){{
            add(roleRepository.findByName(ERole.ROLE_TEACHER)
                    .orElseThrow( () -> new RuntimeException("Error: Role is not found")));
        }});
        userRepository.save(teacher);
    }

    @Quand("le professeur {string} ajoute une réponse possible de contenu {string} a la question {string} du questionnaire {string} du module {string} oqm")
    public void leProfesseurAjouteUneRéponsePossibleDeContenuALaQuestionDuQuestionnaireDuModuleOqm(String arg0, String arg1, String arg2, String arg3, String arg4) throws IOException {
        User teacher = userRepository.findByUsername(arg0).get();
        Module module = moduleRepository.findByName(arg4).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg3);
        Question question = questionnaire.findQuestionByName(arg2);
        Answer answer = new Answer(arg1);

        String jwTeacher = authController.generateJwt(arg0, PASSWORD);
        executePut("http://localhost:8080/api/module/"
                +module.getId()+"/questionnaire/"
                +questionnaire.getId()+"/open_question/"+question.getId()+"/possible_answer",
                new AnswerRequest(new HashSet<>(){{ add(new MyAnswer("Réponse D"));
                }}),
                jwTeacher);
    }

    @Alors("le status de la dernière réponse est {int} oqm")
    public void leStatusDeLaDernièreRéponseEstOqm(int arg0) {
        assertEquals(arg0, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Et("les réponses possibles de la question {string} du questionnaire {string} du module {string} possède un réponse de contenu {string} oqm")
    public void lesRéponsesPossiblesDeLaQuestionDuQuestionnaireDuModulePossèdeUnRéponseDeContenuOqm(String arg0, String arg1, String arg2, String arg3) {
        Module module = moduleRepository.findByName(arg2).get();
        Questionnaire questionnaire = (Questionnaire) module.findRessourceByName(arg1);
        Question question = questionnaire.findQuestionByName(arg0);

        assertTrue(openQuestionRepository.existsByName(arg3));
    }



}

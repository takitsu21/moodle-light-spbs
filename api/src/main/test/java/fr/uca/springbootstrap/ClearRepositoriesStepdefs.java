package fr.uca.springbootstrap;

import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.cours.TextRepository;
import fr.uca.api.repository.question.*;
import io.cucumber.java.fr.Etantdonnéque;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class ClearRepositoriesStepdefs extends SpringIntegration {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    TextRepository textRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    GradesQuestionnaireRepository gradesQuestionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    AnswerCodeRunnerRepository answerCodeRunnerRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    AnswerQCMRepository answerQCMRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @Autowired
    AnswerOpenQuestionRepository answerOpenQuestionRepository;


    @Etantdonnéque("les tables sont videes")
    public void lesTablesSontVidees() {
//        moduleRepository.deleteAll();
//        ressourceRepository.deleteAll();
//        coursRepository.deleteAll();
//        textRepository.deleteAll();
//        questionnaireRepository.deleteAll();
//        gradesQuestionnaireRepository.deleteAll();
//        questionRepository.deleteAll();
//        qcmRepository.deleteAll();
//        answerQCMRepository.deleteAll();
//        openQuestionRepository.deleteAll();
//        answerOpenQuestionRepository.deleteAll();
//        codeRunnerRepository.deleteAll();
//        answerCodeRunnerRepository.deleteAll();
//        answerRepository.deleteAll();
//        userRefRepository.deleteAll();
//        userToken = new HashMap<>();
    }
}

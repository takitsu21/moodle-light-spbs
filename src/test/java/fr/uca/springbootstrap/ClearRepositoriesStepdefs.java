package fr.uca.springbootstrap;

import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.cours.TextRepository;
import fr.uca.springbootstrap.repository.question.*;
import io.cucumber.java.fr.Etantdonnéque;
import org.springframework.beans.factory.annotation.Autowired;

public class ClearRepositoriesStepdefs extends SpringIntegration {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

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
        moduleRepository.deleteAll();
        ressourceRepository.deleteAll();
        coursRepository.deleteAll();
        textRepository.deleteAll();
        questionnaireRepository.deleteAll();
        gradesQuestionnaireRepository.deleteAll();
        questionRepository.deleteAll();
        qcmRepository.deleteAll();
        answerQCMRepository.deleteAll();
        openQuestionRepository.deleteAll();
        answerOpenQuestionRepository.deleteAll();
        codeRunnerRepository.deleteAll();
        answerCodeRunnerRepository.deleteAll();
        answerRepository.deleteAll();
        userRepository.deleteAll();
    }
}

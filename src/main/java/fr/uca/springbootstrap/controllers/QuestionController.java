package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.QuestionRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/{module_id}/questionnaire/{questionnaire_id}")
public class QuestionController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;


    @GetMapping("/question/{question_id}")
    public ResponseEntity<?> getQuestion(Principal principal,
                                         @PathVariable("module_id") long module_id,
                                         @PathVariable("questionnaire_id") long questionnaire_id,
                                         @PathVariable("question_id") long question_id) {

        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not exist."));
        }
        else if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: module does not exist."));
        }
        else if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: questionnaire does not exist."));
        }

        User user = optionalUser.get();
        Module module = optionalModule.get();
        if (!module.containsParticipant(user)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not belong in this module."));
        }

        Optional<Question> optionalQuestion = questionRepository.findById(question_id);
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: question does not exist."));
        }

        Question question = optionalQuestion.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: question is not in the questionnaire"));
        }

        return new ResponseEntity<>(question, HttpStatus.OK);
    }
}

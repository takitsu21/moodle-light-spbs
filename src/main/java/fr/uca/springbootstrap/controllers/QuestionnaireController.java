package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.QCMRequest;
import fr.uca.springbootstrap.payload.request.QuestionRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/{module_id}/questionnaire")
public class QuestionnaireController {

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


//    @PostMapping("/")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
//    public ResponseEntity<?> createQuestionnaire(Principal principal,
//                                                 @Valid @RequestBody RessourceRequest ressourceRequest,
//                                                 @PathVariable("module_id") long module_id) {
//
//        if (!userRepository.existsByUsername(principal.getName())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: User does not exist."));
//        }
//
//        Questionnaire questionnaire = new Questionnaire(ressourceRequest.getName(), ressourceRequest.getDescription());
//        questionnaireRepository.save(questionnaire);
//        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully created."));
//    }
//
//
//    @DeleteMapping("/{questionnaire_id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
//    public ResponseEntity<?> deleteQuestionnaire(Principal principal,
//                                                 @PathVariable("module_id") long module_id,
//                                                 @PathVariable("questionnaire_id") long questionnaire_id) {
//        if (!questionnaireRepository.existsById(questionnaire_id)) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: Questionnaire doesn't exist."));
//        }
//        else if (!userRepository.existsByUsername(principal.getName())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: User does not exist."));
//        }
//
//        Module module = moduleRepository.findById(module_id).get();
//        Questionnaire questionnaire = questionnaireRepository.findById(questionnaire_id).get();
//        questionnaireRepository.delete(questionnaire);
//        moduleRepository.delete(module);
//        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully deleted."));
//    }


    @PostMapping("/{questionnaire_id}/qcm")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addQcm(Principal principal,
                                    @Valid @RequestBody QCMRequest qcmRequest,
                                    @PathVariable("module_id") long module_id,
                                    @PathVariable("questionnaire_id") long questionnaire_id) {

        if (!userRepository.existsByUsername(principal.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }
        else if (!questionnaireRepository.existsById(questionnaire_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Questionnaire does not exist."));
        }
        else if (!moduleRepository.existsById(module_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        Module module = moduleRepository.findById(module_id).get();
        User user = userRepository.findByUsername(principal.getName()).get();
        if (!module.containsParticipant(user)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not belong to this module."));
        }

        Questionnaire questionnaire = questionnaireRepository.findById(questionnaire_id).get();
        QCM question = new QCM(qcmRequest.getNumber(), qcmRequest.getName(), qcmRequest.getDescription());
        if (questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: question already exists in the questionnaire."));
        }

        questionRepository.save(question);
        questionnaire.addQuestion(question);
        questionnaireRepository.save(questionnaire);
        return ResponseEntity.ok(new MessageResponse("Question successfully added."));
    }

    @DeleteMapping("/{questionnaire_id}/question/{question_id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeQuestion(Principal principal,
                                            @PathVariable("module_id") long module_id,
                                            @PathVariable("questionnaire_id") long questionnaire_id,
                                            @PathVariable("question_id") long question_id) {

        if (!userRepository.existsByUsername(principal.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }
        else if (!questionRepository.existsById(question_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Question does not exist."));
        }
        else if (!moduleRepository.existsById(module_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        Module module = moduleRepository.findById(module_id).get();
        User user = userRepository.findByUsername(principal.getName()).get();
        if (!module.containsParticipant(user)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not belong to this module."));
        }

        Question question = questionRepository.findById(question_id).get();
        Questionnaire questionnaire = questionnaireRepository.findById(questionnaire_id).get();

        questionnaire.removeQuestion(question);
        questionRepository.delete(question);
        questionnaireRepository.save(questionnaire);

        return ResponseEntity.ok(new MessageResponse("Question successfully removed."));
    }

}

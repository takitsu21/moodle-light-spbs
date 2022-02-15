package fr.uca.api.controllers;

import fr.uca.api.models.Module;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.User;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.Question;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRepository;
import fr.uca.api.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payload.request.QuestionRequest;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    UserRefRepository userRepository;

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/questions/")
    public ResponseEntity<?> getQuestion(){
        List<Question> questions = questionRepository.findAll();
        return ResponseEntity.ok(questions);
    }

//    @DeleteMapping("{module_id}/questionnaire/{questionnaire_id}/questions/{id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
//    public ResponseEntity<?> deleteQuestionById(@PathVariable long id){
//
//
//        if (!questionRepository.existsById(id)){
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Question doesn't exists!"));
//        }
//
//        Question question = questionRepository.findById(id).get();
//        questionRepository.delete(question);
//        return ResponseEntity.ok(new MessageResponse("Question deleted successfully!"));
//
//    }


    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/questions/{question_id}/name")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> setName(Principal principal,
                                    @Valid @RequestBody QuestionRequest questionRequest,
                                     @PathVariable("module_id") long module_id,
                                     @PathVariable("questionnaire_id") long questionnaire_id,
                                     @PathVariable("question_id") long question_id){
        Optional<Question> oQuestion = questionRepository.findById(question_id);
        Optional<Module> oModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> oQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        if (oQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }
        Optional<UserRef> ouser = userRepository.findByUsername(principal.getName());

        if (ouser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(oModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(oQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }

        User user = ouser.get();
        Questionnaire questionnaire = oQuestionnaire.get();
        Module module = oModule.get();
        Question question = oQuestion.get();

        if(!module.getRessources().contains(questionnaire)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This module does not contains this questionnaire!"));
        }
        if(!questionnaire.getQuestions().contains(question)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This questionnaire does not contains this question!"));
        }
        if (!module.getParticipants().contains(user)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to modify the name of the question!"));
        }

        question.setName(questionRequest.getName());
        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Name of the question successfully changed"));

    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/questions/{question_id}/description")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> setDescription(Principal principal,
                                            @Valid @RequestBody QuestionRequest questionRequest,
                                            @PathVariable("module_id") long module_id,
                                            @PathVariable("questionnaire_id") long questionnaire_id,
                                            @PathVariable("question_id") long question_id){
        Optional<Question> oQuestion = questionRepository.findById(question_id);
        Optional<Module> oModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> oQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<User> ouser = userRepository.findByUsername(principal.getName());

        if (oQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }
        if (oModule.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (oQuestionnaire.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (ouser.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such user!"));
        }

        User user = ouser.get();
        Question question = oQuestion.get();
        Module module = oModule.get();
        Questionnaire questionnaire = oQuestionnaire.get();

        if(!module.getRessources().contains(questionnaire)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This module does not contains this questionnaire!"));
        }
        if(!questionnaire.getQuestions().contains(question)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This questionnaire does not contains this question!"));
        }
        if (!module.getParticipants().contains(user)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You are not allowed to modify the name of the question!"));
        }
        question.setDescription(questionRequest.getDescription());
        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Description of the question successfully changed"));

    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/questions/{question_id}/number")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> setNumber(Principal principal,
                                     @Valid @RequestBody QuestionRequest questionRequest,
                                     @PathVariable("module_id") long module_id,
                                     @PathVariable("questionnaire_id") long questionnaire_id,
                                     @PathVariable("question_id") long question_id){
        Optional<Question> oQuestion = questionRepository.findById(question_id);
        Optional<Module> oModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> oQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        if (oQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }
        Optional<User> ouser = userRepository.findByUsername(principal.getName());

        if (ouser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(oModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(oQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }

        User user = ouser.get();
        Questionnaire questionnaire = oQuestionnaire.get();
        Module module = oModule.get();
        Question question = oQuestion.get();

        if(!module.getRessources().contains(questionnaire)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This module does not contains this questionnaire!"));
        }
        if(!questionnaire.getQuestions().contains(question)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This questionnaire does not contains this question!"));
        }
        if (!module.getParticipants().contains(user)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to modify the name of the question!"));
        }

        question.setNumber(questionRequest.getNumber());
        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Name of the question successfully changed"));

    }

}

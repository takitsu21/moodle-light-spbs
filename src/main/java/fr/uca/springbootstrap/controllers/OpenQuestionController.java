package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.OpenQuestion;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.AnswerRequest;
import fr.uca.springbootstrap.payload.request.MyAnswer;
import fr.uca.springbootstrap.payload.request.OpenQuestionRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.QuestionnaireRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.question.OpenQuestionRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/")
public class OpenQuestionController {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;



    @GetMapping("{modul_id}/questionnaire/{questionnaire_id}/open_question")
    public ResponseEntity<?> getOpenQuestion(){
        List<OpenQuestion> openQuestions = openQuestionRepository.findAll();
        return ResponseEntity.ok(openQuestions);
    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/add_possible_answer")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> addPossibleAnswer(Principal principal,
                                               @Valid @RequestBody AnswerRequest answerRequest,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id){
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalUser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(optionalModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(optionalQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }

        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        Module module = optionalModule.get();
        OpenQuestion question = optionalQuestion.get();

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

        question.setPossibleAnswer(answerRequest.getAnswers());
        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Possible answer successfully added"));
    }

    @DeleteMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/possible_answer")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePossibleAnswer(Principal principal,
                                               @Valid @RequestBody OpenQuestionRequest openQuestionRequest,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id){

        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalUser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(optionalModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(optionalQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }

        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        Module module = optionalModule.get();
        OpenQuestion question = optionalQuestion.get();

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

        question.setPossibleAnswers(openQuestionRequest.getPossibleAnswers());
        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Possible answer successfully removed"));
    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/add_answer")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> addAnswers(Principal principal,
                                               @Valid @RequestBody OpenQuestionRequest openQuestionRequest,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id){
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalUser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(optionalModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(optionalQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }

        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        Module module = optionalModule.get();
        OpenQuestion question = optionalQuestion.get();

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

        question.setAnswers(openQuestionRequest.getAnswers());
        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added"));
    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/remove_answer")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> removeAnswer(Principal principal,
                                               @Valid @RequestBody OpenQuestionRequest openQuestionRequest,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id){
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalUser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(optionalModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(optionalQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }

        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        Module module = optionalModule.get();
        OpenQuestion question = optionalQuestion.get();

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


        question.setAnswers(openQuestionRequest.getAnswers());
        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Answer successfully removed"));
    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/add_possible_answer")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> addStudentAnswer(Principal principal,
                                               @Valid @RequestBody OpenQuestionRequest openQuestionRequest,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id){
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalUser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(optionalModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(optionalQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }

        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        Module module = optionalModule.get();
        OpenQuestion question = optionalQuestion.get();

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

        question.setPossibleAnswers(openQuestionRequest.getPossibleAnswers());
        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Possible answer successfully added"));
    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/remove_student_answer")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> removeStudentAnswer(Principal principal,
                                               @Valid @RequestBody OpenQuestionRequest openQuestionRequest,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id){
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalUser.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if(optionalModule.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if(optionalQuestionnaire.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }

        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        Module module = optionalModule.get();
        OpenQuestion question = optionalQuestion.get();

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
                    .body(new MessageResponse("Error: You are not allowed to modify the answer of the question!"));
        }

        question.setPossibleAnswers(openQuestionRequest.getPossibleAnswers());
        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Student answer successfully removed"));
    }

}

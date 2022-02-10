package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.QuestionRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/question/")
    public ResponseEntity<?> getQuestion(){
        List<Question> questions = questionRepository.findAll();
        return ResponseEntity.ok(questions);
    }

    @DeleteMapping("{module_id}/questionnaire/{questionnaire_id}/question/{id}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuestionById(@PathVariable long id){
        if (!questionRepository.existsById(id)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Question doesn't exists!"));
        }

        Question question = questionRepository.findById(id).get();
        questionRepository.delete(question);
        return ResponseEntity.ok(new MessageResponse("Question deleted successfully!"));

    }


    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/question/{id}/name")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> setName(@Valid @RequestBody QuestionRequest questionRequest,
                                     @PathVariable("id") long id,
                                     @PathVariable("name") String new_name){
        Optional<Question> oQuestion = questionRepository.findById(id);

        if (oQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }

        Question question = oQuestion.get();
        question.setName(questionRequest.getName());
        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Name of the question successfully changed"));

    }

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/question/{id}/description")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> setDescription(@Valid @RequestBody QuestionRequest questionRequest,
                                            @PathVariable("id") long id){
        Optional<Question> oQuestion = questionRepository.findById(id);

        if (oQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }

        Question question = oQuestion.get();
        question.setDescription(questionRequest.getDescription());
        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Description of the question successfully changed"));

    }

}

package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/{id}/questionnaire/{id}/question")
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/")
    public ResponseEntity<?> getQuestion(){
        List<Question> questions = questionRepository.findAll();
        return ResponseEntity.ok(questions);
    }

    @DeleteMapping("/delete/{id}")
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


    @PutMapping("/put/{id}/name/{name}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> setName(@PathVariable("id") long id,
                                     @PathVariable("name") String new_name){
        Optional<Question> oQuestion = questionRepository.findById(id);

        if (oQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse(("Error: No such question!")));
        }
        else{
            Question question = oQuestion.get();
            question.setName(new_name);
            questionRepository.save(question);
            return ResponseEntity.ok(new MessageResponse("Name of the question successfully changed"));
        }
    }

    @PutMapping("/put/{id}/description/{description}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> setDescription(@PathVariable("id") long id,
                                     @PathVariable("description") String new_descritpion){
        Optional<Question> oQuestion = questionRepository.findById(id);

        if (oQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }
        else{
            Question question = oQuestion.get();
            question.setDescription(new_descritpion);
            questionRepository.save(question);
            return ResponseEntity.ok(new MessageResponse("Description of the question successfully changed"));
        }
    }

}

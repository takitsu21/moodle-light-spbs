package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.question.QCMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/QCM")
public class QCMController {

    @Autowired
    QCMRepository qcmRepository;

    @GetMapping("/")
    public ResponseEntity<?> getQCM(){
        List<QCM> QCMs = qcmRepository.findAll();

        return ResponseEntity.ok(QCMs);
    }

    @GetMapping("/{id}/possible_answers")
    public ResponseEntity<?> getPossibleAnswers(@PathVariable("id") long id){
        if (! qcmRepository.existsById(id)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such QCM"));
        }
        else{
            QCM qcm = qcmRepository.findById(id).get();
            return ResponseEntity.ok(qcm.getPossibleAnswers());
        }
    }

    @GetMapping("/{id}/student_answer")
    public ResponseEntity<?> getStudentAnswer(@PathVariable("id") long id){
        if (! qcmRepository.existsById(id)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such QCM"));
        }
        else{
            QCM qcm = qcmRepository.findById(id).get();
            return ResponseEntity.ok(qcm.getStudentsAnswers());
        }
    }

    //public ResponseEntity<?> setPossibleAnswers(Set<Answers> possibleAnswers);
    //public ResponseEntity<?> setPossibleAnswers(Set<String> possibleAnswers);

    //public ResponseEntity<?> addPossibleAnswers(Answer answer)
    //public ResponseEntity<?> addPossibleAnswers(String answer)

    //public ResponseEntity<?> removePossibleAnswers(long answer_id);
    //public ResponseEntity<?> removePossibleAnswers(String answers);

    //public ResponseEntity<?> getAnswer()
    //public ResponseEntity<?> setAnswer(String answer)
    //public ResponseEntity<?> setAnswer(Answer answer)

    //public ResponseEntity<?> setStudentAnswer(long id_answer)

}

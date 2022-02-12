package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.AnswerQCM;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.payload.request.AnswersRequest;
import fr.uca.springbootstrap.payload.request.MyAnswer;
import fr.uca.springbootstrap.payload.request.MyText;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.question.AnswerQCMRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.QCMRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/qcm")
public class QCMController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QCMRepository qcmRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerQCMRepository answerQCMRepository;

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


    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/qcm/{qcm_id}/possible_answer")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addPossibleAnswers(Principal principal,
                                                @Valid @RequestBody AnswersRequest answerRequest,
                                                @PathVariable("module_id") long moduleId,
                                                @PathVariable("questionnaire_id") long questionnaireId,
                                                @PathVariable("qcm_id") long QCMId) {

        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findByUsername(principal.getName());
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<QCM> oQCM = qcmRepository.findById(QCMId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (ouser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if (oressource.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }

        if (oQCM.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such QCM!"));
        }

        if (!moduleRepository.existsById(moduleId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }

        Module module = omodule.get();
        User user = ouser.get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed!"));
        }
        QCM qcm = oQCM.get();
        Set<Answer> answers = qcm.getPossibleAnswers();

        for (MyAnswer answer : answerRequest.getAnswers()) {
            answers.add(new Answer(answer.getContent()));
        }

        qcmRepository.save(qcm);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added to the QCM!"));
    }

    //public ResponseEntity<?> addPossibleAnswers(Answer answer)
    //public ResponseEntity<?> addPossibleAnswers(String answer)

    //public ResponseEntity<?> removePossibleAnswers(long answer_id);
    //public ResponseEntity<?> removePossibleAnswers(String answers);

    @DeleteMapping("/{module_id}/questionnaire/{questionnaire_id}/qcm/{qcm_id}/possible_answer/{answer_id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removePossibleAnswers(Principal principal,
                                                @PathVariable("module_id") long moduleId,
                                                @PathVariable("questionnaire_id") long questionnaireId,
                                                @PathVariable("qcm_id") long QCMId,
                                                @PathVariable("answer_id") long answerId) {

        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findByUsername(principal.getName());
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<QCM> oQCM = qcmRepository.findById(QCMId);
        Optional<Answer> oanswer = answerRepository.findById(QCMId);


        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (ouser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if (oressource.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }

        if (oQCM.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such QCM!"));
        }

        if (oanswer.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such answer!"));
        }

        if (!moduleRepository.existsById(moduleId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }

        Module module = omodule.get();
        User user = ouser.get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed!"));
        }
        QCM qcm = oQCM.get();
        qcm.getPossibleAnswers().remove(oanswer.get());

        qcmRepository.save(qcm);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added to the QCM!"));
    }

    //public ResponseEntity<?> getAnswer()
    //public ResponseEntity<?> setAnswer(String answer)
    //public ResponseEntity<?> setAnswer(Answer answer)

    //public ResponseEntity<?> setStudentAnswer(long id_answer)

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/qcm/{qcm_id}/student_answer")
    public ResponseEntity<?> addStudentAnswers(Principal principal,
                                                @Valid @RequestBody MyAnswer answer,
                                                @PathVariable("module_id") long moduleId,
                                                @PathVariable("questionnaire_id") long questionnaireId,
                                                @PathVariable("qcm_id") long QCMId) {

        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findByUsername(principal.getName());
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<QCM> oQCM = qcmRepository.findById(QCMId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (ouser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if (oressource.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }

        if (oQCM.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such QCM!"));
        }

        if (!moduleRepository.existsById(moduleId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }

        Module module = omodule.get();
        User user = ouser.get();

        if (!module.getParticipants().contains(user) && !user.isTeacher()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed!"));
        }
        QCM qcm = oQCM.get();

        if(!qcm.possibleAnswerContains(answer.getContent())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: It's not a valid answer!"));
        }

        Answer answer1=new Answer(answer.getContent());
        answerRepository.save(answer1);

        if(qcm.StudentAnswerContains(user)){
            qcm.getStudentAnswerOf(user).setAnswer(answer1);
        }
        else{
            AnswerQCM answerQCM=new AnswerQCM(answer1, user);
            answerQCMRepository.save(answerQCM);
            qcm.getStudentsAnswers().add(answerQCM);
        }

        qcmRepository.save(qcm);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added to the QCM!"));
    }

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/qcm/{qcm_id}/good_answer")
    public ResponseEntity<?> setAnswer(Principal principal,
                                               @Valid @RequestBody MyAnswer answer,
                                               @PathVariable("module_id") long moduleId,
                                               @PathVariable("questionnaire_id") long questionnaireId,
                                               @PathVariable("qcm_id") long QCMId) {

        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findByUsername(principal.getName());
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<QCM> oQCM = qcmRepository.findById(QCMId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (ouser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if (oressource.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }

        if (oQCM.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such QCM!"));
        }

        if (!moduleRepository.existsById(moduleId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }

        Module module = omodule.get();
        User user = ouser.get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed!"));
        }
        QCM qcm = oQCM.get();
        Answer answer1=new Answer(answer.getContent());
        answerRepository.save(answer1);
        qcm.setAnswer(answer1);

        qcmRepository.save(qcm);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added to the QCM!"));
    }
}

package fr.uca.api.controllers;

import fr.uca.api.models.*;
import fr.uca.api.models.Module;
import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.AnswerQCM;
import fr.uca.api.models.questions.QCM;
import fr.uca.api.repository.*;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.question.AnswerQCMRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.QCMRepository;
import fr.uca.api.util.VerifyAuthorizations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import payload.request.AnswersRequest;
import payload.request.MyAnswer;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/qcm")
public class QCMController {


    @Autowired
    UserRefRepository userRepository;

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

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/qcm/{qcm_id}/possible_answer")
    public ResponseEntity<?> addPossibleAnswers(@Valid @RequestBody AnswersRequest answerRequest,
                                                @RequestHeader Map<String, String> headers,
                                                @PathVariable("module_id") long moduleId,
                                                @PathVariable("questionnaire_id") long questionnaireId,
                                                @PathVariable("qcm_id") long QCMId) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername((String) authVerif.get("username"));
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
        UserRef user = ouser.get();

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

    @DeleteMapping("/{module_id}/questionnaire/{questionnaire_id}/qcm/{qcm_id}/possible_answer/{answer_id}")
    public ResponseEntity<?> removePossibleAnswers(@RequestHeader Map<String, String> headers,
                                                @PathVariable("module_id") long moduleId,
                                                @PathVariable("questionnaire_id") long questionnaireId,
                                                @PathVariable("qcm_id") long QCMId,
                                                @PathVariable("answer_id") long answerId) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<QCM> oQCM = qcmRepository.findById(QCMId);
        Optional<Answer> oanswer = answerRepository.findById(answerId);


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
        UserRef user = ouser.get();

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

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/qcm/{qcm_id}/student_answer")
    public ResponseEntity<?> addStudentAnswers(
                                               @Valid @RequestBody MyAnswer answer,
                                               @RequestHeader Map<String, String> headers,
                                               @PathVariable("module_id") long moduleId,
                                               @PathVariable("questionnaire_id") long questionnaireId,
                                               @PathVariable("qcm_id") long QCMId) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_STUDENT.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername((String) authVerif.get("username"));
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
        UserRef user = ouser.get();

        if (!module.getParticipants().contains(user) && !user.isTeacher(headers)) {
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
    public ResponseEntity<?> setAnswer(@RequestHeader Map<String, String> headers,
                                               @Valid @RequestBody MyAnswer answer,
                                               @PathVariable("module_id") long moduleId,
                                               @PathVariable("questionnaire_id") long questionnaireId,
                                               @PathVariable("qcm_id") long QCMId) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());

        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername((String) authVerif.get("username"));
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
        UserRef user = ouser.get();

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

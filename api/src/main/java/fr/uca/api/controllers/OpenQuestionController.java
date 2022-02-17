package fr.uca.api.controllers;

import fr.uca.api.models.ERole;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.Module;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.AnswerOpenQuestion;
import fr.uca.api.models.questions.OpenQuestion;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.AnswerOpenQuestionRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.OpenQuestionRepository;
import fr.uca.api.repository.question.QuestionRepository;
import fr.uca.api.util.VerifyAuthorizations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import payload.request.AnswersRequest;
import payload.request.MyAnswer;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/open_question/")
public class OpenQuestionController {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerOpenQuestionRepository answerOpenQuestionRepository;

    ///////////// Get Mapping

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question")
    public ResponseEntity<?> getOpenQuestion(){
        List<OpenQuestion> openQuestions = openQuestionRepository.findAll();
        return ResponseEntity.ok(openQuestions);
    }

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/possible_answers")
    public ResponseEntity<?> getPossibleAnswers(@PathVariable("question_id") long question_id){
        if(!openQuestionRepository.existsById(question_id)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such Open Question!"));
        }

        OpenQuestion openQuestion = openQuestionRepository.findById(question_id).get();
        return ResponseEntity.ok(openQuestion.getPossibleAnswers());
    }

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/answers")
    public ResponseEntity<?> getAnswers(@PathVariable("question_id") long question_id){
        if(!openQuestionRepository.existsById(question_id)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such Open Question!"));
        }

        OpenQuestion openQuestion = openQuestionRepository.findById(question_id).get();
        return ResponseEntity.ok(openQuestion.getAnswers());
    }

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/student_answers/{student_id}")
    public ResponseEntity<?> getStudentAnswers(@RequestHeader Map<String, String> headers,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id,
                                               @PathVariable("student_id") long student_id){

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalOpenQuestion = openQuestionRepository.findById(question_id);
        Optional<UserRef> optionalStudent = userRefRepository.findById(student_id);
        Optional<UserRef> optionalTeacher = userRefRepository.findByUsername((String) authVerif.get("username"));

        if (optionalModule.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such module!"));
        }
        if (optionalQuestionnaire.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such questionnaire!"));
        }
        if (optionalOpenQuestion.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such open question!"));
        }
        if (optionalStudent.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such student!"));
        }
        if (optionalTeacher.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such teacher!"));
        }

        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion openQuestion = optionalOpenQuestion.get();
        UserRef student = optionalStudent.get();
        UserRef teacher = optionalTeacher.get();

        if (!module.getRessources().contains(questionnaire)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(openQuestion)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }
        if (!module.getParticipants().contains(student)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the student!"));
        }
        if (!module.getParticipants().contains(teacher)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the teacher!"));
        }

        return ResponseEntity.ok(openQuestion.getStudentAnswerOf(student));
    }


    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/possible_answer")
    public ResponseEntity<?> addPossibleAnswers(@RequestHeader Map<String, String> headers,
                                                @Valid @RequestBody AnswersRequest answerRequest,
                                                @PathVariable("module_id") long moduleId,
                                                @PathVariable("questionnaire_id") long questionnaireId,
                                                @PathVariable("question_id") long questionId) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRefRepository.findByUsername((String) authVerif.get("username"));
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<OpenQuestion> oopenquestion = openQuestionRepository.findById(questionId);

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

        if (oopenquestion.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such open question!"));
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
        OpenQuestion openQuestion = oopenquestion.get();
        Set<Answer> answers = openQuestion.getPossibleAnswers();

        for (MyAnswer answer : answerRequest.getAnswers()) {
            Answer a =new Answer(answer.getContent());
            answerRepository.save(a);
            answers.add(a);
        }

        openQuestionRepository.save(openQuestion);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added to the open question!"));
    }

    @DeleteMapping("/{module_id}/questionnaire/{questionnaire_id}/opne_question/{question_id}/possible_answer/{answer_id}")
    public ResponseEntity<?> removePossibleAnswers(@RequestHeader Map<String, String> headers,
                                                   @PathVariable("module_id") long moduleId,
                                                   @PathVariable("questionnaire_id") long questionnaireId,
                                                   @PathVariable("question_id") long questionId,
                                                   @PathVariable("answer_id") long answerId) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRefRepository.findByUsername((String)authVerif.get("username"));
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<OpenQuestion> oopenquestion = openQuestionRepository.findById(questionId);
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

        if (oopenquestion.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such open question!"));
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
        OpenQuestion openQuestion = oopenquestion.get();
        openQuestion.getPossibleAnswers().remove(oanswer.get());

        openQuestionRepository.save(openQuestion);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added to the open question!"));
    }

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/student_answer")
    public ResponseEntity<?> addStudentAnswers(@RequestHeader Map<String, String> headers,
                                               @Valid @RequestBody AnswersRequest answers,
                                               @PathVariable("module_id") long moduleId,
                                               @PathVariable("questionnaire_id") long questionnaireId,
                                               @PathVariable("question_id") long questionId) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_STUDENT.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }

        if (!userRefRepository.existsByUsername((String) authVerif.get("username"))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }

        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRefRepository.findByUsername((String) authVerif.get("username"));
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<OpenQuestion> oopenquestion = openQuestionRepository.findById(questionId);


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

        if (oopenquestion.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such open question!"));
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
        OpenQuestion openQuestion = oopenquestion.get();

        Set<MyAnswer> answersMyAnswer = answers.getAnswers();
        Set<Answer> setAnswers = new HashSet<>();

        for (MyAnswer myAnswer : answersMyAnswer) {
            Answer answer = new Answer(myAnswer.getContent());
            answerRepository.save(answer);
            setAnswers.add(answer);

            if (!openQuestion.possibleAnswerContains(answer.getAnswer())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: It's not a valid answer!"));
            }
        }

        if (openQuestion.studentAnswerContains(user)) {
            openQuestion.getStudentAnswerOf(user).setAnswers(setAnswers);
        } else {
            AnswerOpenQuestion answerOpenQuestion = new AnswerOpenQuestion(setAnswers, user);
            answerOpenQuestionRepository.save(answerOpenQuestion);
            openQuestion.getStudentAnswers().add(answerOpenQuestion);
        }

        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("New student answer created"));
    }

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/good_answer")
    public ResponseEntity<?> setAnswer(@RequestHeader Map<String, String> headers,
                                       @Valid @RequestBody AnswersRequest answers,
                                       @PathVariable("module_id") long moduleId,
                                       @PathVariable("questionnaire_id") long questionnaireId,
                                       @PathVariable("question_id") long questionId) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRefRepository.findByUsername((String) authVerif.get("username"));
        Optional<Questionnaire> oressource = questionnaireRepository.findById(questionnaireId);
        Optional<OpenQuestion> oopenquestion = openQuestionRepository.findById(questionId);

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

        if (oopenquestion.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such open question!"));
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
        OpenQuestion openQuestion = oopenquestion.get();
        Set<MyAnswer> answersMyAnswer = answers.getAnswers();
        Set<Answer> setAnswers = new HashSet<>();

        for (MyAnswer myAnswer: answersMyAnswer){
            Answer answer=new Answer(myAnswer.getContent());
            answerRepository.save(answer);
            setAnswers.add(answer);

        }
        openQuestion.setAnswers(setAnswers);

        openQuestionRepository.save(openQuestion);
        return ResponseEntity.ok(new MessageResponse("Answer successfully added to the open question!"));
    }

}

package fr.uca.api.controllers;

import fr.uca.api.models.ERole;
import fr.uca.api.models.Module;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.AnswerOpenQuestion;
import fr.uca.api.models.questions.OpenQuestion;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.OpenQuestionRepository;
import fr.uca.api.repository.question.QuestionRepository;
import fr.uca.api.util.VerifyAuthorizations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import payload.request.AnswerRequest;
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
@RequestMapping("/api/modules/")
public class OpenQuestionController {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    UserRefRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    OpenQuestionRepository openQuestionRepository;

    @Autowired
    AnswerRepository answerRepository;

    ///////////// Get Mapping

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question")
    public ResponseEntity<?> getOpenQuestion(@RequestHeader Map<String, String> headers) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers);
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        List<OpenQuestion> openQuestions = openQuestionRepository.findAll();
        return ResponseEntity.ok(openQuestions);
    }

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/possible_answers")
    public ResponseEntity<?> getPossibleAnswers(
            @RequestHeader Map<String, String> headers,
            @PathVariable("question_id") long question_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers);
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        if (!openQuestionRepository.existsById(question_id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such Open Question!"));
        }

        OpenQuestion openQuestion = openQuestionRepository.findById(question_id).get();
        return ResponseEntity.ok(openQuestion.getPossibleAnswers());
    }

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/answers")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAnswers(@RequestHeader Map<String, String> headers,
                                        @PathVariable("question_id") long question_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        if (!openQuestionRepository.existsById(question_id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such Open Question!"));
        }

        OpenQuestion openQuestion = openQuestionRepository.findById(question_id).get();
        return ResponseEntity.ok(openQuestion.getAnswers());
    }

    @GetMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/student_answers/{student_id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> getStudentAnswers(
                                               @RequestHeader Map<String, String> headers,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id,
                                               @PathVariable("student_id") long student_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(question_id);
        Optional<OpenQuestion> optionalOpenQuestion = openQuestionRepository.findById(question_id);
        Optional<UserRef> optionalStudent = userRepository.findById(student_id);
        Optional<UserRef> optionalTeacher = userRepository.findByUsername((String) authVerif.get("username"));

        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such questionnaire!"));
        }
        if (optionalOpenQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such open question!"));
        }
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such student!"));
        }
        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error no such teacher!"));
        }

        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion openQuestion = optionalOpenQuestion.get();
        UserRef student = optionalStudent.get();
        UserRef teacher = optionalTeacher.get();

        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(openQuestion)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }
        if (!module.getParticipants().contains(student)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the student!"));
        }
        if (!module.getParticipants().contains(teacher)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the teacher!"));
        }

        return ResponseEntity.ok(openQuestion.getStudentAnswerByStudent(student));
    }

    ///////////////// Delete Mapping

    @DeleteMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/possible_answer/{possible_answer_id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePossibleAnswer(
                                                  @RequestHeader Map<String, String> headers,
                                                  @PathVariable("module_id") long module_id,
                                                  @PathVariable("questionnaire_id") long questionnaire_id,
                                                  @PathVariable("question_id") long question_id,
                                                  @PathVariable("answer_id") long answer_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> optionalTeacher = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);
        Optional<Answer> optionalAnswer = answerRepository.findById(answer_id);

        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such teacher!"));
        }
        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }
        if (optionalAnswer.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such answer!"));
        }

        UserRef teacher = optionalTeacher.get();
        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion question = optionalQuestion.get();
        Answer answer = optionalAnswer.get();

        if (!module.getParticipants().contains(teacher)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the user doesn't have the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }
        if (!question.getPossibleAnswers().contains(answer)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the question does not contains the answer as a possible answer!"));
        }

        question.removePossibleAnswer(answer);

        if (question.getPossibleAnswers().isEmpty()) {
            question.addPossibleAnswer(answer);
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can't remove the only possible answer of the question"));
        }

        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Possible answer successfully removed from the question"));
    }

    @DeleteMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/answers/{answer_id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAnswer(
                                          @RequestHeader Map<String, String> headers,
                                          @PathVariable("module_id") long module_id,
                                          @PathVariable("questionnaire_id") long questionnaire_id,
                                          @PathVariable("question_id") long question_id,
                                          @PathVariable("answer_id") long answer_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> optionalTeacher = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);
        Optional<Answer> optionalAnswer = answerRepository.findById(answer_id);

        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such teacher!"));
        }
        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }
        if (optionalAnswer.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such answer!"));
        }

        UserRef teacher = optionalTeacher.get();
        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion question = optionalQuestion.get();
        Answer answer = optionalAnswer.get();

        if (!module.getParticipants().contains(teacher)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the user doesn't have the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }
        if (!question.getAnswers().contains(answer)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the question does not contains the answer as an answer!"));
        }

        question.removeAnswer(answer);

        if (question.getAnswers().isEmpty()) {
            question.addAnswer(answer);
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can't remove the only answer of the question"));
        }

        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Answer successfully removed from the question"));
    }

    @DeleteMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/student_answers/{answer_id}")
//    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> deleteStudentAnswer(
                                                 @RequestHeader Map<String, String> headers,
                                                 @PathVariable("module_id") long module_id,
                                                 @PathVariable("questionnaire_id") long questionnaire_id,
                                                 @PathVariable("question_id") long question_id,
                                                 @PathVariable("answer_id") long answer_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_STUDENT.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> optionalStudent = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);
        Optional<Answer> optionalAnswer = answerRepository.findById(answer_id);

        if (optionalStudent.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such teacher!"));
        }
        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }
        if (optionalAnswer.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such answer!"));
        }

        UserRef student = optionalStudent.get();
        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion question = optionalQuestion.get();
        Answer answer = optionalAnswer.get();

        if (!module.getParticipants().contains(student)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the user doesn't have the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }
        if (!question.getAnswers().contains(answer)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the question does not contains the answer as an answer!"));
        }

        AnswerOpenQuestion studentAnswer = question.getStudentAnswerByStudent(student);
        studentAnswer.getAnswers().remove(answer);

        if (studentAnswer.getAnswers().isEmpty()) {
            studentAnswer.getAnswers().add(answer);
            return ResponseEntity.badRequest().body(new MessageResponse("Error: You can't remove the only answer of the question"));
        }

        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Answer successfully removed from the question"));
    }
    ///////////// Put Mapping

    @PutMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/possible_answer")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> addPossibleAnswer(
                                               @Valid @RequestBody AnswerRequest answerRequest,
                                               @RequestHeader Map<String, String> headers,
                                               @PathVariable("module_id") long module_id,
                                               @PathVariable("questionnaire_id") long questionnaire_id,
                                               @PathVariable("question_id") long question_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> optionalTeacher = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such teacher!"));
        }
        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }

        UserRef teacher = optionalTeacher.get();
        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion question = optionalQuestion.get();

        if (!module.getParticipants().contains(teacher)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the user doesn't have the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }

        Set<Answer> answers = question.getPossibleAnswers();

        for (MyAnswer answer : answerRequest.getAnswers()) {
            Answer new_answer = new Answer(answer.getContent());
            answers.add(new_answer);
        }

        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("New possible answers created"));

    }

    @PutMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/answer/{answer_id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> addAnswer(
                                       @Valid @RequestBody AnswerRequest answerRequest,
                                       @RequestHeader Map<String, String> headers,
                                       @PathVariable("module_id") long module_id,
                                       @PathVariable("questionnaire_id") long questionnaire_id,
                                       @PathVariable("question_id") long question_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> optionalTeacher = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such teacher!"));
        }
        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }

        UserRef teacher = optionalTeacher.get();
        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion question = optionalQuestion.get();

        if (!module.getParticipants().contains(teacher)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the user doesn't have the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }

        Set<Answer> answers = question.getAnswers();

        for (MyAnswer answer : answerRequest.getAnswers()) {
            Answer theAnswer = question.getPossibleAnswerByContent(answer.getContent());
            if (theAnswer == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: the answer is not in the possible answer"));
            }
            answers.add(theAnswer);
        }

        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("New answer created"));
    }

    @PutMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/student_answer")
//    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> addStudentAnswer(
                                              @Valid @RequestBody AnswerRequest answerRequest,
                                              @RequestHeader Map<String, String> headers,
                                              @PathVariable("module_id") long module_id,
                                              @PathVariable("questionnaire_id") long questionnaire_id,
                                              @PathVariable("question_id") long question_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> optionalStudent = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalStudent.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such teacher!"));
        }
        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }

        UserRef student = optionalStudent.get();
        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion question = optionalQuestion.get();

        if (!module.getParticipants().contains(student)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the user doesn't have the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }

        Set<Answer> answers = question.getAnswers();

        for (MyAnswer answer : answerRequest.getAnswers()) {
            Answer theAnswer = question.getPossibleAnswerByContent(answer.getContent());
            if (theAnswer == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: the answer is not in the possible answer"));
            }
            answers.add(theAnswer);
        }

        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("New student answer created"));
    }

    ///////////// Post Mapping

    @PostMapping("{module_id}/questionnaire/{questionnaire_id}/open_question/{question_id}/possible_answer/{answer_id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> modifyPossibleAnswer(
                                                  @Valid @RequestBody MyAnswer myAnswer,
                                                  @RequestHeader Map<String, String> headers,
                                                  @PathVariable("module_id") long module_id,
                                                  @PathVariable("questionnaire_id") long questionnaire_id,
                                                  @PathVariable("question_id") long question_id,
                                                  @PathVariable("answer_id") long answer_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> optionalTeacher = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
        Optional<OpenQuestion> optionalQuestion = openQuestionRepository.findById(question_id);

        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such teacher!"));
        }
        if (optionalModule.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such module!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (optionalQuestion.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No such question!"));
        }

        UserRef teacher = optionalTeacher.get();
        Module module = optionalModule.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        OpenQuestion question = optionalQuestion.get();

        if (!module.getParticipants().contains(teacher)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the user doesn't have the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the module does not contains the questionnaire!"));
        }
        if (!questionnaire.getQuestions().contains(question)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: the questionnaire does not contains the question!"));
        }


        Answer answer = question.getPossibleAnswerById(answer_id);
        answer.setAnswer(myAnswer.getContent());

        openQuestionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Possible answers modify created"));

    }


}

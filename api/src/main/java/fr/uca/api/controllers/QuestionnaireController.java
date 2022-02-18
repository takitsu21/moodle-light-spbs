package fr.uca.api.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.uca.api.models.Module;
import fr.uca.api.models.*;
import fr.uca.api.models.questions.*;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.GradesQuestionnaireRepository;
import fr.uca.api.repository.question.QuestionRepository;
import fr.uca.api.util.CodeRunnerExec;
import fr.uca.api.util.VerifyAuthorizations;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payload.request.CodeRunnerRequest;
import payload.request.QCMRequest;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/{module_id}/questionnaire")
public class QuestionnaireController {
    @Autowired
    UserRefRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    GradesQuestionnaireRepository gradesQuestionnaireRepository;


    @PostMapping("/{questionnaire_id}/qcm")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addQcm(
            @Valid @RequestBody QCMRequest qcmRequest,
            @RequestHeader Map<String, String> headers,
            @PathVariable("module_id") long module_id,
            @PathVariable("questionnaire_id") long questionnaire_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        if (!userRepository.existsByUsername((String) authVerif.get("username"))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        } else if (!questionnaireRepository.existsById(questionnaire_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Questionnaire does not exist."));
        } else if (!moduleRepository.existsById(module_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        Module module = moduleRepository.findById(module_id).get();
        UserRef user = userRepository.findByUsername((String) authVerif.get("username")).get();
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

    @DeleteMapping("/{questionnaire_id}/questions/{question_id}")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeQuestion(
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
        if (!userRepository.existsByUsername((String) authVerif.get("username"))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        } else if (!questionRepository.existsById(question_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Question does not exist."));
        } else if (!moduleRepository.existsById(module_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        Module module = moduleRepository.findById(module_id).get();
        UserRef user = userRepository.findByUsername((String) authVerif.get("username")).get();
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

    @PostMapping("/{questionnaire_id}")
    public ResponseEntity<?> submitQuestionnaire(
            @RequestHeader Map<String, String> headers,
            @PathVariable("module_id") long moduleId,
            @PathVariable("questionnaire_id") long questionnaireId) throws IOException {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_STUDENT.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<UserRef> optionalUser = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaireId);
        if (optionalModule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (!userRepository.existsByUsername((String) authVerif.get("username"))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }

        Module module = optionalModule.get();
        UserRef user = userRepository.findByUsername((String) authVerif.get("username")).get();
        Questionnaire questionnaire = optionalQuestionnaire.get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is not registered in the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: questionnaire does not belong to this module!"));
        }

        int note = 0;

        for (Question question : questionnaire.getQuestions()) {

            if (question instanceof CodeRunner) {
                CodeRunner codeRunner = (CodeRunner) question;
                UserRef currentStudent;

                for (AnswerCodeRunner answerCodeRunner : codeRunner.getStudentsAnswers()) {
                    currentStudent = answerCodeRunner.getStudent();
                    if (currentStudent.equals(user)) {
                        CloseableHttpResponse resp = VerifyAuthorizations.executePost(
                                VerifyAuthorizations.codeRunnerHost + "api/coderunner/",
                                new CodeRunnerRequest(
                                        answerCodeRunner.getAnswer().getAnswer(),
                                        codeRunner.getTest(),
                                        codeRunner.getAnwser().getAnswer()), null);
                        String jsonString = EntityUtils.toString(resp.getEntity());
                        GsonBuilder builder = new GsonBuilder();
                        builder.setPrettyPrinting();

                        Gson gson = builder.create();
                        Map<String, Object> map = gson.fromJson(jsonString, Map.class);
                        EntityUtils.consume(resp.getEntity());
                        if ((Boolean) map.get("success")) {
                            note++;
                        }
                    }
                }
            } else if (question instanceof QCM) {
                QCM qcm = (QCM) question;
                UserRef currentStudent;
                for (AnswerQCM studentQcmAnswer : qcm.getStudentsAnswers()) {
                    currentStudent = studentQcmAnswer.getStudent();
                    if (currentStudent.equals(user)) {
                        Answer studentAnswer = studentQcmAnswer.getAnswer();
                        if (studentAnswer.getAnswer().equals(qcm.getAnswer().getAnswer())) {
                            note++;
                        }
                    }
                }
            } else if (question instanceof OpenQuestion) {
                OpenQuestion openQuestion = (OpenQuestion) question;
                UserRef currentStudent;
                for (AnswerOpenQuestion studentOpenAnswer : openQuestion.getStudentAnswers()) {
                    currentStudent = studentOpenAnswer.getStudent();
                    if (currentStudent.equals(user)) {
                        int tmpNote = 0;
                        for (Answer answer : studentOpenAnswer.getAnswers()) {
                            for (Answer openQuestionAnswer : openQuestion.getAnswers()) {
                                if (Objects.equals(answer.getAnswer(), openQuestionAnswer.getAnswer())) {
                                    tmpNote++;
                                }
                            }
                        }
                        if (tmpNote == openQuestion.getAnswers().size()) {
                            note++;
                        }
                    }
                }
            }
        }

        GradesQuestionnaire gradesQuestionnaire = new GradesQuestionnaire(questionnaire, note, user);
        gradesQuestionnaireRepository.save(gradesQuestionnaire);

        Map<String, Integer> notes = new HashMap<>();
        notes.put("note", note);
        notes.put("nbQuestion", questionnaire.getQuestions().size());

        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{questionnaire_id}/grades")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getGrades(
            @RequestHeader Map<String, String> headers,
            @PathVariable("module_id") long moduleId,
            @PathVariable("questionnaire_id") long questionnaireId) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<UserRef> optionalUser = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaireId);
        if (optionalModule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }
        if (optionalQuestionnaire.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire!"));
        }
        if (!userRepository.existsByUsername((String) authVerif.get("username"))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }

        Module module = optionalModule.get();
        UserRef user = userRepository.findByUsername((String) authVerif.get("username")).get();
        Questionnaire questionnaire = optionalQuestionnaire.get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is not registered in the module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: questionnaire does not belong to this module!"));
        }

        Map<String, String> grades = new HashMap<>();
        Set<GradesQuestionnaire> gradesQuestionnaire = questionnaire.getStudentsGrades();
        for (GradesQuestionnaire grade : gradesQuestionnaire) {
            grades.put(grade.getStudent().getUsername(), grade.getFinalGradeString());
        }

        return ResponseEntity.ok(grades);
    }
}

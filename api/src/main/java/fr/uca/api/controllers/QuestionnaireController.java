package fr.uca.api.controllers;

import fr.uca.api.models.*;
import fr.uca.api.models.Module;
import fr.uca.api.models.questions.QCM;
import fr.uca.api.models.questions.Question;
import fr.uca.api.models.questions.*;

import fr.uca.api.repository.*;
import fr.uca.api.repository.question.GradesQuestionnaireRepository;
import fr.uca.api.repository.question.QuestionRepository;
import fr.uca.api.util.CodeRunnerExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import payload.request.Grade;
import payload.request.QCMRequest;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/{module_id}/questionnaire")
public class QuestionnaireController {
    @Autowired
    UserRefRepository userRepository;

    @Autowired
    RoleCoursesRepository roleCoursesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    GradesQuestionnaireRepository gradesQuestionnaireRepository;


//    @PostMapping("/")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
//    public ResponseEntity<?> createQuestionnaire(Principal principal,
//                                                 @Valid @RequestBody RessourceRequest ressourceRequest,
//                                                 @PathVariable("module_id") long module_id) {
//
//        if (!userRepository.existsByUsername(principal.getName())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: User does not exist."));
//        }
//
//        Questionnaire questionnaire = new Questionnaire(ressourceRequest.getName(), ressourceRequest.getDescription());
//        questionnaireRepository.save(questionnaire);
//        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully created."));
//    }
//
//
//    @DeleteMapping("/{questionnaire_id}")
//    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
//    public ResponseEntity<?> deleteQuestionnaire(Principal principal,
//                                                 @PathVariable("module_id") long module_id,
//                                                 @PathVariable("questionnaire_id") long questionnaire_id) {
//        if (!questionnaireRepository.existsById(questionnaire_id)) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: Questionnaire doesn't exist."));
//        }
//        else if (!userRepository.existsByUsername(principal.getName())) {
//            return ResponseEntity.badRequest()
//                    .body(new MessageResponse("Error: User does not exist."));
//        }
//
//        Module module = moduleRepository.findById(module_id).get();
//        Questionnaire questionnaire = questionnaireRepository.findById(questionnaire_id).get();
//        questionnaireRepository.delete(questionnaire);
//        moduleRepository.delete(module);
//        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully deleted."));
//    }


    @PostMapping("/{questionnaire_id}/qcm")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addQcm(Principal principal,
                                    @Valid @RequestBody QCMRequest qcmRequest,
                                    @PathVariable("module_id") long module_id,
                                    @PathVariable("questionnaire_id") long questionnaire_id) {

        if (!userRepository.existsByUsername(principal.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }
        else if (!questionnaireRepository.existsById(questionnaire_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Questionnaire does not exist."));
        }
        else if (!moduleRepository.existsById(module_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        Module module = moduleRepository.findById(module_id).get();
        UserRef user = userRepository.findByUsername(principal.getName()).get();
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeQuestion(Principal principal,
                                            @PathVariable("module_id") long module_id,
                                            @PathVariable("questionnaire_id") long questionnaire_id,
                                            @PathVariable("question_id") long question_id) {

        if (!userRepository.existsByUsername(principal.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }
        else if (!questionRepository.existsById(question_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Question does not exist."));
        }
        else if (!moduleRepository.existsById(module_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        Module module = moduleRepository.findById(module_id).get();
        UserRef user = userRepository.findByUsername(principal.getName()).get();
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
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitQuestionnaire(Principal principal,
                                                 @PathVariable("module_id") long moduleId,
                                                 @PathVariable("questionnaire_id") long questionnaireId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<UserRef> optionalUser = userRepository.findByUsername(principal.getName());
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
        }        if (!userRepository.existsByUsername(principal.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }

        Module module = optionalModule.get();
        UserRef user = userRepository.findByUsername(principal.getName()).get();
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
                        CodeRunnerExec codeRunnerExec = new CodeRunnerExec();
                        Map<String, Object> exec = codeRunnerExec.
                                execPy(answerCodeRunner.getAnswer().getAnswer(), codeRunner);

                        if ((Boolean) exec.get("success")) {
                            note++;
                        }
                    }
                }
            } else if (question instanceof QCM) {
                QCM qcm = (QCM) question;
                UserRef currentStudent;
//                for (AnswerQCM studentAnswer : qcm.getStudentsAnswers()) {
//                    currentStudent = studentAnswer.getStudent();
//                    if (currentStudent.equals(user)) {
//                        Answer studentAnswer = currentStudent.getAnswer();
//                        if (studentAnswer.getAnswer().equals(qcm.getAnswer().getAnswer()) {
//                            note ++;
//                        }
//                    }
//                }
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getGrades(Principal principal,
                                                 @PathVariable("module_id") long moduleId,
                                                 @PathVariable("questionnaire_id") long questionnaireId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<UserRef> optionalUser = userRepository.findByUsername(principal.getName());
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
        if (!userRepository.existsByUsername(principal.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }

        Module module = optionalModule.get();
        UserRef user = userRepository.findByUsername(principal.getName()).get();
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
        Set<Grade> grades = new HashSet<>();
        Set<GradesQuestionnaire> gradesQuestionnaire =  questionnaire.getStudentsGrades();
        for (GradesQuestionnaire grade : gradesQuestionnaire) {
            grades.add(new Grade(
                    grade.getNote(),
                    questionnaire.getNbQuestions(),
                    grade.getStudent().getUsername()
            ));
        }

        return ResponseEntity.ok(grades);
    }
}

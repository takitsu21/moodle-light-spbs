package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.QCM;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.QCMRequest;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.*;
import fr.uca.springbootstrap.payload.request.QuestionnaireRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.question.GradesQuestionnaireRepository;
import fr.uca.springbootstrap.repository.question.QuestionRepository;
import fr.uca.util.CodeRunnerExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/{module_id}/questionnaire")
public class QuestionnaireController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    GradesQuestionnaireRepository gradesQuestionnaireRepository;



    @PostMapping("/")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> addQuestionnaire(Principal principal,
                                              @Valid @RequestBody QuestionnaireRequest questionnaireRequest,
                                              @PathVariable("module_id") long module_id) {

        Optional<User> oUser = userRepository.findByUsername(principal.getName());
        Optional<Module> oModule = moduleRepository.findById(module_id);

        if (oUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }
        else if (oModule.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        User user = oUser.get();
        Questionnaire questionnaire = new Questionnaire(questionnaireRequest.getName(), questionnaireRequest.getDescription(), questionnaireRequest.getNum());
        Module module = oModule.get();

        if (!module.containsParticipant(user)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User is not registered in the module."));
        }
        questionnaireRepository.save(questionnaire);

        module.addRessource(questionnaire);
        moduleRepository.save(module);

        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully added."));
    }


    @DeleteMapping("/{questionnaire_id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeQuestionnaire(Principal principal,
                                                 @PathVariable("module_id") long module_id,
                                                 @PathVariable("questionnaire_id") long questionnaire_id) {

        if (!userRepository.existsByUsername(principal.getName())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        }
        else if (!questionnaireRepository.existsById(questionnaire_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: questionnaire does not exist."));
        }
        Module module = moduleRepository.findById(module_id).get();
        User user = userRepository.findByUsername(principal.getName()).get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is not registered in the module."));
        }


        Questionnaire questionnaire = questionnaireRepository.findById(questionnaire_id).get();
        module.removeRessource(questionnaire);
        questionnaireRepository.delete(questionnaire);

        moduleRepository.save(module);


        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully removed."));
    }


    @GetMapping("/{questionnaire_id}")
    public ResponseEntity<?> getQuestionnaire(Principal principal,
                                              @PathVariable("module_id") long module_id,
                                              @PathVariable("questionnaire_id") long questionnaire_id) {

        Optional<Module> oModule = moduleRepository.findById(module_id);
        Optional<User> oUser = userRepository.findByUsername(principal.getName());

        if (oModule.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: module does not exist."));
        }

        if (oUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not exist."));
        }

        Module module = oModule.get();
        User user = oUser.get();
        Questionnaire questionnaire = null;

        if (!module.containsParticipant(user)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not belong in this module."));
        }

        for (Ressource ressource : module.getRessources()) {
            if (ressource.getId() == questionnaire_id) {
                questionnaire = (Questionnaire) ressource;
            }
        }

        if (questionnaire == null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: questionnaire does not exist."));
        }

        if (!questionnaire.isVisibility() && !user.isTeacher()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not have permission to access this questionnaire."));
        }

        return new ResponseEntity<>(questionnaire, HttpStatus.OK);
    }


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
        User user = userRepository.findByUsername(principal.getName()).get();
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
        User user = userRepository.findByUsername(principal.getName()).get();
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
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
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
        User user = userRepository.findByUsername(principal.getName()).get();
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
                User currentStudent;

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
                User currentStudent;
                for (AnswerQCM studentQcmAnswer : qcm.getStudentsAnswers()) {
                    currentStudent = studentQcmAnswer.getStudent();
                    if (currentStudent.equals(user)) {
                        Answer studentAnswer = studentQcmAnswer.getAnswer();
                        if (studentAnswer.getAnswer().equals(qcm.getAnswer().getAnswer())) {
                            note ++;
                        }
                    }
                }
            }
            //TODO faire pour open question
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
                                                 @PathVariable("module_id") long module_id,
                                                 @PathVariable("questionnaire_id") long questionnaire_id) {
        Optional<Module> optionalModule = moduleRepository.findById(module_id);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaire_id);
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

        Module module = optionalModule.get();
        User user = optionalUser.get();
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
        Set<GradesQuestionnaire> gradesQuestionnaire =  questionnaire.getStudentsGrades();
        for (GradesQuestionnaire grade : gradesQuestionnaire) {
            grades.put(grade.getStudent().getUsername(),grade.getFinalGradeString());
        }

        return ResponseEntity.ok(grades);
    }
}

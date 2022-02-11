package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.AnswerCodeRunner;
import fr.uca.springbootstrap.models.questions.CodeRunner;
import fr.uca.springbootstrap.payload.request.CodeRunnerRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.cours.TextRepository;
import fr.uca.springbootstrap.repository.question.AnswerCodeRunnerRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.CodeRunnerRepository;
import fr.uca.util.CodeRunnerExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module")
public class CodeRunnerController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerCodeRunnerRepository answerCodeRunnerRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/code_runner")
    public ResponseEntity<?> addCodeRunnerQuestion(Principal principal,
                                                   @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
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
        }

        User user = optionalUser.get();
        Module module = optionalModule.get();

        Questionnaire questionnaire = optionalQuestionnaire.get();
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire in the module!"));
        }
        Answer answer = new Answer(codeRunnerRequest.getAnswer());
        answerRepository.save(answer);
        CodeRunner question = new CodeRunner(codeRunnerRequest.getNumber(),
                codeRunnerRequest.getName(),
                codeRunnerRequest.getDescription(),
                codeRunnerRequest.getTest(), answer);
        codeRunnerRepository.save(question);
        questionnaire.getQuestions().add(question);

        questionnaireRepository.save(questionnaire);

        return ResponseEntity.ok(new MessageResponse("Code runner question successfully added!"));
    }

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/code_runner/{code_runner_id}")
    public ResponseEntity<?> submitCodeRunner(Principal principal,
                                              @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
                                              @PathVariable("module_id") long moduleId,
                                              @PathVariable("questionnaire_id") long questionnaireId,
                                              @PathVariable("code_runner_id") long codeRunnerId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaireId);
        Optional<CodeRunner> optionalCodeRunner = codeRunnerRepository.findById(codeRunnerId);

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
        if (optionalCodeRunner.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such question!"));
        }
        Module module = optionalModule.get();
        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: you do not belong to this module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire in the module!"));
        }

        CodeRunner question = optionalCodeRunner.get();
        Answer answer;
        if (question.getStudentsAnswers().contains(user)) {
            answer = new Answer(codeRunnerRequest.getCode());
            answerRepository.save(answer);
            question.getStudentAnswer(user).setAnswer(answer);
        } else {
            answer = new Answer(codeRunnerRequest.getCode());
            answerRepository.save(answer);
            AnswerCodeRunner answerCodeRunner = new AnswerCodeRunner(
                    answer,
                    user
            );
            answerCodeRunnerRepository.save(answerCodeRunner);
            question.getStudentsAnswers().add(answerCodeRunner);
        }
        codeRunnerRepository.save(question);

//        CodeRunnerExec codeRunnerExec = new CodeRunnerExec();
//        Map<String, Object> exec = codeRunnerExec.execPy(codeRunnerRequest.getCode(), question);
//
//        return ResponseEntity.ok(exec);
        return ResponseEntity.ok(new MessageResponse("Code runner answer successfully added to the questionnaire!"));
    }

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/code_runner/{code_runner_id}/test")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> testCodeRunner(Principal principal,
                                              @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
                                              @PathVariable("module_id") long moduleId,
                                              @PathVariable("questionnaire_id") long questionnaireId,
                                              @PathVariable("code_runner_id") long codeRunnerId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        Optional<Questionnaire> optionalQuestionnaire = questionnaireRepository.findById(questionnaireId);
        Optional<CodeRunner> optionalCodeRunner = codeRunnerRepository.findById(codeRunnerId);

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
        if (optionalCodeRunner.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such question!"));
        }
        Module module = optionalModule.get();
        User user = optionalUser.get();
        Questionnaire questionnaire = optionalQuestionnaire.get();
        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: you do not belong to this module!"));
        }
        if (!module.getRessources().contains(questionnaire)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such questionnaire in the module!"));
        }

        CodeRunner question = optionalCodeRunner.get();
        Answer answer;
        if (question.getStudentsAnswers().contains(user)) {
            answer = new Answer(codeRunnerRequest.getCode());
            answerRepository.save(answer);
            question.getStudentAnswer(user).setAnswer(answer);
        } else {
            answer = new Answer(codeRunnerRequest.getCode());
            answerRepository.save(answer);
            AnswerCodeRunner answerCodeRunner = new AnswerCodeRunner(
                    answer,
                    user
            );
            answerCodeRunnerRepository.save(answerCodeRunner);
            question.getStudentsAnswers().add(answerCodeRunner);
        }
        codeRunnerRepository.save(question);

        CodeRunnerExec codeRunnerExec = new CodeRunnerExec();
        Map<String, Object> exec = codeRunnerExec.execPy(codeRunnerRequest.getCode(), question);

        return ResponseEntity.ok(exec);
    }
}

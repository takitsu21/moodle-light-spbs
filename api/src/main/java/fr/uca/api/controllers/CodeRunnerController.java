package fr.uca.api.controllers;

import fr.uca.api.models.Module;
import fr.uca.api.models.Questionnaire;
import fr.uca.api.models.UserRef;
import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.AnswerCodeRunner;
import fr.uca.api.models.questions.CodeRunner;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.question.AnswerCodeRunnerRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.CodeRunnerRepository;
import fr.uca.api.util.CodeRunnerExec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import payload.request.CodeRunnerRequest;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/{module_id}/questionnaire/{questionnaire_id}")
public class CodeRunnerController {
    @Autowired
    UserRefRepository userRepository;



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

    @PostMapping("/code_runner")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addCodeRunnerQuestion(Principal principal,
                                                   @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
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

        UserRef user = optionalUser.get();
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

    @PostMapping("/code_runner/{code_runner_id}")
    public ResponseEntity<?> submitCodeRunner(Principal principal,
                                              @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
                                              @PathVariable("module_id") long moduleId,
                                              @PathVariable("questionnaire_id") long questionnaireId,
                                              @PathVariable("code_runner_id") long codeRunnerId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<UserRef> optionalUser = userRepository.findByUsername(principal.getName());
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
        UserRef user = optionalUser.get();
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
        return ResponseEntity.ok(new MessageResponse("Code runner answer successfully added to the questionnaire!"));
    }

    @PostMapping("/code_runner/{code_runner_id}/test")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> testCodeRunner(Principal principal,
                                            @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
                                            @PathVariable("module_id") long moduleId,
                                            @PathVariable("questionnaire_id") long questionnaireId,
                                            @PathVariable("code_runner_id") long codeRunnerId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<UserRef> optionalUser = userRepository.findByUsername(principal.getName());
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
        UserRef user = optionalUser.get();
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

package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.Ressource;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.CodeRunner;
import fr.uca.springbootstrap.payload.request.CodeRunnerRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.cours.TextRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.CodeRunnerRepository;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.StringWriter;
import java.security.Principal;
import java.util.HashMap;
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
    RessourceRepository ressourceRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    TextRepository textRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @Autowired
    AnswerRepository answerRepository;

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
        Answer answer = new Answer(codeRunnerRequest.getAnswer(), user);
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

    @PostMapping("/{module_id}/questionnaire/{questionnaire_id}/code_runner/{code_runner_id}/submit")
    public ResponseEntity<?> submitCodeRunner(Principal principal,
                                              @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
                                              @PathVariable("module_id") long moduleId,
                                              @PathVariable("questionnaire_id") long questionnaireId,
                                              @PathVariable("code_runner_id") long codeRunnerId) {
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


        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            Map<String, Boolean> success = new HashMap<>();
            CodeRunner question = codeRunnerRepository.findById(codeRunnerId).get();

            StringWriter output = new StringWriter();
            pyInterp.setOut(output);
            pyInterp.exec(codeRunnerRequest.getCode() + "\n" + question.getTest());
            boolean isValid = output.toString().trim().equals(question.getAnwser().getAnswer());
            success.put("success", isValid);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e));
        }
    }
}

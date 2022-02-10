package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.models.questions.CodeRunner;
import fr.uca.springbootstrap.payload.request.CodeRunnerRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.cours.TextRepository;
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


    @PostMapping("/{module_id}/code_runner/{code_runner_id}/submit")
    public ResponseEntity<?> submitCodeRunner(Principal principal,
                                              @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
                                              @PathVariable("module_id") long moduleId,
                                              @PathVariable("code_runner_id") long codeRunnerId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
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
        Module module = optionalModule.get();
        User user = optionalUser.get();
        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: you do not belong to this module!"));
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
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e));
        }
    }
}

package fr.uca.api.controllers;

import fr.uca.api.models.*;
import fr.uca.api.models.Module;
import fr.uca.api.repository.*;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.cours.TextRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.CodeRunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import payload.request.MyText;
import payload.request.TextRequest;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/{module_id}/cours/{cours_id}")
public class TextController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRefRepository userRepository;

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
    AnswerRepository answerRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;


    @PostMapping("/texts")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addText(Principal principal,
                                     @Valid @RequestBody TextRequest textRequest,
                                     @PathVariable("module_id") long moduleId,
                                     @PathVariable("cours_id") long coursId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername(principal.getName());
        Optional<Cours> oressource = coursRepository.findById(coursId);

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
                    .body(new MessageResponse("Error: No such cours!"));
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
                    .body(new MessageResponse("Error: You are not allowed to delete courses!"));
        }
        Cours cours = oressource.get();
        Set<Text> texts = cours.getTexts();

        for (MyText text : textRequest.getTexts()) {
            texts.add(new Text(text.getNum(), text.getContent()));
        }

        coursRepository.save(cours);
        return ResponseEntity.ok(new MessageResponse("Texts successfully added to the course!"));
    }

    @DeleteMapping("/texts/{text_id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeText(Principal principal,
                                        @PathVariable("module_id") long moduleId,
                                        @PathVariable("cours_id") long coursId,
                                        @PathVariable("text_id") long textId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername(principal.getName());
        Optional<Text> otext = textRepository.findById(textId);
        Optional<Cours> ocours = coursRepository.findById(coursId);

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
        if (otext.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such text!"));
        }
        if (ocours.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such text!"));
        }

        if (!moduleRepository.existsById(moduleId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }

        Module module = omodule.get();
        UserRef user = ouser.get();
        Cours cours = ocours.get();
        Text text = otext.get();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to delete courses!"));
        }
        if (!cours.getTexts().contains(text)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This text is not in the course!"));
        }

        cours.getTexts().remove(text);
        coursRepository.save(cours);
        return ResponseEntity.ok(new MessageResponse("Texts successfully removed from the course!"));
    }

    @GetMapping("/{module_id}/cours/{cours_id}/texts")
    public ResponseEntity<?> getTexts(@PathVariable("module_id") long moduleId,
                                      @PathVariable("cours_id") long coursId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<Cours> ocours = coursRepository.findById(coursId);

        if (omodule.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (ocours.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such course!"));
        }
        Cours cours = ocours.get();
        return ResponseEntity.ok(cours.getTexts());
    }
}

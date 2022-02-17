package fr.uca.api.controllers;

import fr.uca.api.models.*;
import fr.uca.api.models.Module;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.RessourceRepository;
import fr.uca.api.repository.UserRefRepository;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.cours.TextRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.CodeRunnerRepository;
import fr.uca.api.util.VerifyAuthorizations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import payload.request.CoursRequest;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/{module_id}")
public class CoursController {

    @Autowired
    UserRefRepository userRepository;

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

    @PostMapping("/cours")
    public ResponseEntity<?> addCours(
                                      @RequestHeader Map<String, String> headers,
                                      @Valid @RequestBody CoursRequest coursRequest,
                                      @PathVariable("module_id") long moduleId) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername((String) authVerif.get("username"));

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

        Module module = omodule.get();
        UserRef user = ouser.get();
        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to add courses!"));
        }

        Cours cours = new Cours(coursRequest.getName(), coursRequest.getDescription(), coursRequest.getNum());
        if (module.containsRessource(cours)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: this course already exists!"));
        }


        coursRepository.save(cours);
        module.getRessources().add(cours);
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("Course added to the module successfully!"));
    }

    @DeleteMapping("/cours/{cours_id}")
    public ResponseEntity<?> deleteCours(
                                         @RequestHeader Map<String, String> headers,
                                         @PathVariable("module_id") long moduleId,
                                         @PathVariable("cours_id") long coursId) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRepository.findByUsername((String) authVerif.get("username"));
        Optional<Cours> ocourse = coursRepository.findById(coursId);

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
        if (ocourse.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such course!"));
        }

        if (!moduleRepository.existsById(moduleId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }
        Module module = omodule.get();
        UserRef user = ouser.get();
        Cours course = ocourse.get();


        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to delete courses!"));
        }

        if (!module.getRessources().contains(course)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This course is not in the current module!"));
        }

        coursRepository.delete(course);
        return ResponseEntity.ok(new MessageResponse("Course removed from the module successfully!"));
    }
}

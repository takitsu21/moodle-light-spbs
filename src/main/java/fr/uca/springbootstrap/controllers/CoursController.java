package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.Cours;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.Ressource;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.payload.request.CoursRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.cours.TextRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.CodeRunnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules/{module_id}")
public class CoursController {
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
    AnswerRepository answerRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;

    @PostMapping("/cours")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addCours(Principal principal,
                                      @Valid @RequestBody CoursRequest coursRequest,
                                      @PathVariable("module_id") long moduleId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findByUsername(principal.getName());

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
        User user = ouser.get();
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteCours(Principal principal,
                                         @PathVariable("module_id") long moduleId,
                                         @PathVariable("cours_id") long coursId) {

        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<User> ouser = userRepository.findByUsername(principal.getName());
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
        User user = ouser.get();
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

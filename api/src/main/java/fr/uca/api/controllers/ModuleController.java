package fr.uca.api.controllers;

import fr.uca.api.models.Module;
import fr.uca.api.models.*;
import fr.uca.api.repository.ModuleRepository;
import fr.uca.api.repository.QuestionnaireRepository;
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
import payload.request.ModuleRequest;
import payload.request.QuestionnaireRequest;
import payload.request.VisibilityRequest;
import payload.response.MessageResponse;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules")
public class ModuleController {
    @Autowired
    UserRefRepository userRefRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RessourceRepository ressourceRepository;

    @Autowired
    CoursRepository coursRepository;

    @Autowired
    TextRepository textRepository;
    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    CodeRunnerRepository codeRunnerRepository;


    @PostMapping("/{id}/participants/{userid}")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addUser(@RequestHeader Map<String, String> headers,
                                     @PathVariable long id, @PathVariable int userid) {
        System.out.println("add User: "+headers);
        Map<String, Object> authVerif = VerifyAuthorizations
                .verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<UserRef> ouser = userRefRepository.findByUserId(userid);
        if (!omodule.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (!ouser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }

        Module module = omodule.get();
        UserRef user = ouser.get();
        UserRef actor = userRefRepository.findByUsername((String) authVerif.get("username")).get();

        Set<UserRef> participants = module.getParticipants();
        if (participants.contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User already registered in this module!"));
        }
        if ((participants.isEmpty() && actor.equals(user))
                || participants.contains(actor)) {
            participants.add(user);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to add user!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }

    @DeleteMapping("/{id}/participants/{userid}")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> remvoveUser(@RequestHeader Map<String, String> headers,
                                         @PathVariable long id, @PathVariable int userid) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<UserRef> ouser = userRefRepository.findByUserId(userid);
        if (!omodule.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        if (!ouser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such user!"));
        }

        Module module = omodule.get();
        UserRef user = ouser.get();
        UserRef actor = userRefRepository.findByUsername((String) authVerif.get("username")).get();

        Set<UserRef> participants = module.getParticipants();
        if (!participants.isEmpty()
                && participants.contains(user)
                && participants.contains(actor)) {
            participants.remove(user);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Not allowed to add user!"));
        }
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("User successfully removed to module!"));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<?> getParticipants(@RequestHeader Map<String, String> headers,
                                             @PathVariable long id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers);
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Module module = moduleRepository.findById(id).get();
        UserRef user = userRefRepository.findByUsername((String) authVerif.get("username")).get();
        Map<Integer, String> paticipantView = new HashMap<>();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to see participants!"));

        }
        for (UserRef participant : module.getParticipants()) {
            paticipantView.put(participant.getUserId(), participant.getUsername());
        }
        return ResponseEntity.ok(paticipantView);
    }

    @PostMapping("/")
    public ResponseEntity<?> addModule(@Valid @RequestBody ModuleRequest moduleRequest,
                                       @RequestHeader Map<String, String> headers) {

        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        if (moduleRepository.existsByName(moduleRequest.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module already exists!"));
        }

        Module module = new Module(moduleRequest.getName());
        moduleRepository.save(module);
        return ResponseEntity.ok(new MessageResponse("Module created successfully!"));
    }

    @GetMapping("/")
    public ResponseEntity<?> getModules(
                                        @RequestHeader Map<String, String> headers) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers);
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        UserRef user = userRefRepository.findByUsername((String) authVerif.get("username")).get();
        List<Module> modules = moduleRepository.findAll();
        Map<Long, String> modulesView = new HashMap<>();

        for (Module module : modules) {
            if (module.getParticipants().contains(user)) {
                modulesView.put(module.getId(), module.getName());
            }
        }
        return ResponseEntity.ok(modulesView);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteModuleById(@RequestHeader Map<String, String> headers,
                                              @PathVariable long id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        if (!moduleRepository.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }

        Module module = moduleRepository.findById(id).get();
        moduleRepository.delete(module);

        return ResponseEntity.ok(new MessageResponse("Module deleted successfully!"));
    }

    @PostMapping("/{module_id}/visibility/{ressource_id}")
//    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateVisibility(
                                              @Valid @RequestBody VisibilityRequest visibilityRequest,
                                              @RequestHeader Map<String, String> headers,
                                              @PathVariable("module_id") long moduleId,
                                              @PathVariable("ressource_id") long ressourceId) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRefRepository.findByUsername((String) authVerif.get("username"));
        Optional<Ressource> oressource = ressourceRepository.findById(ressourceId);

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
                    .body(new MessageResponse("Error: No such ressource!"));
        }

        if (!moduleRepository.existsById(moduleId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Module doesn't exists!"));
        }
        Module module = omodule.get();
        Ressource ressource = oressource.get();
        UserRef user = ouser.get();
        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to modify ressource visibility!"));
        }
        if (!module.getRessources().contains(ressource)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ressource not in Module!"));
        }
        ressource.setVisibility(visibilityRequest.getVisibility());
        ressourceRepository.save(ressource);
        return ResponseEntity.ok(new MessageResponse("Ressource visibility change successfully!"));
    }

    @GetMapping("/{id}/ressources")
    public ResponseEntity<?> getRessources(
                                           @RequestHeader Map<String, String> headers,
                                           @PathVariable("id") long id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers);
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Module module = moduleRepository.findById(id).get();
        UserRef user = userRefRepository.findByUsername((String) authVerif.get("username")).get();
        Map<Long, String> ressourceView = new HashMap<>();

        if (!module.getParticipants().contains(user)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You are not allowed to see ressource!"));

        }

        for (Ressource ressource : module.getRessources()) {
            if (ressource.isVisibility() || user.isTeacher(headers)) {
                ressourceView.put(ressource.getId(), ressource.getName());
            }
        }
        return new ResponseEntity<Map>(ressourceView, HttpStatus.OK);

    }

    @GetMapping("/{module_id}/questionnaire/{questionnaire_id}")
    public ResponseEntity<?> getQuestionnaire(
                                              @RequestHeader Map<String, String> headers,
                                              @PathVariable("module_id") long module_id,
                                              @PathVariable("questionnaire_id") long questionnaire_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers);
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<Module> oModule = moduleRepository.findById(module_id);
        Optional<UserRef> oUser = userRefRepository.findByUsername((String) authVerif.get("username"));

        if (oModule.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: module does not exist."));
        }

        if (oUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not exist."));
        }

        Module module = oModule.get();
        UserRef user = oUser.get();
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

        if (!questionnaire.isVisibility() && !user.isTeacher(headers)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: user does not have permission to access this questionnaire."));
        }

        return new ResponseEntity<>(questionnaire, HttpStatus.OK);
    }


    @PostMapping("/{module_id}/questionnaire")
//	@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> addQuestionnaire(@Valid @RequestBody QuestionnaireRequest questionnaireRequest,
                                              @RequestHeader Map<String, String> headers,
                                              @PathVariable("module_id") long module_id) {
        System.out.println("dedans add");
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers,
                ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        Optional<UserRef> oUser = userRefRepository.
                findByUsername((String) authVerif.get("username"));
        Optional<Module> oModule = moduleRepository.findById(module_id);

        if (oUser.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        } else if (oModule.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Module does not exist."));
        }

        UserRef user = oUser.get();
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


    @DeleteMapping("{module_id}/questionnaire/{questionnaire_id}")
//	@PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> removeQuestionnaire(
                                                 @RequestHeader Map<String, String> headers,
                                                 @PathVariable("module_id") long module_id,
                                                 @PathVariable("questionnaire_id") long questionnaire_id) {
        Map<String, Object> authVerif = VerifyAuthorizations.verify(headers, ERole.ROLE_TEACHER.toString());
        if (!VerifyAuthorizations.isSuccess(authVerif)) {
            return ResponseEntity.
                    status(HttpStatus.UNAUTHORIZED).
                    body(authVerif);
        }
        if (!userRefRepository.existsByUsername((String) authVerif.get("username"))) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User does not exist."));
        } else if (!questionnaireRepository.existsById(questionnaire_id)) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: questionnaire does not exist."));
        }
        Module module = moduleRepository.findById(module_id).get();
        UserRef user = userRefRepository.findByUsername((String) authVerif.get("username")).get();

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
}

package fr.uca.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.uca.api.models.Module;
import fr.uca.api.models.*;

import fr.uca.api.repository.*;
import fr.uca.api.repository.cours.CoursRepository;
import fr.uca.api.repository.cours.TextRepository;
import fr.uca.api.repository.question.AnswerRepository;
import fr.uca.api.repository.question.CodeRunnerRepository;
import fr.uca.api.util.VerifyAuthorizations;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addUser(Principal principal, @PathVariable long id, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<UserRef> ouser = userRefRepository.findById(userid);
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
        UserRef actor = userRefRepository.findByUsername(principal.getName()).get();

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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> remvoveUser(Principal principal, @PathVariable long id, @PathVariable long userid) {
        Optional<Module> omodule = moduleRepository.findById(id);
        Optional<UserRef> ouser = userRefRepository.findById(userid);
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
		UserRef actor = userRefRepository.findByUsername(principal.getName()).get();

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
    public ResponseEntity<?> getParticipants(Principal principal, @PathVariable long id) {
        Module module = moduleRepository.findById(id).get();
		UserRef user = userRefRepository.findByUsername(principal.getName()).get();
        Map<Integer, String> paticipantView = new HashMap<>();

		if(!module.getParticipants().contains(user)) {
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
									   @RequestHeader Map<String, String> headers) throws IOException {

		if (!VerifyAuthorizations.verify(headers, null, ERole.ROLE_TEACHER.toString())) {
			return ResponseEntity.
					status(HttpStatus.UNAUTHORIZED).
					body(new MessageResponse("Jwt token not found!"));
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
    public ResponseEntity<?> getModules(Principal principal) {
		UserRef user = userRefRepository.findByUsername(principal.getName()).get();
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteModuleById(@PathVariable long id) {
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
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateVisibility(Principal principal,
											  @Valid @RequestBody VisibilityRequest visibilityRequest,
                                              @PathVariable("module_id") long moduleId,
                                              @PathVariable("ressource_id") long ressourceId) {
        Optional<Module> omodule = moduleRepository.findById(moduleId);
        Optional<UserRef> ouser = userRefRepository.findByUsername(principal.getName());
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
	public ResponseEntity<?> getRessources(Principal principal, @PathVariable("id") long id) {
		Module module = moduleRepository.findById(id).get();
		UserRef user = userRefRepository.findByUsername(principal.getName()).get();
		Map<Long, String> ressourceView = new HashMap<>();

		if(!module.getParticipants().contains(user)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: You are not allowed to see ressource!"));

		}

		for (Ressource ressource : module.getRessources()) {
			if (ressource.isVisibility() || user.isTeacher()){
				ressourceView.put(ressource.getId(), ressource.getName());
			}
		}
		return new ResponseEntity<Map>(ressourceView, HttpStatus.OK);

	}


	private Cours findCoursByNameInModule(Module module, String name) {
		for (Ressource cours : module.getRessources()) {
			if (cours.getName().equalsIgnoreCase(name)) {
				return (Cours) cours;
			}
		}
		return null;
	}


	@GetMapping("/{module_id}/questionnaire/{questionnaire_id}")
	public ResponseEntity<?> getQuestionnaire(Principal principal,
											  @PathVariable("module_id") long module_id,
											  @PathVariable("questionnaire_id") long questionnaire_id) {

		Optional<Module> oModule = moduleRepository.findById(module_id);
		Optional<UserRef> oUser = userRefRepository.findByUsername(principal.getName());

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

		if (!questionnaire.isVisibility() && !user.isTeacher()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: user does not have permission to access this questionnaire."));
		}

		return new ResponseEntity<>(questionnaire, HttpStatus.OK);
	}


	@PostMapping("{module_id}/questionnaire")
	@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
	public ResponseEntity<?> addQuestionnaire(Principal principal,
											  @Valid @RequestBody QuestionnaireRequest questionnaireRequest,
											  @PathVariable("module_id") long module_id) {

		Optional<UserRef> oUser = userRefRepository.findByUsername(principal.getName());
		Optional<Module> oModule = moduleRepository.findById(module_id);

		if (oUser.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: User does not exist."));
		}
		else if (oModule.isEmpty()) {
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
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> removeQuestionnaire(Principal principal,
												 @PathVariable("module_id") long module_id,
												 @PathVariable("questionnaire_id") long questionnaire_id) {

		if (!userRefRepository.existsByUsername(principal.getName())) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: User does not exist."));
		}
		else if (!questionnaireRepository.existsById(questionnaire_id)) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: questionnaire does not exist."));
		}
		Module module = moduleRepository.findById(module_id).get();
		UserRef user = userRefRepository.findByUsername(principal.getName()).get();

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

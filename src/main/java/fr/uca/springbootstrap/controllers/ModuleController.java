package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.models.questions.Answer;
import fr.uca.springbootstrap.models.questions.CodeRunner;
import fr.uca.springbootstrap.models.questions.Question;
import fr.uca.springbootstrap.payload.request.*;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.repository.cours.CoursRepository;
import fr.uca.springbootstrap.repository.cours.TextRepository;
import fr.uca.springbootstrap.repository.question.AnswerRepository;
import fr.uca.springbootstrap.repository.question.CodeRunnerRepository;
import fr.uca.springbootstrap.util.Pair;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.StringWriter;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module")
public class ModuleController {
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


//
//	@Autowired
//	PasswordEncoder encoder;
//
//	@Autowired
//	JwtUtils jwtUtils;

	@PostMapping("/{id}/participants/{userid}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addUser(Principal principal, @PathVariable long id, @PathVariable long userid) {
		Optional<Module> omodule = moduleRepository.findById(id);
		Optional<User> ouser = userRepository.findById(userid);
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
		User user = ouser.get();
		User actor = userRepository.findByUsername(principal.getName()).get();

		Set<User> participants = module.getParticipants();
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
		Optional<User> ouser = userRepository.findById(userid);
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
		User user = ouser.get();
		User actor = userRepository.findByUsername(principal.getName()).get();

		Set<User> participants = module.getParticipants();
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
		System.out.println(module.getParticipants());
		for (User participant : module.getParticipants()) {
			System.out.println(participant.getUsername());
		}
		return ResponseEntity.ok(new MessageResponse("success"));
	}

	@PostMapping("/")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> createModule(@Valid @RequestBody ModuleRequest moduleRequest) {
		if (moduleRepository.existsByName(moduleRequest.getName())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Module already exists!"));
		}

		// Create new user's account
		Module module = new Module(moduleRequest.getName());
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("Module created successfully!"));
	}

	@GetMapping("/")
	public ResponseEntity<?> getModules(Principal principal) {
		User user = userRepository.findByUsername(principal.getName()).get();
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

	@PostMapping("/{module_id}/ressourceVisible/{ressource_id}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> ressourceVisible(Principal principal, @PathVariable("module_id") long moduleId, @PathVariable("ressource_id")  long ressourceId) {
		Optional<Module> omodule = moduleRepository.findById(moduleId);
		Optional<User> ouser = userRepository.findByUsername(principal.getName());
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
		User user = ouser.get();
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
		ressource.setVisibility(true);
		ressourceRepository.save(ressource);
		return ResponseEntity.ok(new MessageResponse("Ressource visibility change successfully!"));
	}

	@PostMapping("/{module_id}/ressourceInvisible/{ressource_id}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> ressourceInvisible(Principal principal, @PathVariable("module_id") long moduleId, @PathVariable("ressource_id")  long ressourceId) {
		Optional<Module> omodule = moduleRepository.findById(moduleId);
		Optional<User> ouser = userRepository.findByUsername(principal.getName());
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
		User user = ouser.get();
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
		ressource.setVisibility(false);
		ressourceRepository.save(ressource);
		return ResponseEntity.ok(new MessageResponse("Ressource visibility change successfully!"));
	}

	@GetMapping("/{id}/ressources")
	public ResponseEntity<?> getRessourcess(Principal principal, @PathVariable("id") long id) {
		Module module = moduleRepository.findById(id).get();
		User user = userRepository.findByUsername(principal.getName()).get();

		for (Ressource ressource : module.getRessources()) {
			if(ressource.isVisibility() || (module.getParticipants().contains(user) && user.hasTeacher())) {
				System.out.println(ressource.getName());
			}
		}
		return ResponseEntity.ok(new MessageResponse("success"));
	}

	@PutMapping("/{module_id}/code_runner")
	public ResponseEntity<?> addCodeRunnerQuestion(Principal principal,
												   @Valid @RequestBody CodeRunnerRequest codeRunnerRequest,
												   @PathVariable("module_id") long moduleId) {
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

		User user = optionalUser.get();

		Answer answer = new Answer(codeRunnerRequest.getAnswer(), user);
		answerRepository.save(answer);

		CodeRunner question = new CodeRunner(codeRunnerRequest.getNumber(),
				codeRunnerRequest.getName(),
				codeRunnerRequest.getDescription(),
				codeRunnerRequest.getTest(), answer);

		codeRunnerRepository.save(question);
		return ResponseEntity.ok(new MessageResponse("Code runner quesiton successfully added!"));
	}

	private Cours findCoursByNameInModule(Module module, String name) {
		for (Ressource cours : module.getRessources()) {
			if (cours.getName().equalsIgnoreCase(name)) {
				return (Cours) cours;
			}
		}
		return null;
	}
}

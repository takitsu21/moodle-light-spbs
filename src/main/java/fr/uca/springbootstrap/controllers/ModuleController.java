package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.payload.request.*;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.*;
import fr.uca.springbootstrap.security.jwt.JwtUtils;
import moodle.users.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
	QuestionnaireRepository questionnaireRepository;

	@Autowired
	TextRepository textRepository;


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

		// Create new module
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


	@PostMapping("/{module_id}/cours")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addCours(Principal principal,
									  @Valid @RequestBody RessourceRequest ressourceRequest,
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

		if (!moduleRepository.existsById(moduleId)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Module doesn't exists!"));
		}
		Module module = omodule.get();
		User user = ouser.get();
		if (!module.getParticipants().contains(user)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: You are not allowed to add courses!"));
		}
		Cours cours = new Cours(ressourceRequest.getName(), ressourceRequest.getDescription(), ressourceRequest.getNum());
		coursRepository.save(cours);
		return ResponseEntity.ok(new MessageResponse("Course added to the module successfully!"));
	}

	@DeleteMapping("/{module_id}/cours/{cours_id}")
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

		coursRepository.delete(course);
		return ResponseEntity.ok(new MessageResponse("Course removed from the module successfully!"));
	}

	@PostMapping("/{module_id}/cours/{cours_id}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addText(Principal principal,
									 @Valid @RequestBody TextRequest textRequest,
									 @PathVariable("module_id") long moduleId,
									 @PathVariable("cours_id") long coursId) {
		Optional<Module> omodule = moduleRepository.findById(moduleId);
		Optional<User> ouser = userRepository.findByUsername(principal.getName());
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

		Module module = omodule.get();
		User user = ouser.get();

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

	@DeleteMapping("/{module_id}/cours/{cours_id}/texts/{text_id}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> removeText(Principal principal,
									 @PathVariable("module_id") long moduleId,
									 @PathVariable("cours_id") long coursId,
									 @PathVariable("text_id") long textId) {
		Optional<Module> omodule = moduleRepository.findById(moduleId);
		Optional<User> ouser = userRepository.findByUsername(principal.getName());
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
		User user = ouser.get();
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


	@PostMapping("{module_id}/questionnaire/{questionnaire_id}")
	@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
	public ResponseEntity<?> addQuestionnaire(Principal principal,
											  @PathVariable("module_id") long module_id,
											  @PathVariable("questionnaire_id") long questionnaire_id) {

		Optional<User> oUser = userRepository.findByUsername(principal.getName());
		Optional<Questionnaire> oQuestionnaire = questionnaireRepository.findById(questionnaire_id);
		Optional<Module> oModule = moduleRepository.findById(module_id);

		if (oUser.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: User does not exist."));
		}
		else if (oQuestionnaire.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Questionnaire does not exist."));
		}
		else if (oModule.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: Module does not exist."));
		}

		User user = oUser.get();
		Questionnaire questionnaire = oQuestionnaire.get();
		Module module = oModule.get();

		if (!module.containsParticipant(user)) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: User is not registered in the module."));
		}
		module.addRessource(questionnaire);

		moduleRepository.save(module);
		questionnaireRepository.save(questionnaire);

		return ResponseEntity.ok(new MessageResponse("Questionnaire successfully added."));
	}


	@DeleteMapping("{module_id}/questionnaire/{questionnaire_id}")
	@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
	public ResponseEntity<?> removeQuestionnaire(Principal principal,
												 @PathVariable("module_id") long module_id,
												 @PathVariable("questionnaire_id") long questionnaire_id) {

		if (!userRepository.existsByUsername(principal.getName())) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: User does not exist."));
		}
		else if (!questionnaireRepository.existsById(questionnaire_id)) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Error: questionnaire does not exist."));
		}

		Questionnaire questionnaire = questionnaireRepository.findById(questionnaire_id).get();
		Module module = moduleRepository.findById(module_id).get();
		module.removeRessource(questionnaire);

		moduleRepository.save(module);
		questionnaireRepository.delete(questionnaire);

		return ResponseEntity.ok(new MessageResponse("Questionnaire successfully removed."));
	}
}

package fr.uca.springbootstrap.controllers;

import fr.uca.springbootstrap.models.*;
import fr.uca.springbootstrap.models.Module;
import fr.uca.springbootstrap.payload.request.CoursRequest;
import fr.uca.springbootstrap.payload.request.ModuleRequest;
import fr.uca.springbootstrap.payload.request.RessourceRequest;
import fr.uca.springbootstrap.payload.request.SignupRequest;
import fr.uca.springbootstrap.payload.response.MessageResponse;
import fr.uca.springbootstrap.repository.ModuleRepository;
import fr.uca.springbootstrap.repository.RessourceRepository;
import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

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

	@PostMapping("/create")
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
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> getModules() {
		List<Module> modules = moduleRepository.findAll();
		return ResponseEntity.ok(modules);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
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
		Ressource ressource = new Cours(ressourceRequest.getName(), ressourceRequest.getDescription());

		ressourceRepository.save(ressource);
		return ResponseEntity.ok(new MessageResponse("Course added to the module successfully!"));
	}

	@DeleteMapping("/{module_id}/cours/{cours_id}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> deleteCours(Principal principal,
									  @PathVariable("module_id") long moduleId,
										 @PathVariable("cours_id") long coursId) {

		Optional<Module> omodule = moduleRepository.findById(moduleId);
		Optional<User> ouser = userRepository.findByUsername(principal.getName());
		Optional<Ressource> oressource = ressourceRepository.findById(coursId);

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
		User user = ouser.get();
		Ressource ressource = oressource.get();


		if (!module.getParticipants().contains(user)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: You are not allowed to delete courses!"));
		}

		ressourceRepository.delete(ressource);
		return ResponseEntity.ok(new MessageResponse("Course removed from the module successfully!"));
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
}

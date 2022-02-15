package fr.uca.springbootstrap;

import fr.uca.auth.models.ERole;
import fr.uca.auth.models.Role;
import fr.uca.springbootstrap.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Optional;


@SpringBootApplication
public class SpringBootSecurityPostgresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityPostgresqlApplication.class, args);
    }
}

@Component
class AddRoles implements ApplicationRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        Optional<Role> student = roleRepository.findByName(ERole.ROLE_STUDENT);
        Optional<Role> teacher = roleRepository.findByName(ERole.ROLE_TEACHER);
        Optional<Role> admin = roleRepository.findByName(ERole.ROLE_ADMIN);

        if (teacher.isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_TEACHER));
        }
        if (student.isEmpty() ) {
            roleRepository.save(new Role(ERole.ROLE_STUDENT));

        }
        if (admin.isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));

        }

    }
}
package fr.uca.api;

import fr.uca.api.models.ERole;
import fr.uca.api.models.RoleCourses;
import fr.uca.api.repository.RoleCoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Optional;

@SpringBootApplication
@AutoConfigurationPackage
@Configuration
//@ComponentScan(basePackages = {"auth.service"})
public class SpringBootSecurityPostgresqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityPostgresqlApplication.class, args);
    }
}

@Component
class AddRoles implements ApplicationRunner {
    @Autowired
    private RoleCoursesRepository roleCoursesRepository;

    @Override
    public void run(ApplicationArguments args) {
        Optional<RoleCourses> student = roleCoursesRepository.findByName(ERole.ROLE_STUDENT);
        Optional<RoleCourses> teacher = roleCoursesRepository.findByName(ERole.ROLE_TEACHER);
        Optional<RoleCourses> admin = roleCoursesRepository.findByName(ERole.ROLE_ADMIN);

        if (teacher.isEmpty()) {
            roleCoursesRepository.save(new RoleCourses(ERole.ROLE_TEACHER));
        }
        if (student.isEmpty()) {
            roleCoursesRepository.save(new RoleCourses(ERole.ROLE_STUDENT));

        }
        if (admin.isEmpty()) {
            roleCoursesRepository.save(new RoleCourses(ERole.ROLE_ADMIN));

        }

    }
}
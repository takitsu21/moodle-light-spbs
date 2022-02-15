package fr.uca.api.repository;

import fr.uca.api.models.ERole;
import fr.uca.api.models.RoleCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleCoursesRepository extends JpaRepository<RoleCourses, Long> {
    Optional<RoleCourses> findByName(ERole name);

    Optional<RoleCourses> findById(long id);
}

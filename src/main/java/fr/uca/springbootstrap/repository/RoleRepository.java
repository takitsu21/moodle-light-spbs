package fr.uca.springbootstrap.repository;

import fr.uca.auth.models.ERole;
import fr.uca.auth.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    Optional<Role> findById(long id);
}

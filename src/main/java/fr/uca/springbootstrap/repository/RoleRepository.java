package fr.uca.springbootstrap.repository;

import java.util.Optional;

import fr.uca.springbootstrap.models.ERole;
import fr.uca.springbootstrap.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}

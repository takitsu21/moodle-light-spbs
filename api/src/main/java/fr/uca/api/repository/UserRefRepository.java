package fr.uca.api.repository;

import fr.uca.api.models.UserRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRefRepository extends JpaRepository<UserRef, Long> {
    Optional<UserRef> findById(Long aLong);

    Optional<UserRef> findByUsername(String username);

    boolean existsById(Long aLong);

    Boolean existsByUsername(String username);

}
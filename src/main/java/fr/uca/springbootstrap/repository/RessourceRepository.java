package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    Optional<Ressource> findByName(String name);

    Optional<Ressource> findById(long id);

    Boolean existsByName(String name);

    Boolean existsById(long id);
}

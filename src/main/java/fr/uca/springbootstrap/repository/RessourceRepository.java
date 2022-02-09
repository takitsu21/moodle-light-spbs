package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    Optional<Ressource> findByName(String name);

    Optional<Ressource> findByIdAndDtype(long id, String dtype);

    Boolean existsByName(String name);

    Boolean existsById(long id);

    Boolean existsByIdAndDtype(long id, String dtype);
}

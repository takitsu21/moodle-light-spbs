package fr.uca.api.repository;

import fr.uca.api.models.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {
    Boolean existsByName(String name);

    Boolean existsById(long id);
}

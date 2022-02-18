package fr.uca.api.repository;

import fr.uca.api.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findByName(String name);

    Optional<Module> findById(long id);

    Boolean existsByName(String name);

    List<Module> findAll();

    Boolean existsById(long id);
}

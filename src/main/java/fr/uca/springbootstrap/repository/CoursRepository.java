package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.Cours;
import fr.uca.springbootstrap.models.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoursRepository extends JpaRepository<Cours, Long> {
    Optional<Cours> findById(long id);

    Optional<Cours> findByName(String name);


}

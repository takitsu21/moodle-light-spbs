package fr.uca.api.repository.cours;

import fr.uca.api.models.Cours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoursRepository extends JpaRepository<Cours, Long> {
    Optional<Cours> findById(long id);

    Optional<Cours> findByName(String name);


}

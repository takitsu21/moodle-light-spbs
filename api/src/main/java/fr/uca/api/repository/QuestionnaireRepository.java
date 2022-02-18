package fr.uca.api.repository;

import fr.uca.api.models.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    Optional<Questionnaire> findById(long id);

    Boolean existsById(long id);

    Boolean existsByName(String name);

    List<Questionnaire> findAll();
}

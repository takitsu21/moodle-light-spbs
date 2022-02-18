package fr.uca.api.repository.question;

import fr.uca.api.models.questions.OpenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpenQuestionRepository extends JpaRepository<OpenQuestion, Long> {

    Optional<OpenQuestion> findById(long id);

    Boolean existsById(long id);

    Boolean existsByName(String name);

    List<OpenQuestion> findAll();
}

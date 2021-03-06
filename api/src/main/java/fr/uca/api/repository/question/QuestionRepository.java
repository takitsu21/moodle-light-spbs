package fr.uca.api.repository.question;

import fr.uca.api.models.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByName(String name);
    Optional<Question> findById(long id);
    Optional<Question> findByNumber(int number);

    Boolean existsById(long id);
    Boolean existsByName(String name);
    Boolean existsByNumber(int number);

    List<Question> findAll();
}

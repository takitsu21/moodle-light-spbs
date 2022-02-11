package fr.uca.springbootstrap.repository.question;

import fr.uca.springbootstrap.models.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByName(String name);

    Optional<Question> findById(long id);

    Boolean existsById(long id);

    Boolean existsByName(String name);

    List<Question> findAll();
}

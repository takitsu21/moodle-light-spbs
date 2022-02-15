package fr.uca.api.repository.question;

import fr.uca.api.models.questions.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findById(long id);

    Boolean existsById(long id);

    Boolean existsByAnswer(String Answer);


    List<Answer> findAll();

}

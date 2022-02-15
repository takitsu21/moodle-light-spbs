package fr.uca.api.repository.question;

import fr.uca.api.models.questions.AnswerCodeRunner;
import fr.uca.api.models.questions.AnswerCodeRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerCodeRunnerRepository extends JpaRepository<AnswerCodeRunner, Long> {
    Optional<AnswerCodeRunner> findById(Long aLong);
}

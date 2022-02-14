package fr.uca.springbootstrap.repository.question;

import fr.uca.springbootstrap.models.questions.AnswerCodeRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerCodeRunnerRepository extends JpaRepository<AnswerCodeRunner, Long> {
    Optional<AnswerCodeRunner> findById(Long aLong);
}

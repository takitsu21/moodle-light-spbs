package fr.uca.api.repository.question;

import fr.uca.api.models.questions.CodeRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRunnerRepository extends JpaRepository<CodeRunner, Long> {
    Optional<Long> findById(CodeRunner codeRunner);
}

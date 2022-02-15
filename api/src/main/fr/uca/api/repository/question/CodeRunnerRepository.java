package fr.uca.springbootstrap.repository.question;

import fr.uca.springbootstrap.models.questions.CodeRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRunnerRepository extends JpaRepository<CodeRunner, Long> {
    Optional<Long> findById(CodeRunner codeRunner);
}

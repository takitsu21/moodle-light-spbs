package fr.uca.api.repository.question;

import fr.uca.api.models.questions.Answer;
import fr.uca.api.models.questions.AnswerQCM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerQCMRepository extends JpaRepository<AnswerQCM, Long> {

    Optional<AnswerQCM> findById(long id);

    Boolean existsById(long id);

    Boolean existsByAnswer(String Answer);


    List<AnswerQCM> findAll();
}

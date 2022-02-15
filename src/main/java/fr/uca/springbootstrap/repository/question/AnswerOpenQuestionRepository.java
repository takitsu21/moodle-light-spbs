package fr.uca.springbootstrap.repository.question;

import fr.uca.auth.models.User;
import fr.uca.springbootstrap.models.questions.AnswerOpenQuestion;
import fr.uca.springbootstrap.models.questions.OpenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerOpenQuestionRepository extends JpaRepository<AnswerOpenQuestion, Long> {

    Optional<AnswerOpenQuestion> findById(long id);

    Boolean existsById(long id);

    List<AnswerOpenQuestion> findAllByStudent(User student);

    List<AnswerOpenQuestion> findAllByOpenQuestion(OpenQuestion openQuestion);

    List<AnswerOpenQuestion> findAll();
}

package fr.uca.springbootstrap.repository.question;

import fr.uca.springbootstrap.models.GradesQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradesQuestionnaireRepository extends JpaRepository<GradesQuestionnaire, Long> {
    Optional<GradesQuestionnaire> findById(Long aLong);
}

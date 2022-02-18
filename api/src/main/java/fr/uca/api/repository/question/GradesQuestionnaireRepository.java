package fr.uca.api.repository.question;

import fr.uca.api.models.GradesQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradesQuestionnaireRepository extends JpaRepository<GradesQuestionnaire, Long> {
    Optional<GradesQuestionnaire> findById(Long aLong);
}

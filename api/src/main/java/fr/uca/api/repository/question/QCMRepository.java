package fr.uca.api.repository.question;

import fr.uca.api.models.questions.QCM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QCMRepository extends JpaRepository<QCM, Long> {

    Optional<QCM> findById(long id);

    Boolean existsById(long id);

    Boolean existsByName(String name);

    List<QCM> findAll();
}

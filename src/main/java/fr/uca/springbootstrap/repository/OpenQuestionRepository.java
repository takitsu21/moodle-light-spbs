package fr.uca.springbootstrap.repository;

import fr.uca.springbootstrap.models.questions.OpenQuestion;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpenQuestionRepository extends JpaRepository<OpenQuestion, Long> {

    Optional<Open>

}

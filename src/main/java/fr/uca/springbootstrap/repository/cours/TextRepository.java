package fr.uca.springbootstrap.repository.cours;

import fr.uca.springbootstrap.models.Text;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TextRepository extends JpaRepository<Text, Long> {
    Optional<Text> findById(long id);

    Boolean existsById(long id);
}

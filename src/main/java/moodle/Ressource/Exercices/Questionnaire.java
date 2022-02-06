package moodle.Ressource.Exercices;

import moodle.Ressource.Ressource;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(	name = "questionnaires")
public class Questionnaire extends Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "suestion_questionnaires",
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questionnaire;

    public Questionnaire(String name, String description, List<Question> questionnaire) {
        super(name, description);
        this.questionnaire = questionnaire;
    }

    public Questionnaire() {

    }
}

package moodle.Ressource.Exercices;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "questionnaires")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private int number;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public Question(int number, String name, String description) {
        this.number = number;
        this.name = name;
        this.description = description;
    }

    public Question() {

    }
}

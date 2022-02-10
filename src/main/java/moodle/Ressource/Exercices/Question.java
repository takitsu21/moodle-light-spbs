package moodle.Ressource.Exercices;

import moodle.users.Student;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "questionnaires")
public abstract class Question {
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

    public abstract boolean succeedQuestion(Student student);
}

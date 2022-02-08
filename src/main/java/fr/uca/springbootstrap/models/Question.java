package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table( name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private int number;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 120)
    private String description;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Questionnaire getQuestionnaire() { return questionnaire; }
    public void setQuestionnaire(Questionnaire questionnaire) { this.questionnaire = questionnaire; }


}

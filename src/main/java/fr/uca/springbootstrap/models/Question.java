package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type",
                    discriminatorType = DiscriminatorType.STRING)
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false)
    private long id;

    @NotNull
    @Column(name = "question_nb")
    private int number;

    @NotBlank
    @Size(max = 50)
    @Column(name = "question_name")
    private String name;

    @NotBlank
    @Size(max = 120)
    @Column(name = "question_description")
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

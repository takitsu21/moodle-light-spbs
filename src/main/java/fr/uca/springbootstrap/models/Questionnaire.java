package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "questionnaire")
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionnaire_id", nullable = false)
    private long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "questionnaire_name")
    private String name;

    @NotBlank
    @Size(max = 1024)
    @Column(name = "questionnaire_description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Question> questionSet = new HashSet<>();

    public Questionnaire(){

    }

    public Questionnaire(String name, String description){
        this.name = name;
        this.description = description;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<Question> getQuestionSet() { return questionSet; }
    public void setQuestionSet(Set<Question> questionSet) { this.questionSet = questionSet; }


}

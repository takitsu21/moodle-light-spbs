package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "questionnaire")
public class Questionnaire extends Ressource {

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Question> questionSet = new HashSet<>();

    @OneToMany
    @JoinTable(name = "students_grades",
            joinColumns = @JoinColumn(name = "questionnaire"),
            inverseJoinColumns = @JoinColumn(name = "grades"))
    private Set<GradesQuestionnaire> studentsGrades = new HashSet<>();

    public Questionnaire(){

    }

    public Set<Question> getQuestionSet() { return questionSet; }
    public void setQuestionSet(Set<Question> questionSet) { this.questionSet = questionSet; }


}

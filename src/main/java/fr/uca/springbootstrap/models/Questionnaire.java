package fr.uca.springbootstrap.models;

import fr.uca.springbootstrap.models.questions.Question;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Questionnaire")
public class Questionnaire extends Ressource {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Question> questions;

    @OneToMany
    @JoinTable(name = "students_grades",
            joinColumns = @JoinColumn(name = "questionnaire"),
            inverseJoinColumns = @JoinColumn(name = "grades"))
    private Set<GradesQuestionnaire> studentsGrades;

    public Questionnaire() {

    public Questionnaire(String name, String description, int number){
        super(name, description, number);
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<GradesQuestionnaire> getStudentsGrades() { return studentsGrades; }
    public void setStudentsGrades(Set<GradesQuestionnaire> studentsGrades) { this.studentsGrades = studentsGrades; }
}

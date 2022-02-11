package fr.uca.springbootstrap.models;

import fr.uca.springbootstrap.models.questions.Question;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Questionnaire")
public class Questionnaire extends Ressource {

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Question> questions;

    @OneToMany
    @JoinTable(name = "students_grades",
            joinColumns = @JoinColumn(name = "questionnaire"),
            inverseJoinColumns = @JoinColumn(name = "grades"))
    private Set<GradesQuestionnaire> studentsGrades;

    public Questionnaire(){
        super();
    }

    public Questionnaire(String name, String description, int number){
        super(name, description, number);
        this.questions = new HashSet<>();
        this.studentsGrades = new HashSet<>();
    }


    public Set<Question> getQuestions() { return questions; }
    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    public Set<GradesQuestionnaire> getStudentsGrades() { return studentsGrades; }
    public void setStudentsGrades(Set<GradesQuestionnaire> studentsGrades) { this.studentsGrades = studentsGrades; }
    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    public int getNbQuestions() {
        return questions.size();
    }
}

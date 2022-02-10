package fr.uca.springbootstrap.models;

import fr.uca.springbootstrap.models.questions.Question;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("dictionnaire")
public class Questionnaire extends Ressource {


    @OneToMany(fetch = FetchType.EAGER)
    private Set<Question> questions = new HashSet<>();

    @OneToMany
    @JoinTable(name = "students_grades",
            joinColumns = @JoinColumn(name = "questionnaire"),
            inverseJoinColumns = @JoinColumn(name = "grades"))
    private Set<GradesQuestionnaire> studentsGrades = new HashSet<>();

    public Questionnaire(){
        super();
    }

    public Questionnaire(String name, String description, int number){
        super(name, description, number);
        this.studentsGrades = new HashSet<>();
        this.questions = new HashSet<>();
    }

    public Set<Question> getQuestions() { return questions; }
    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    public Set<GradesQuestionnaire> getStudentsGrades() { return studentsGrades; }
    public void setStudentsGrades(Set<GradesQuestionnaire> studentsGrades) { this.studentsGrades = studentsGrades; }
}

package fr.uca.springbootstrap.models;

import fr.uca.springbootstrap.models.questions.Question;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Questionnaire")
public class Questionnaire extends Ressource {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "questionnaire_question",
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> questions = new HashSet<>();

    @OneToMany
    @JoinTable(name = "students_grades",
            joinColumns = @JoinColumn(name = "questionnaire"),
            inverseJoinColumns = @JoinColumn(name = "grades"))
    private Set<GradesQuestionnaire> studentsGrades = new HashSet<>();

    public Questionnaire(){
        super();
    }

    public Questionnaire(String name, String description, Integer num) {
        super(name, description, num);
    }


    public Set<Question> getQuestions() { return questions; }
    public void setQuestions(Set<Question> questions) { this.questions = questions; }

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

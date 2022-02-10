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
    private Set<GradesQuestionnaire> studentsGrades = new HashSet<>();

    public Questionnaire(){
        super();
    }

    public Questionnaire(String name, String description, Integer num){
        super(name, description, num);
    }

    public Set<Question> getQuestions() { return questions; }
    public void setQuestions(Set<Question> questions) { this.questions = questions; }

    public void addQuestion(Question question) {
        questions.add(question);
        question.setQuestionnaire(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
//        question.setQuestionnaire(null); //pas sûr de ça, si le GB s'en charge de toute façon
    }

    public int getNbQuestions() {
        return questions.size();
    }
}

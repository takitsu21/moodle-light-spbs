package fr.uca.api.models;

import fr.uca.api.models.questions.Question;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "grades_id"))
    private Set<GradesQuestionnaire> studentsGrades = new HashSet<>();



    public Questionnaire() {

    }

    public Questionnaire(String name, String description, int number){
        super(name, description, number);
        this.questions = new HashSet<>();
        this.studentsGrades = new HashSet<>();
    }


    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    public int getNbQuestions() {
        return questions.size();
    }
    public Question findQuestionByName(String arg1) {
        for(Question question: questions){
            if(Objects.equals(question.getName(), arg1)){
                return question;
            }
        }
        return null;
    }

    public boolean containsQuestion(String arg1) {
        for(Question question: questions){
            if(Objects.equals(question.getName(), arg1)){
                return true;
            }
        }
        return false;
    }

    public Set<GradesQuestionnaire> getStudentsGrades() {
        return studentsGrades;
    }

    public void setStudentsGrades(Set<GradesQuestionnaire> studentsGrades) {
        this.studentsGrades = studentsGrades;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }
}

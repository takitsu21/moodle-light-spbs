package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.Questionnaire;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("open")
public class OpenQuestion extends Question {

    @OneToMany
    @JoinTable(name = "possible_answers",
            joinColumns = @JoinColumn(name = "open_question"),
            inverseJoinColumns = @JoinColumn(name = "answers"))
    private Set<Answer> possibleAnswers;

    @OneToMany
    @JoinTable(name = "answers",
            joinColumns = @JoinColumn(name = "open_question"),
            inverseJoinColumns = @JoinColumn(name = "answers"))
    private Set<Answer> answers;

    @ManyToMany
    @JoinTable(name = "students_anwser",
            joinColumns = @JoinColumn(name = "open_question"),
            inverseJoinColumns = @JoinColumn(name = "student_anwser_open_question"))
    private Set<AnswerOpenQuestion> answerOpenQuestionSet;


    public OpenQuestion(Set<Answer> answers, Set<AnswerOpenQuestion> answerOpenQuestionSet,
                        Set<Answer> possibleAnswers, String name, String description,
                        int number, Questionnaire questionnaire){
        super(number, name, description);
        this.answers = answers;
        this.answerOpenQuestionSet = answerOpenQuestionSet;
        this.possibleAnswers = possibleAnswers;
    }


    public OpenQuestion() {
        super();
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<AnswerOpenQuestion> getStudentAnswerOpenQuestionSet() {
        return answerOpenQuestionSet;
    }

    public void setStudentAnswerOpenQuestionSet(Set<AnswerOpenQuestion> answerOpenQuestionSet) {
        this.answerOpenQuestionSet = answerOpenQuestionSet;
    }

    public Set<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(Set<Answer> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }
}

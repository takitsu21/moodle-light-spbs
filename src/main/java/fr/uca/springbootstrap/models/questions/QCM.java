package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("qcm")
public class QCM extends Question {

    @ManyToMany
    @JoinTable(name = "possible_answers",
            joinColumns = @JoinColumn(name = "open_question"),
            inverseJoinColumns = @JoinColumn(name = "answers"))
    private Set<Answer> possibleAnswers;

    @OneToOne
    private Answer answer;

    @ManyToMany
    @JoinTable(name = "student_answer_qcm",
            joinColumns = @JoinColumn(name = "student"),
            inverseJoinColumns = @JoinColumn(name = "answer"))
    private Set<User> studentsAnswers;

    public QCM(int number, String name, String description, Questionnaire questionnaire) {
        super(number, name, description, questionnaire);
    }

    public QCM() {
        super();
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Set<User> getStudentsAnswers() {
        return studentsAnswers;
    }

    public void setStudentsAnswers(Set<User> studentsAnswers) {
        this.studentsAnswers = studentsAnswers;
    }

    public Set<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(Set<Answer> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }
}

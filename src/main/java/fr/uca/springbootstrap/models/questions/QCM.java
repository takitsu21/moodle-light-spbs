package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.Questionnaire;
import fr.uca.springbootstrap.models.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("qcm")
public class QCM extends Question {

    @ManyToMany
    @JoinTable( name = "possible_answers",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers"))
    private Set<Answer> possibleAnswers;

    @OneToOne
    private Answer answer;

    @ManyToMany

    @JoinTable(name = "student_answer_qcm",
            joinColumns = @JoinColumn(name = "qcm"),
            inverseJoinColumns = @JoinColumn(name = "student_answer"))
    private Set<AnswerQCM> studentsAnswers;

    public QCM(int number, String name, String description) {
        super(number, name, description);
    }

    public QCM() {
        super();
    }

    public Answer getAnswer() { return answer; }
    public void setAnswer(Answer answer) { this.answer = answer; }

    public Set<AnswerQCM> getStudentsAnswers() { return studentsAnswers; }
    public void setStudentsAnswers(Set<AnswerQCM> studentsAnswers) { this.studentsAnswers = studentsAnswers; }

    public Set<Answer> getPossibleAnswers() { return possibleAnswers; }
    public void setPossibleAnswers(Set<Answer> possibleAnswers) { this.possibleAnswers = possibleAnswers; }
}

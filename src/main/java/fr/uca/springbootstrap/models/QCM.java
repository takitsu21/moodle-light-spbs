package fr.uca.springbootstrap.models;

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
            joinColumns = @JoinColumn(name = "student"),
            inverseJoinColumns = @JoinColumn(name = "answer"))
    private Set<User> studentsAnswers;

    public Answer getAnswer() { return answer; }
    public void setAnswer(Answer answer) { this.answer = answer; }

    public Set<User> getStudentsAnswers() { return studentsAnswers; }
    public void setStudentsAnswers(Set<User> studentsAnswers) { this.studentsAnswers = studentsAnswers; }

    public Set<Answer> getPossibleAnswers() { return possibleAnswers; }
    public void setPossibleAnswers(Set<Answer> possibleAnswers) { this.possibleAnswers = possibleAnswers; }
}

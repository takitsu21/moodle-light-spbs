package fr.uca.springbootstrap.models.questions;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("open")
public class OpenQuestion extends Question {

    @OneToMany
    @JoinTable( name = "possible_answers",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers"))
    private Set<Answer> possibleAnswers;

    @OneToMany
    @JoinTable( name = "answers",
            joinColumns = @JoinColumn(name ="open_question"),
            inverseJoinColumns = @JoinColumn(name="answers"))
    private Set<Answer> answers;

    @ManyToMany
    private Set<StudentAnswerOpenQuestion> studentAnswerOpenQuestionSet;

    public Set<Answer> getAnswers() { return answers; }
    public void setAnswers(Set<Answer> answers) { this.answers = answers; }

    public Set<StudentAnswerOpenQuestion> getStudentAnswerOpenQuestionSet() { return studentAnswerOpenQuestionSet; }
    public void setStudentAnswerOpenQuestionSet(Set<StudentAnswerOpenQuestion> studentAnswerOpenQuestionSet) { this.studentAnswerOpenQuestionSet = studentAnswerOpenQuestionSet; }

    public Set<Answer> getPossibleAnswers() { return possibleAnswers; }
    public void setPossibleAnswers(Set<Answer> possibleAnswers) { this.possibleAnswers = possibleAnswers; }
}

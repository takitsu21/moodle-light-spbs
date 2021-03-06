package fr.uca.api.models.questions;

import fr.uca.api.models.UserRef;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class AnswerOpenQuestion {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Answer> answers;

    @OneToOne
    @JoinColumn(name = "student")
    private UserRef student;

    public AnswerOpenQuestion() {

    }

    public AnswerOpenQuestion(Set<Answer> answers, UserRef student) {
        this.answers = answers;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public UserRef getStudent() {
        return student;
    }

    public void setStudent(UserRef student) {
        this.student = student;
    }

    public boolean answerContains(String s) {
        for(Answer answer: answers){
            if(Objects.equals(answer.getAnswer(), s)){
                return true;
            }
        }
        return false;
    }
}

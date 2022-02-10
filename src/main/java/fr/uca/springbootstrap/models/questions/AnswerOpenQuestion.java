package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.User;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AnswerOpenQuestion {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany
    private Set<Answer> answers;

    @OneToOne
    @JoinColumn(name = "student")
    private User student;

    @OneToOne
    @JoinColumn(name = "open_question")
    private OpenQuestion openQuestion;

    public AnswerOpenQuestion() {

    }

    public AnswerOpenQuestion(Set<Answer> answers, User student, OpenQuestion openQuestion) {
        this.answers = answers;
        this.student = student;
        this.openQuestion = openQuestion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpenQuestion getOpenQuestion() {
        return openQuestion;
    }

    public void setOpenQuestion(OpenQuestion openQuestion) {
        this.openQuestion = openQuestion;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}

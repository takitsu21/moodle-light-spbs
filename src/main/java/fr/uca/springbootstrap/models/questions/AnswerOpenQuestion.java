package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.User;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AnswerOpenQuestion {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Answer> answers;

    @OneToOne()
    @JoinColumn(name = "student")
    private User student;

    public AnswerOpenQuestion() {

    }

    public AnswerOpenQuestion(Set<Answer> answers, User student) {
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

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}

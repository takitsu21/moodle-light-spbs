package fr.uca.springbootstrap.models.questions;

import fr.uca.auth.models.User;

import javax.persistence.*;

@Entity
public class AnswerCodeRunner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Answer answer;

    @OneToOne
    @JoinColumn(name = "student")
    private User student;

    public AnswerCodeRunner() {

    }

    public AnswerCodeRunner(Answer answer, User student) {
        this.answer = answer;
        this.student = student;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}



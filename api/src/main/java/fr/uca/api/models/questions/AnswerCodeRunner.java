package fr.uca.api.models.questions;

import fr.uca.api.models.UserRef;

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
    private UserRef student;

    public AnswerCodeRunner() {

    }

    public AnswerCodeRunner(Answer answer, UserRef student) {
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

    public UserRef getStudent() {
        return student;
    }

    public void setStudent(UserRef student) {
        this.student = student;
    }
}



package fr.uca.api.models.questions;

import fr.uca.api.models.User;
import fr.uca.api.models.UserRef;

import javax.persistence.*;

@Entity
public class AnswerQCM {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private Answer answer;

    @OneToOne
    @JoinColumn(name = "student")
    private UserRef student;

    public AnswerQCM(){

    }

    public AnswerQCM(Answer answer,UserRef student){
        this.answer = answer;
        this.student = student;
    }

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public Answer getAnswer() { return answer; }
    public void setAnswer(Answer answer) { this.answer = answer; }

    public UserRef getStudent() { return student; }
    public void setStudent(UserRef student) { this.student = student; }
}

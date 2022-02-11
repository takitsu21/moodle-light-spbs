package fr.uca.springbootstrap.models.questions;

import fr.uca.springbootstrap.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Answer {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 100)
    private String answer;

    @ManyToOne
    private User student;

    public Answer() {

    }

    public Answer(String content) {
        this.answer=content;
    }

    public Answer(String answer, User user) {
        this.answer=answer;
        this.student=user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

 //   public User getStudent() { return student; }
 //   public void setStudent(User student) { this.student = student; }
}

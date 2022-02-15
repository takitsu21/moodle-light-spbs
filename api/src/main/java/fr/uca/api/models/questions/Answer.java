package fr.uca.api.models.questions;

import fr.uca.api.models.UserRef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Answer {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size(max = 100)
    private String answer;

    @ManyToOne
    private UserRef student;

    public Answer() {

    }

    public Answer(String content) {
        this.answer=content;
    }

    public Answer(String answer, UserRef user) {
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

}

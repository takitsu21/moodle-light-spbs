package fr.uca.springbootstrap.models.questions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Answer {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size( max = 100 )
    private String answer;


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}

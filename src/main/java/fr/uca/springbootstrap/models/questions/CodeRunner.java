package fr.uca.springbootstrap.models.questions;

import javax.persistence.*;

@Entity
@DiscriminatorValue("code_runner")
public class CodeRunner extends Question {

    @ManyToOne
    private Answer anwser;
    private String test;

    public CodeRunner(int number,
                      String name,
                      String description,
                      String test, Answer answer) {
        super(number, name, description);
        this.test = test;
        this.anwser = answer;
    }

    public CodeRunner() {

    }

    public Answer getAnwser() {
        return anwser;
    }

    public void setAnwser(Answer anwser) {
        this.anwser = anwser;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}

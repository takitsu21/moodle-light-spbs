package fr.uca.springbootstrap.models.questions;

import moodle.users.Student;

import javax.persistence.*;
import java.util.Map;

@Entity
@DiscriminatorValue("code_runner")
public class CodeRunner extends Question {
    private String anwser;
    private String test;

    public CodeRunner(int number, String name, String description, String test, String answer) {
        super(number, name, description);
        this.test = test;
        this.anwser = answer;
    }

    public CodeRunner() {

    }

    public String getAnwser() {
        return anwser;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}

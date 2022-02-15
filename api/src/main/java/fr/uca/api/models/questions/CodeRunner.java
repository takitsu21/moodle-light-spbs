package fr.uca.api.models.questions;

import fr.uca.api.models.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("code_runner")
public class CodeRunner extends Question {

    @ManyToOne
    private Answer anwser;
    private String test;

    @ManyToMany
    @JoinTable(name = "student_answer_code_runner",
            joinColumns = @JoinColumn(name = "code_runner_id"),
            inverseJoinColumns = @JoinColumn(name = "student_answer"))
    private Set<AnswerCodeRunner> studentsAnswers;

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

    public Set<AnswerCodeRunner> getStudentsAnswers() {
        return studentsAnswers;
    }

    public void setStudentsAnswers(Set<AnswerCodeRunner> studentsAnswers) {
        this.studentsAnswers = studentsAnswers;
    }

    public AnswerCodeRunner getStudentAnswer(User user) {
        for (AnswerCodeRunner answerCodeRunner : studentsAnswers) {
            if (answerCodeRunner.getStudent().equals(user)) {
                return answerCodeRunner;
            }
        }
        return null;
    }

    public boolean studentAnswerContains(User user) {
        for (AnswerCodeRunner answerCodeRunner : studentsAnswers) {
            if (answerCodeRunner.getStudent().equals(user)) {
                return true;
            }
        }
        return false;
    }

}

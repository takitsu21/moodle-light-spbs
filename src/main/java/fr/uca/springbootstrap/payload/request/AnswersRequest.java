package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class AnswersRequest {

    private Set<MyAnswer> answers;

    public Set<MyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<MyAnswer> answers) {
        this.answers = answers;
    }
}

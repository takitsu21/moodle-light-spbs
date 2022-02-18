package payload.request;

import java.util.Set;

public class AnswerRequest {

    private Set<MyAnswer> answers;

    public AnswerRequest() {
    }

    public Set<MyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<MyAnswer> answers) {
        this.answers = answers;
    }

    public AnswerRequest(Set<MyAnswer> answers){
        this.answers = answers;
    }
}

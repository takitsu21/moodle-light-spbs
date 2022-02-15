package payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class AnswersRequest {

    private Set<MyAnswer> answers=new HashSet<>();

    public AnswersRequest() {
    }

    public AnswersRequest(HashSet<MyAnswer> ans) {
        for(MyAnswer a: ans){
            answers.add(a);
        }
    }

    public Set<MyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<MyAnswer> answers) {
        this.answers = answers;
    }
}

package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class MyAnswer {
    @NotBlank
    private String answer;

    public MyAnswer() {
    }

    public MyAnswer(String content) {
        this.answer = content;
    }

    public String getContent() {
        return answer;
    }

    public void setContent(String content) {
        this.answer = content;
    }
}

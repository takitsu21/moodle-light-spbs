package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MyAnswer {
    @NotBlank
    @Size( max = 100 )
    private String answer;

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


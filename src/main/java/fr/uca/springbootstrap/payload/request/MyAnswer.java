package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


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


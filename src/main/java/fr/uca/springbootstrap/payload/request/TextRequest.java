package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TextRequest {
    @NotBlank
    private Set<MyText> texts;

    public TextRequest() {

    }




    public Set<MyText> getTexts() {
        return texts;
    }

    public void setTexts(Set<MyText> texts) {
        this.texts = texts;
    }
}

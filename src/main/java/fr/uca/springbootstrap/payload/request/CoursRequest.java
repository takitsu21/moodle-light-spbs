package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class CoursRequest {
    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    private Set<String> text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getText() {
        return text;
    }

    public void setText(Set<String> text) {
        this.text = text;
    }
}

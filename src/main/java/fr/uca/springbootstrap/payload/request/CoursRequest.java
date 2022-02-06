package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class CoursRequest {
    @NotBlank
    @Size(min=2, max=20)
    private String name;

    @NotBlank
    @Size(min=2, max=255)
    private String description;

    private Set<String> text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getText() {
        return text;
    }

    public void setText(Set<String> text) {
        this.text = text;
    }
}

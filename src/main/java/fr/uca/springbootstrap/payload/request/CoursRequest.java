package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class CoursRequest {
    @NotBlank
    @Size(min=2, max=20)
    private String name;

    @NotBlank
    @Size(min=2, max=128)
    private String description;

    @NotNull
    private Integer num;


    public CoursRequest() {
    }

    public CoursRequest(String name, String description, Integer num) {
        this.name = name;
        this.description = description;
        this.num = num;
    }

    private Set<String> texts;

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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Set<String> getTexts() {
        return texts;
    }

    public void setTexts(Set<String> texts) {
        this.texts = texts;
    }
}

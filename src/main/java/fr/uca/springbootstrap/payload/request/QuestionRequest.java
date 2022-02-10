package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QuestionRequest {
    @NotNull
    private int number;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 120)
    private String description;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

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
}

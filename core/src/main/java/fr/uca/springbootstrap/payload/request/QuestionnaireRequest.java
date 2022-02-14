package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class QuestionnaireRequest {
    @NotBlank
    @Size(min=2, max=30)
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private int num;

    public QuestionnaireRequest(String name, String description, int num) {
        this.name = name;
        this.description = description;
        this.num = num;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

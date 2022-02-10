package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;

public class QuestionRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public QuestionRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }
}

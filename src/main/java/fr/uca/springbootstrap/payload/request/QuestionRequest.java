package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QuestionRequest {

    @NotNull
    private int number;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public QuestionRequest(String name, String description, int number) {
        this.name = name;
        this.description = description;
        this.number = number;
    }

    public QuestionRequest(){

    }

    public void setNumber(int number) { this.number = number; }
    public int getNumber() { return number; }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }
}

package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RessourceRequest {
    @NotBlank
    @Size(min=2, max=20)
    private String name;

    @NotBlank
    @Size(min=2, max=128)
    private String description;

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

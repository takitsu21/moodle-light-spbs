package fr.uca.springbootstrap.payload.request;

import javax.validation.constraints.NotBlank;

public class ModuleRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

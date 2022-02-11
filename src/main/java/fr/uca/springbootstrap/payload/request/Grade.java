package fr.uca.springbootstrap.payload.request;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Grade {

    @NotNull
    private Integer note;

    @NotNull
    private Integer nbQuestion;

    @NotBlank
    private String username;

    public Grade(Integer note, Integer nbQuestion, String username) {
        this.note = note;
        this.nbQuestion = nbQuestion;
        this.username = username;
    }

    public Grade() {
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Integer getNbQuestion() {
        return nbQuestion;
    }

    public void setNbQuestion(Integer nbQuestion) {
        this.nbQuestion = nbQuestion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

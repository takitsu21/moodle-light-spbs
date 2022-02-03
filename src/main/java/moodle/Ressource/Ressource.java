package moodle.Ressource;

import moodle.Ressource.Cours.Cours;
import moodle.Ressource.Exercices.Questionnaire;

import java.util.List;

public class Ressource {
    private String name;
    private String description;
    private List<Cours> cours;
    private List<Questionnaire> questionnaires;
    private boolean visibility=false;

    public Ressource(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}

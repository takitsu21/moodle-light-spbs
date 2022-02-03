package moodle.Ressource;

import moodle.Ressource.Cours.Cours;
import moodle.Ressource.Exercices.Questionnaire;

import java.util.List;

public class Ressource {
    private String name;
    private String description;
    private List<Cours> cours;
    private List<Questionnaire> questionnaires;

    public Ressource(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

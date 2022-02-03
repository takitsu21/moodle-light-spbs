package Ressource;

import Ressource.Cours.Cours;
import Ressource.Exercices.Questionnaire;

import java.util.ArrayList;

public class Ressource {
    private String name;
    private String description;
    private ArrayList<Cours> cours;
    private ArrayList<Questionnaire> questionnaires;

    public Ressource(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

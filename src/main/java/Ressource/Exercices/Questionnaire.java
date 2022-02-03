package Ressource.Exercices;

import Ressource.Ressource;

import java.util.List;

public class Questionnaire extends Ressource {
    List<Question> questionnaire;

    public Questionnaire(String name, String description, List<Question> questionnaire) {
        super(name, description);
        this.questionnaire = questionnaire;
    }
}

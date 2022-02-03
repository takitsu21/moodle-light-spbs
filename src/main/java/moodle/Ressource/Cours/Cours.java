package moodle.Ressource.Cours;

import moodle.Ressource.Ressource;

import java.util.List;

public class Cours extends Ressource {
    private List<Text> texts;

    public Cours(String name, String description) {
        super(name, description);
    }
}

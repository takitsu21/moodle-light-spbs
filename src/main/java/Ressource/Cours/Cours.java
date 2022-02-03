package Ressource.Cours;

import Ressource.Ressource;

import java.util.List;

public class Cours extends Ressource {
    List<Text> texts;

    public Cours(String name, String description) {
        super(name, description);
    }
}

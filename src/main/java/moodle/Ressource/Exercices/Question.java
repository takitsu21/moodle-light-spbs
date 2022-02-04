package moodle.Ressource.Exercices;

import moodle.users.Student;

public abstract class Question {
    private int number;
    private String name;
    private String description;

    public Question(int number, String name, String description) {
        this.number = number;
        this.name = name;
        this.description = description;
    }

    public abstract boolean succeedQuestion(Student student);

}

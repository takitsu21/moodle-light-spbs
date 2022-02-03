package moodle.Ressource.Exercices;

import moodle.users.Student;

import java.util.HashMap;

public class QCM extends Question {
    private String[] possibleAnswers;
    private int answer;
    private HashMap<Student, Integer> sut;

    public QCM(int number, String name, String description, String[] possibleAnswers, String answer) {
        super(number, name, description);
        this.possibleAnswers = possibleAnswers;
        this.answer = answer;
    }
}

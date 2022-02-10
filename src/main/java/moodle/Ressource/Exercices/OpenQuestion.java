package moodle.Ressource.Exercices;

import moodle.users.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenQuestion extends Question {
    private String[] possibleAnswers;
    private int[] answers;
    private HashMap<Student, List<Integer>> studentsAnswer;

    public OpenQuestion(int number, String name, String description, String[] possibleAnswers, int[] answers) {
        super(number, name, description);
        this.possibleAnswers = possibleAnswers;
        this.answers = answers;
        this.studentsAnswer = new HashMap<Student, List<Integer>>();
    }

    public boolean succeedQuestion(Student student) {
        for (int a : answers) {
            if (!studentsAnswer.get(student).contains(a)) {
                return false;
            }
        }
        return true;
    }

    public void addStudentAnswer(Student student, int answer) {
        if (!studentsAnswer.containsKey(student)) {
            studentsAnswer.put(student, new ArrayList<>());
        }
        if (!studentsAnswer.get(student).contains(answer)) {
            studentsAnswer.get(student).add(answer);
        }
    }

    public void removeStudentAnswer(Student student, int answer) {
        if (!studentsAnswer.containsKey(student)) {
            studentsAnswer.put(student, new ArrayList<>());
        }
        studentsAnswer.get(student).remove((Integer) answer);
    }

    public int[] getAnswers() {
        return answers;
    }

    public List<Integer> getStudentAnswers(Student student) {
        return studentsAnswer.get(student);
    }


}

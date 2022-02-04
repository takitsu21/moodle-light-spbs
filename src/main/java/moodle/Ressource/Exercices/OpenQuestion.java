package moodle.Ressource.Exercices;

import moodle.users.Student;

import java.util.*;

public class OpenQuestion extends Question {
    private String[] possibleAnswers;
    private int[] answers;
    private HashMap<Student, List<Integer>> studentsAnswer;

    public OpenQuestion(int number, String name, String description, String[] possibleAnswers, int[] answers) {
        super(number, name, description);
        this.possibleAnswers = possibleAnswers;
        this.answers = answers;
        this.studentsAnswer = new HashMap<Student,List<Integer>>();
    }

    public boolean succeedQuestion(Student student){
        for (int a : answers){
            if (!studentsAnswer.get(student).contains(a)){
                return false;
            }
        }
        return true;
    }

    public void addAnswer(Student student, int answer){
        if (! studentsAnswer.get(student).contains(answer)){
            studentsAnswer.get(student).add(answer);
        }
    }

    public void removeAnswer(Student student, int answer){
        studentsAnswer.get(student).remove(answer);
    }






}

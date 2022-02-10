package moodle.Ressource.Exercices;

import moodle.users.Student;

import java.util.HashMap;

public class QCM extends Question {
    private String[] possibleAnswers;
    private int answer;
    private HashMap<Student, Integer> studentsAnswer;

    public QCM(int number, String name, String description, String[] possibleAnswers, int answer) {
        super(number, name, description);
        this.possibleAnswers = possibleAnswers;
        this.answer = answer;
        this.studentsAnswer = new HashMap<Student, Integer>();
    }

    public void setStudentAnswer(Student student, int answer) {
        this.studentsAnswer.put(student, answer);
    }

    public boolean succeedQuestion(Student student) {
        return answer == studentsAnswer.get(student);
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int newAnswer) {
        this.answer = newAnswer;
    }

    public int getStudentsAnswer(Student student) {
        return studentsAnswer.get(student);
    }
}

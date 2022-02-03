package moodle.Ressource.Exercices;

import moodle.Ressource.Ressource;
import moodle.users.Student;

import java.util.HashMap;
import java.util.List;

public class Questionnaire extends Ressource {
    private List<Question> questionnaire;
    private HashMap<Student, Integer> grades;

    public Questionnaire(String name, String description, List<Question> questionnaire, List<Student> sutdents) {
        super(name, description);
        this.questionnaire = questionnaire;
        this.grades = new HashMap<>();
        for (Student student : sutdents.toArray(new Student[0])){
            grades.put(student, null);
        }
    }

    public List<Question> getQuestionnaire() {
        return questionnaire;
    }

    public HashMap<Student, Integer> getGrades() {
        return grades;
    }

    public void validateQuestionnaire(Student student){

    }

}


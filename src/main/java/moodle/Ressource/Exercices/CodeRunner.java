package moodle.Ressource.Exercices;

import moodle.users.Student;

public class CodeRunner extends Question {

    public CodeRunner(int number, String name, String description) {
        super(number, name, description);
    }


    @Override
    public boolean succeedQuestion(Student student) {
        return false;
    }
}

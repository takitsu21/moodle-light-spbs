package moodle;

import moodle.Ressource.Cours.Cours;
import moodle.Ressource.Ressource;
import moodle.users.Student;
import moodle.users.Teacher;
import moodle.users.User;

import java.util.List;

public class Module {
    private List<Ressource> resources;
    private List<Teacher> teacher;
    private List<Student> students;
    private String name;

    public Module(String name, List<Ressource> resources) {
        this.resources = resources;
        this.name = name;
    }

    public boolean addStudent(Student student) {
        if (!students.contains(student)) {
            return students.add(student);
        }
        return false;
    }

    public boolean removeStudent(Student student) {
        return students.remove(student);
    }
}

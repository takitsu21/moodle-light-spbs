package moodle;

import moodle.Ressource.Cours.Cours;
import moodle.Ressource.Ressource;
import moodle.users.Student;
import moodle.users.Teacher;
import moodle.users.User;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private List<Ressource> resources;
    private final List<Teacher> teacher = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private String name;

    public Module(String name, List<Ressource> resources) {
        this.resources = resources;
        this.name = name;
    }

    public Module(String name) {
        this(name, new ArrayList<>());
    }

    public List<Ressource> getResources() {
        return resources;
    }

    public void setResources(List<Ressource> resources) {
        this.resources = resources;
    }

    public List<Teacher> getTeacher() {
        return teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

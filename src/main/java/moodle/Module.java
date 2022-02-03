package moodle;

import moodle.Ressource.Ressource;
import moodle.users.User;

import java.util.List;

public class Module {
    private List<Ressource> resources;
    private List<User> teachers;
    private List<User> students;
    private String name;

    public Module(String name, List<Ressource> resources) {
        this.resources = resources;
        this.name = name;
    }

    public List<User> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<User> teacher) {
        this.teachers = teacher;
    }

    public String getName() {
        return name;
    }
}

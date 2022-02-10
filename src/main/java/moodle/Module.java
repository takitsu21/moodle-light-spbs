package moodle;

import moodle.Ressource.Ressource;
import moodle.users.Student;
import moodle.users.Teacher;
import moodle.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Module {
    private Map<String, Ressource> resources = new HashMap<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private String name;

    public Module(String name, Map<String, Ressource> resources) {
        this.resources = resources;
        this.name = name;
    }

    public Module(String name) {
        this(name, new HashMap<>());
    }

    public boolean assignTeacher(Teacher teacher) {
        if (teachers.isEmpty()) {
            teachers.add(teacher);
            return true;
        }
        return false;
    }

    public boolean assignUser(Teacher teacherAssign, User toAssign) {
        if (teacherAssign != null && teachers.contains(teacherAssign)) {
            if (toAssign instanceof Teacher) {
                teachers.add((Teacher) toAssign);
            } else {
                students.add((Student) toAssign);
            }
            return true;
        }
        return false;
    }

    public boolean removeUser(Teacher teacherAssign, User toRemove) {
        if (teacherAssign != null && teachers.contains(teacherAssign)) {
            if (toRemove instanceof Teacher) {
                teachers.remove((Teacher) toRemove);
            } else {
                students.remove((Student) toRemove);
            }
            return true;
        }
        return false;
    }

    public boolean changeVisibilityOfRessource(Teacher teacher, String ressource, boolean visibility) {
        if (teachers.contains(teacher)) {
            resources.get(ressource).setVisibility(visibility);
            return true;
        }
        return false;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teacher) {
        this.teachers = teacher;
    }

    public List<Teacher> getTeacher() {
        return teachers;
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

    public Map<String, Ressource> getResources() {
        return resources;
    }
}

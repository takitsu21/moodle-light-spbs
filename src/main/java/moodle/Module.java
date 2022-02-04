package moodle;

import moodle.Ressource.Ressource;
import moodle.users.Student;
import moodle.users.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private List<Ressource> resources = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
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

    public boolean assignTeacher(Teacher teacher) {
        return assignTeacher(null, teacher);
    }

    public boolean assignTeacher(Teacher teacherAssign, Teacher teacherToAssign) {
        if ((teachers.isEmpty() && teacherAssign == null) || teachers.contains(teacherToAssign)) {
            teachers = List.of(teacherToAssign);
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

package moodle;

import moodle.Ressource.Ressource;
import moodle.users.Teacher;
import moodle.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Module {
    private List<Ressource> resources=new ArrayList<>();
    private List<User> teachers=new ArrayList<>();
    private List<User> students=new ArrayList<>();
    private String name;

    public Module(String name, List<Ressource> resources) {
        this.resources = resources;
        this.name = name;
    }

    public boolean assignTeacher(Teacher teacher){
        return assignTeacher(null, teacher);
    }

    public boolean assignTeacher(Teacher teacherAssign, Teacher teacherToAssign){
        if ((teachers.isEmpty() && teacherAssign==null) || teachers.contains(teacherToAssign)){
            teachers = List.of(teacherToAssign);
            return true;
        }
        return false;
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

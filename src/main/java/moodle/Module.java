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
        if (teachers.isEmpty()){
            teachers.add(teacher);
            return true;
        }
        return false;
    }

    public boolean assignTeacher(Teacher teacherAssign, Teacher teacherToAssign){
        if (teacherAssign!=null && teachers.contains(teacherAssign)){
            teachers.add(teacherToAssign);
            return true;
        }
        return false;
    }

    public boolean deleteTeacher(Teacher teacherAssign, Teacher teacherToAssign){
        if (teacherAssign!=null && teachers.contains(teacherAssign)){
            teachers.remove(teacherToAssign);
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

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }
}

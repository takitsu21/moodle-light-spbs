package moodle;

import moodle.Ressource.Ressource;
import moodle.users.Teacher;
import moodle.users.User;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private List<Ressource> resources=new ArrayList<>();
    private List<User> teachers=new ArrayList<>();
    private List<User> students=new ArrayList<>();
    private String name;

    public Module(String name, List<Ressource> resources) {
        this.resources = resources;
        this.name = name;
    }

    public boolean assignUser(Teacher teacher){
        if (teachers.isEmpty()){
            teachers.add(teacher);
            return true;
        }
        return false;
    }

    public boolean assignUser(Teacher teacherAssign, User toAssign){
        if (teacherAssign!=null && teachers.contains(teacherAssign)){
            if(toAssign instanceof Teacher) {
                teachers.add(toAssign);
            }
            else{
                students.add(toAssign);
            }
            return true;
        }
        return false;
    }

    public boolean removeUser(Teacher teacherAssign, User toRemove){
        if (teacherAssign!=null && teachers.contains(teacherAssign)){
            if(toRemove instanceof Teacher) {
                teachers.remove(toRemove);
            }
            else {
                students.remove(toRemove);
            }
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

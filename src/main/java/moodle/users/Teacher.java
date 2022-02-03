package moodle.users;

import moodle.Module;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    public Teacher(String name, String password) {
        super(name, password);
    }

    public boolean assignAModule(Module module){
        if (module.getTeachers()==null){
            module.setTeachers(List.of(this));
            return true;
        }
        return false;
    }


}

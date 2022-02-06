package moodle.users;


public class Student extends User {

    public Student(String name, String password) {
        super(Role.STUDENT, name, password);
    }



}

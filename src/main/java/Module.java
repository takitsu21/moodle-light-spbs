import Ressource.Ressource;
import users.User;

import java.util.List;

public class Module {
    private List<Ressource> resources;
    private User teacher;
    private List<User> students;
    private String name;

    public Module(List<Ressource> resources, String name) {
        this.resources = resources;
        this.name = name;
    }
}

import Ressource.Ressource;
import users.User;

import java.util.List;

public class Module {
    List<Ressource> resources;
    List<User> users;
    String name;

    public Module(List<Ressource> resources, String name) {
        this.resources = resources;
        this.name = name;
    }
}

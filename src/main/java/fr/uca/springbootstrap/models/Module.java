package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_modules",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ressources_modules",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
    private Set<Ressource> ressources = new HashSet<>();

    public Module() {
    }

    public Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getParticipants() {
        return participants;
    }
    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Ressource> getRessources() { return ressources; }
    public void setRessources(Set<Ressource> ressources) { this.ressources = ressources; }

    public Ressource findRessourceByName(String name){
        for(Ressource ressource: ressources){
            if(Objects.equals(ressource.getName(), name)){
                return ressource;
            }
        }
        return null;
    }
}

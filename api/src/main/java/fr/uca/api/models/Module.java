package fr.uca.api.models;

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
    private Set<UserRef> participants = new HashSet<>();
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

    /**
     * Compares the ID of the given user to the ID of the currently registered users.
     * @param user User whose ID will be looked for
     * @return true if an ID matches that of the given user, false otherwise
     */
    public boolean containsParticipant(UserRef user) {
        for (UserRef participant : participants) {
            if (participant.getUserId().longValue() == user.getUserId().longValue()) {
                return true;
            }
        }
        return false;
    }

    public Set<UserRef> getParticipants() {
        return participants;
    }
    public void setParticipants(Set<UserRef> participants) {
        this.participants = participants;
    }

    public void addRessource(Ressource ressource) {
        ressources.add(ressource);
//        ressource.getModules().add(this);
    }

    public void removeRessource(Ressource ressource) {
        ressources.remove(ressource);
//        ressource.getModules().remove(this);
    }

    /**
     * Compares the ID of the given resource to the ID of the currently registered auth.service.resources.
     * @param ressource Resource whose ID will be looked for
     * @return true if an ID matches that of the given resource, false otherwise
     */
    public boolean containsRessource(Ressource ressource) {
        return containsRessource(ressource.getName());
    }

    public boolean containsRessource(String ressource) {
        for (Ressource currentRessource : ressources) {
            if (Objects.equals(currentRessource.getName(), ressource)) {
                return true;
            }
        }
        return false;
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

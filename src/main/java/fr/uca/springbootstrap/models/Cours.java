package fr.uca.springbootstrap.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cours")
public class Cours extends Ressource {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cours_text",
            joinColumns = @JoinColumn(name = "cours_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id"))
    private Set<Text> texts;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(	name = "cours_ressources",
//            joinColumns = @JoinColumn(name = "cours_id"),
//            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
//    private Set<Ressource> modules;

    public Cours() {
        super();
    }

    public Cours(String name, String description) {
        super(name, description);
    }

    public Set<Text> getTexts() {
        return texts;
    }

    public void setTexts(Set<Text> texts) {
        this.texts = texts;
    }
}

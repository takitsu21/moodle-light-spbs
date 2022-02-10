package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@DiscriminatorValue("Cours")
public class Cours extends Ressource {
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public Cours(String name, String description, Integer num) {
        super(name, description, num);
    }

    public Set<Text> getTexts() {
        return texts;
    }

    public void setTexts(Set<Text> texts) {
        this.texts = texts;
    }
}

package fr.uca.springbootstrap.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(	name = "cours")
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "cours_text",
            joinColumns = @JoinColumn(name = "cours_id"),
            inverseJoinColumns = @JoinColumn(name = "text_id"))
    private Set<Text> texts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "cours_ressources",
            joinColumns = @JoinColumn(name = "cours_id"),
            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
    private Set<Ressource> modules;

    public Cours() {
    }

    public Cours(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Text> getTexts() {
        return texts;
    }

    public void setTexts(Set<Text> texts) {
        this.texts = texts;
    }

    public Set<Ressource> getModules() {
        return modules;
    }

    public void setModules(Set<Ressource> modules) {
        this.modules = modules;
    }
}
